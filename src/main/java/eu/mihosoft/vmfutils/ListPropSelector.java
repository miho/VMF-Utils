package eu.mihosoft.vmfutils;

import java.util.Collection;
import java.util.function.Predicate;

import eu.mihosoft.vmf.runtime.core.VObject;

public interface ListPropSelector extends PropSelector {

    public static ListPropSelector selectProp() {
        return null;
    }

    public <T> ListPropSelector whichContains(T... elements);
    public <T> ListPropSelector whichContains(Collection<T> elements);
    public <T> ListPropSelector whichOnlyContains(T... elements);
    public <T> ListPropSelector whichOnlyContains(Collection<T> elements);
    public <T> ListPropSelector withAllElementsComplyTo(PropSelector propSel);
    public <T> ListPropSelector withAllElementsComplyTo(Predicate<VObject> selPred);
    public <T> ListPropSelector withAtLeastOnElementCompliesTo(Predicate<VObject> selPred);
    public <T> ListPropSelector withAtLeastOnElementCompliesTo(PropSelector propSel);

}