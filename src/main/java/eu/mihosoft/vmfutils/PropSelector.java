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

    private final List<Predicate<Property>> predicates = new ArrayList();

    @Override
    public PropSelector withName(String name) {
        predicates.add((p)->name.equals(p.getName()));
        return this;
    }

    @Override
    public PropSelector withType(Type type) {
        predicates.add((p)->type.equals(p.getType()));
        return this;
    }

    @Override
    public <T> PropSelector withValueThatIsEqualTo(T value) {
        predicates.add((p)->Objects.equals(p.get(), value));
        return this;
    }

    @Override
    public <T> PropSelector withValueThatMatches(Predicate<T> pred) {
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
    public Predicate<Property> asPredicate() {
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
    public Collection<Property> selectFrom(Collection<? extends Property> collection) {
        return collection.stream().filter(asPredicate()).distinct().
            collect(Collectors.toList());
    }

    @Override
    public Collection<Property> selectFrom(VObject vObj) {
        return vObj.vmf().reflect().properties().stream().filter(asPredicate()).
          collect(Collectors.toList());
    }

}