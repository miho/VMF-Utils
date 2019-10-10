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
        return null;
    }

    public static StringPropSelector selectStringProp() {
        return null;
    }

    public static DoublePropSelector selectDoubleProp() {
        return null;
    }

    public static IntegerPropSelector selectIntegerProp() {
        return null;
    }

    public static ListPropSelector selectListProp() {
        return null;
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