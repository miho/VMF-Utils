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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import eu.mihosoft.vmf.runtime.core.Property;
import eu.mihosoft.vmf.runtime.core.Type;
import eu.mihosoft.vmf.runtime.core.VObject;

public interface PropSelector {

    public static PropSelector selectProp() {
        return new PropSelectorImpl();
    }

    public PropSelector withName(String name);
    public PropSelector withType(Type type);
    public <T> PropSelector withValueThatIsEqualTo(T value);
    public <T> PropSelector withValueThatMatches(Predicate<T> pred);

    public Predicate<Property> asPredicate();

    public Collection<Property> selectFrom(Collection<? extends Property> collection);
    public Collection<Property> selectFrom(VObject vObj);
    
}

class PropSelectorImpl implements PropSelector {

    private final List<Predicate<Property>> predicates = new ArrayList<>();

    protected List<Predicate<Property>> getPredicates() {
        return this.predicates;
    }

    @Override
    public PropSelector withName(String name) {
        return _withName(name);
    }

    @Override
    public PropSelector withType(Type type) {
        return _withType(type);
    }

    @Override
    public <T> PropSelector withValueThatIsEqualTo(T value) {
        return _withValueThatIsEqualTo(value);
    }

    @Override
    public <T> PropSelector withValueThatMatches(Predicate<T> pred) {
        return _withValueThatMatches(pred);
    }

    final PropSelector _withName(String name) {
        predicates.add((p)->name.equals(p.getName()));
        return this;
    }


    final PropSelector _withType(Type type) {
        predicates.add((p)->type.equals(p.getType()));
        return this;
    }


    final <T> PropSelector _withValueThatIsEqualTo(T value) {
        predicates.add((p)->Objects.equals(p.get(), value));
        return this;
    }


    final <T> PropSelector _withValueThatMatches(Predicate<T> pred) {
        predicates.add((p)->{
            try {
                return pred.test((T)p.get());
            } catch(ClassCastException ex) {
                // didn't work, we return false
            }      
            
            return false;
        });
        return this;
    }

    @Override
    public final Predicate<Property> asPredicate() {
        return (p)->{
            for(Predicate<Property> pred : predicates) {
                if(!pred.test(p)) {
                    return false;
                }
            }

            return true;
        };
    }

    @Override
    public final Collection<Property> selectFrom(Collection<? extends Property> collection) {
        return collection.stream().filter(asPredicate()).distinct().
            collect(Collectors.toList());
    }

    @Override
    public final Collection<Property> selectFrom(VObject vObj) {
        return vObj.vmf().reflect().properties().stream().filter(asPredicate()).
          collect(Collectors.toList());
    }

}