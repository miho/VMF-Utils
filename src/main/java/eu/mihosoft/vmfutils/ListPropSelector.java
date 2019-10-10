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

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

import eu.mihosoft.vmf.runtime.core.Property;
import eu.mihosoft.vmf.runtime.core.Type;
import eu.mihosoft.vmf.runtime.core.VObject;

public interface ListPropSelector extends PropSelector {

    public static ListPropSelector selectProp() {
        return null;
    }

    public ListPropSelector withName(String name);
    public ListPropSelector withType(Type type);
    public <T> ListPropSelector withValueThatIsEqualTo(T value);
    public <T> ListPropSelector withValueThatMatches(Predicate<T> pred);

    public <T> ListPropSelector whichContains(T... elements);

    public <T> ListPropSelector whichContains(Collection<T> elements);

    public <T> ListPropSelector whichOnlyContains(T... elements);

    public <T> ListPropSelector whichOnlyContains(Collection<T> elements);

    public <T> ListPropSelector withAllElementsComplyTo(PropSelector propSel);

    public <T> ListPropSelector withAllElementsComplyTo(Predicate<T> selPred);

    public <T> ListPropSelector withAtLeastOnElementCompliesTo(Predicate<T> selPred);

    public <T> ListPropSelector withAtLeastOnElementCompliesTo(PropSelector propSel);

}

class ListPropSelectorImpl extends PropSelectorImpl implements ListPropSelector {

    @Override
    public ListPropSelector withName(String name) {
        super.withName(name);
        return this;
    }

    @Override
    public ListPropSelector withType(Type type) {
        super.withType(type);
        return this;
    }

    @Override
    public <T> ListPropSelector withValueThatIsEqualTo(T value) {
        super.withValueThatIsEqualTo(value);
        return this;
    }

    @Override
    public <T> ListPropSelector withValueThatMatches(Predicate<T> pred) {
        super.withValueThatMatches(pred);
        return this;
    }

    @Override
    public <T> ListPropSelector whichContains(T... elements) {

        getPredicates().add((p) -> {
            if(!(p.get() instanceof List)) {
                return false;
            }

            List<?> listP = (List<?>)p.get();

            return listP.containsAll(Arrays.asList(elements));      
        });

        return this;
    }

    @Override
    public <T> ListPropSelector whichContains(Collection<T> elements) {
        getPredicates().add((p) -> {
            if(!(p.get() instanceof List)) {
                return false;
            }

            List<?> listP = (List<?>)p.get();

            return listP.containsAll(elements);      
        });

        return this;
    }

    @Override
    public <T> ListPropSelector whichOnlyContains(T... elements) {
        getPredicates().add((p) -> {
            if(!(p.get() instanceof List)) {
                return false;
            }

            List<?> listP = (List<?>)p.get();

            if(listP.size() != elements.length) {
                return false;
            }

            return listP.containsAll(Arrays.asList(elements));      
        });

        return this;
    }

    @Override
    public <T> ListPropSelector whichOnlyContains(Collection<T> elements) {
        getPredicates().add((p) -> {
            if(!(p.get() instanceof List)) {
                return false;
            }

            List<?> listP = (List<?>)p.get();

            if(listP.size() != elements.size()) {
                return false;
            }

            return listP.containsAll(elements);      
        });

        return this;
    }

    @Override
    public <T> ListPropSelector withAllElementsComplyTo(PropSelector propSel) {
        return withAllElementsComplyTo(propSel.asPredicate());
    }

    @Override
    public <T> ListPropSelector withAllElementsComplyTo(Predicate<T> selPred) {
        getPredicates().add((p)-> {

            if(!(p.get() instanceof List)) {
                return false;
            }

            List<?> listP = (List<?>)p.get();

            for(Object element : listP) {
                try {
                    // @SuppressWarnings("Unchecked")
                    if(!selPred.test((T)element)) {
                        return false;
                    }
                } catch(ClassCastException ex) {
                    return false;
                }
            }

            return true;
        });

        return this;
    }

    @Override
    public <T> ListPropSelector withAtLeastOnElementCompliesTo(Predicate<T> selPred) {
        getPredicates().add((p)-> {

            if(!(p.get() instanceof List)) {
                return false;
            }

            List<?> listP = (List<?>)p.get();

            for(Object element : listP) {
                try {
                    if(selPred.test((T)element)) {
                        return true;
                    }
                } catch(ClassCastException ex) {
                    // 
                }
            }

            return false;
        });

        return this;
    }

    @Override
    public <T> ListPropSelector withAtLeastOnElementCompliesTo(PropSelector propSel) {
        return withAtLeastOnElementCompliesTo(propSel);
    }

}