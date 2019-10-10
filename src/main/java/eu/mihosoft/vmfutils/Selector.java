/*
 * Copyright 2019-2019 Michael Hoffer <info@michaelhoffer.de>. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.mihosoft.vmfutils;

import java.util.function.Predicate;
import java.util.stream.Collectors;

import eu.mihosoft.vmf.runtime.core.Property;
import eu.mihosoft.vmf.runtime.core.VObject;
import vjavax.observer.Subscription;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public interface Selector {

    public static Selector selectObject() {
        return new SelectorImpl();
    }

    public static PropSelector selectProp() {
        return new PropSelectorImpl();
    }

    public static StringPropSelector selectStringProp() {
        return new StringPropSelectorImpl();
    }

    public static DoublePropSelector selectDoubleProp() {
        return new DoublePropSelectorImpl();
    }

    public static IntegerPropSelector selectIntegerProp() {
        return new IntegerPropSelectorImpl();
    }

    public static ListPropSelector selectListProp() {
        return new ListPropSelectorImpl();
    }

    public Selector withProperty(Predicate<Property> propPred);
    public Selector withProperty(PropSelector propSel);

    public Selector withType(String typeName);
    public Predicate<VObject> asPredicate();

    public Collection<VObject> selectFrom(Collection<? extends VObject> collection);
    public Collection<VObject> selectFrom(VObject vObj);

    public Subscription syncWith(VObject vObj, Collection<VObject> syncedCollection);
}

class SelectorImpl implements Selector {

    private final List<Predicate<Property>> propSelectors = new ArrayList<>();
    private Predicate<VObject> typePred = (vObj) -> true;

    @Override
    public Selector withProperty(Predicate<Property> propPred) {
        
        this.propSelectors.add(propPred);

        return this;
    }

    @Override
    public Selector withProperty(PropSelector propSel) {
        
        this.propSelectors.add(propSel.asPredicate());

        return this;
    }

    @Override
    public Selector withType(String typeName) {
        this.typePred = (VObject vObj) -> {
            return Objects.equals(typeName, vObj.vmf().reflect().type().getName());
        };

        return this;
    }

    @Override
    public Predicate<VObject> asPredicate() {
        return (VObject vObj) -> {
            if(!typePred.test(vObj)) return false;

            for(Predicate<Property> pred : propSelectors) {
                if(vObj.vmf().reflect().properties().stream().
                   filter(pred).count()==0) {
                    return false;
                }
            }

            return true;
        };
    }

    @Override
    public Collection<VObject> selectFrom(Collection<? extends VObject> collection) {
        return collection.stream().
          filter(asPredicate()).distinct().collect(Collectors.toList());
    }

    @Override
    public Collection<VObject> selectFrom(VObject vObj) {
        return vObj.vmf().content().stream().
        filter(asPredicate()).distinct().collect(Collectors.toList());
    }

    @Override
    public Subscription syncWith(VObject root, Collection<VObject> syncedCollection) {

        Predicate<VObject> pred = asPredicate();

        // sync pre existing objects that match the selector
        syncedCollection.addAll(root.vmf().content().
            stream().filter(pred).distinct().collect(Collectors.toList())
        );

        // register listener that updates the added/removed elements with the 
        // synced collection
        return root.vmf().changes().addListener((c)-> {
            c.propertyChange().ifPresent(pc->{
                if(pc.oldValue() instanceof VObject) {
                    if(pred.test((VObject)pc.newValue())) {
                        syncedCollection.remove((VObject)pc.newValue());
                    }
                }
                if(pc.newValue() instanceof VObject) {
                    if(pred.test((VObject)pc.newValue())) {
                        syncedCollection.add((VObject)pc.newValue());
                    }
                }
            });

            c.listChange().ifPresent((lc)->{
                lc.removed().elements().stream().filter(o->o instanceof VObject).
                  map(o->(VObject)o).forEach(vObj-> {
                    if(pred.test(vObj)) {
                        syncedCollection.remove(vObj);
                    }
                });

                lc.added().elements().stream().filter(o->o instanceof VObject).
                  map(o->(VObject)o).forEach(vObj-> {
                    if(pred.test(vObj)) {
                        syncedCollection.add(vObj);
                    }
                });
            });
        });
    }

}