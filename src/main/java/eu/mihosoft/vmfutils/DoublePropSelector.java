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

public interface DoublePropSelector extends PropSelector {

    public static DoublePropSelector selectProp() {
        return new DoublePropSelectorImpl();
    }

    public DoublePropSelector withName(String name);
    public DoublePropSelector withType(Type type);
    public <T> DoublePropSelector withValueThatIsEqualTo(T value);
    public <T> DoublePropSelector withValueThatMatches(Predicate<T> pred);

    public DoublePropSelector withValueThatIsGreaterThan(Double value);

    public DoublePropSelector withValueThatIsGreaterThanOrEqualTo(Double value);

    public DoublePropSelector withValueThatIsLessThan(Double value);

    public DoublePropSelector withValueThatIsLessThanOrEqualTo(Double value);
}

class DoublePropSelectorImpl extends PropSelectorImpl implements DoublePropSelector {

    @Override
    public DoublePropSelectorImpl withName(String name) {
        super.withName(name);
        return this;
    }

    @Override
    public DoublePropSelectorImpl withType(Type type) {
        super.withType(type);
        return this;
    }

    @Override
    public <T> DoublePropSelectorImpl withValueThatIsEqualTo(T value) {
        super.withValueThatIsEqualTo(value);
        return this;
    }

    @Override
    public <T> DoublePropSelectorImpl withValueThatMatches(Predicate<T> pred){
        super.withValueThatMatches(pred);
        return this;
    }


    @Override
    public DoublePropSelector withValueThatIsGreaterThan(Double value) {
        
        getPredicates().add((p)->{
            if(!(p.get() instanceof Double)) {
                return false;
            }

            return ((Double)p.get()) > value;
        });

        return this;
    }

    @Override
    public DoublePropSelector withValueThatIsGreaterThanOrEqualTo(Double value) {

        getPredicates().add((p)->{

            if(!(p.get() instanceof Double)) {
                return false;
            }

            return ((Double)p.get()) >= value;
        });

        return this;
    }

    @Override
    public DoublePropSelector withValueThatIsLessThan(Double value) {

        getPredicates().add((p)->{

            if(!(p.get() instanceof Double)) {
                return false;
            }

            return ((Double)p.get()) < value;
        });

        return this;
    }

    @Override
    public DoublePropSelector withValueThatIsLessThanOrEqualTo(Double value) {

        getPredicates().add((p)->{
            if(!(p.get() instanceof Double)) {
                return false;
            }

            return ((Double)p.get()) <= value;
        });

        return this;
    }

}