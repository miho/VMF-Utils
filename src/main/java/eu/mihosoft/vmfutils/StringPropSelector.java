package eu.mihosoft.vmfutils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import eu.mihosoft.vmf.runtime.core.Property;
import eu.mihosoft.vmf.runtime.core.Type;
import eu.mihosoft.vmf.runtime.core.VObject;

public interface StringPropSelector extends PropSelector {

    public static StringPropSelector selectProp() {
        return new StringPropSelectorImpl();
    }

    public StringPropSelector withName(String name);

    public StringPropSelector withValueThatStartsWith(String value);

    public StringPropSelector withValueThatEndsWith(String value);

    public StringPropSelector withValueThatContains(String value);

    public StringPropSelector withValueThatMatches(String pattern);
}

class StringPropSelectorImpl implements StringPropSelector {



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
    public StringPropSelector withName(String name) {
        
        this.propSel.withName(name);

        return this;
    }

    @Override
    public StringPropSelector withValueThatStartsWith(String value) {
        
        this.predicates.add((p)->{
            if(!(p.get() instanceof String)) {
                return false;
            }

            return ((String)p.get()).startsWith(value);
        });

        return this;
    }

    @Override
    public StringPropSelector withValueThatEndsWith(String value) {
        this.predicates.add((p)->{
            if(!(p.get() instanceof String)) {
                return false;
            }

            return ((String)p.get()).endsWith(value);
        });

        return this;
    }

    @Override
    public StringPropSelector withValueThatContains(String value) {
        this.predicates.add((p)->{
            if(!(p.get() instanceof String)) {
                return false;
            }

            return ((String)p.get()).contains(value);
        });

        return this;
    }

    @Override
    public StringPropSelector withValueThatMatches(String pattern) {
        this.predicates.add((p)->{
            if(!(p.get() instanceof String)) {
                return false;
            }

            return ((String)p.get()).matches(pattern);
        });

        return this;
    }

}

