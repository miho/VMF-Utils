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
    public IntegerPropSelector withType(Type type);
    public <T> IntegerPropSelector withValueThatIsEqualTo(T value);
    public <T> IntegerPropSelector withValueThatMatches(Predicate<T> pred);

    public IntegerPropSelector withValueThatIsGreaterThan(Integer value);

    public IntegerPropSelector withValueThatIsGreaterThanOrEqualTo(Integer value);

    public IntegerPropSelector withValueThatIsLessThan(Integer value);

    public IntegerPropSelector withValueThatIsLessThanOrEqualTo(Integer value);
}

class IntegerPropSelectorImpl extends PropSelectorImpl implements IntegerPropSelector {

    @Override
    public IntegerPropSelector withName(String name) {
        super.withName(name);
        return this;
    }

    @Override
    public IntegerPropSelector withType(Type type) {
        super.withType(type);
        return this;
    }


    @Override
    public <T> IntegerPropSelector withValueThatIsEqualTo(T value) {
        super.withValueThatIsEqualTo(value);
        return this;
    }

    @Override
    public <T> IntegerPropSelector withValueThatMatches(Predicate<T> pred){
        super.withValueThatMatches(pred);
        return this;
    }

    @Override
    public IntegerPropSelector withValueThatIsGreaterThan(Integer value) {
        
        getPredicates().add((p)->{
            if(!(p.get() instanceof Integer)) {
                return false;
            }

            return ((Integer)p.get()) > value;
        });

        return this;
    }

    @Override
    public IntegerPropSelector withValueThatIsGreaterThanOrEqualTo(Integer value) {

        getPredicates().add((p)->{
            if(!(p.get() instanceof Integer)) {
                return false;
            }

            return ((Integer)p.get()) >= value;
        });

        return this;
    }

    @Override
    public IntegerPropSelector withValueThatIsLessThan(Integer value) {

        getPredicates().add((p)->{
            if(!(p.get() instanceof Integer)) {
                return false;
            }

            return ((Integer)p.get()) < value;
        });

        return this;
    }

    @Override
    public IntegerPropSelector withValueThatIsLessThanOrEqualTo(Integer value) {

        getPredicates().add((p)->{
            if(!(p.get() instanceof Integer)) {
                return false;
            }

            return ((Integer)p.get()) <= value;
        });

        return this;
    }

}