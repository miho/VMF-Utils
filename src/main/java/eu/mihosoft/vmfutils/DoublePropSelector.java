package eu.mihosoft.vmfutils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import eu.mihosoft.vmf.runtime.core.Property;
import eu.mihosoft.vmf.runtime.core.Type;
import eu.mihosoft.vmf.runtime.core.VObject;

public interface DoublePropSelector extends PropSelector {

    public static DoublePropSelector selectProp() {
        return new DoublePropSelectorImpl();
    }

    public DoublePropSelector withName(String name);

    public DoublePropSelector withValueThatIsGreaterThan(Double value);

    public DoublePropSelector withValueThatIsGreaterThanOrEqualTo(Double value);

    public DoublePropSelector withValueThatIsLessThan(Double value);

    public DoublePropSelector withValueThatIsLessThanOrEqualTo(Double value);
}

class DoublePropSelectorImpl implements DoublePropSelector {

    private final PropSelector propSel = new PropSelectorImpl();

    private final List<Predicate<Property>> predicates = new ArrayList<>();

    @Override
    public PropSelector withType(Type type) {

        this.propSel.withType(type);

        return this;
    }

    @Override
    public <T> PropSelector withValueThatIsEqualTo(T value) {

        this.propSel.withValueThatIsEqualTo(value);

        return this;
    }

    @Override
    public <T> PropSelector withValueThatMatches(Predicate<T> pred) {

        this.propSel.withValueThatMatches(pred);

        return this;
    }

    @Override
    public Predicate<Property> asPredicate() {

        Predicate<Property> propPred = this.propSel.asPredicate();
        
        return (p)->{
            if(!propPred.test(p)) {
                return false;
            }

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
        return collection.stream().filter(asPredicate()).distinct().collect(Collectors.toList());
    }

    @Override
    public Collection<Property> selectFrom(VObject vObj) {
        return vObj.vmf().reflect().properties().
            stream().filter(asPredicate()).distinct().
            collect(Collectors.toList());
    }

    @Override
    public DoublePropSelector withName(String name) {
        
        this.propSel.withName(name);

        return this;
    }

    @Override
    public DoublePropSelector withValueThatIsGreaterThan(Double value) {
        
        this.predicates.add((p)->{
            if(!(p.get() instanceof Double)) {
                return false;
            }

            return ((Double)p.get()) > value;
        });

        return this;
    }

    @Override
    public DoublePropSelector withValueThatIsGreaterThanOrEqualTo(Double value) {

        this.predicates.add((p)->{
            if(!(p.get() instanceof Double)) {
                return false;
            }

            return ((Double)p.get()) >= value;
        });

        return this;
    }

    @Override
    public DoublePropSelector withValueThatIsLessThan(Double value) {

        this.predicates.add((p)->{
            if(!(p.get() instanceof Double)) {
                return false;
            }

            return ((Double)p.get()) < value;
        });

        return this;
    }

    @Override
    public DoublePropSelector withValueThatIsLessThanOrEqualTo(Double value) {

        this.predicates.add((p)->{
            if(!(p.get() instanceof Double)) {
                return false;
            }

            return ((Double)p.get()) <= value;
        });

        return this;
    }

}