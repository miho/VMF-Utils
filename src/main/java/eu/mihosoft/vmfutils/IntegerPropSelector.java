package eu.mihosoft.vmfutils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import eu.mihosoft.vmf.runtime.core.Property;
import eu.mihosoft.vmf.runtime.core.Type;
import eu.mihosoft.vmf.runtime.core.VObject;

public interface IntegerPropSelector extends PropSelector {

    public static IntegerPropSelector selectProp() {
        return new IntegerPropSelectorImpl();
    }

    public IntegerPropSelector withName(String name);

    public IntegerPropSelector withValueThatIsGreaterThan(Integer value);

    public IntegerPropSelector withValueThatIsGreaterThanOrEqualTo(Integer value);

    public IntegerPropSelector withValueThatIsLessThan(Integer value);

    public IntegerPropSelector withValueThatIsLessThanOrEqualTo(Integer value);
}

class IntegerPropSelectorImpl implements IntegerPropSelector {

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
    public IntegerPropSelector withName(String name) {
        
        this.propSel.withName(name);

        return this;
    }

    @Override
    public IntegerPropSelector withValueThatIsGreaterThan(Integer value) {
        
        this.predicates.add((p)->{
            if(!(p.get() instanceof Integer)) {
                return false;
            }

            return ((Integer)p.get()) > value;
        });

        return this;
    }

    @Override
    public IntegerPropSelector withValueThatIsGreaterThanOrEqualTo(Integer value) {

        this.predicates.add((p)->{
            if(!(p.get() instanceof Integer)) {
                return false;
            }

            return ((Integer)p.get()) >= value;
        });

        return this;
    }

    @Override
    public IntegerPropSelector withValueThatIsLessThan(Integer value) {

        this.predicates.add((p)->{
            if(!(p.get() instanceof Integer)) {
                return false;
            }

            return ((Integer)p.get()) < value;
        });

        return this;
    }

    @Override
    public IntegerPropSelector withValueThatIsLessThanOrEqualTo(Integer value) {

        this.predicates.add((p)->{
            if(!(p.get() instanceof Integer)) {
                return false;
            }

            return ((Integer)p.get()) <= value;
        });

        return this;
    }

}