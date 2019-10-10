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

public interface StringPropSelector extends PropSelector {

    public static StringPropSelector selectProp() {
        return new StringPropSelectorImpl();
    }

    public StringPropSelector withName(String name);
    public StringPropSelector withType(Type type);
    public <T> StringPropSelector withValueThatIsEqualTo(T value);
    public <T> StringPropSelector withValueThatMatches(Predicate<T> pred);

    public StringPropSelector withValueThatStartsWith(String value);

    public StringPropSelector withValueThatEndsWith(String value);

    public StringPropSelector withValueThatContains(String value);

    public StringPropSelector withValueThatMatches(String pattern);
}

class StringPropSelectorImpl extends PropSelectorImpl implements StringPropSelector {

    @Override
    public StringPropSelector withName(String name) {
        super.withName(name);
        return this;
    }

    @Override
    public StringPropSelector withType(Type type) {
        super.withType(type);
        return this;
    }


    @Override
    public <T> StringPropSelector withValueThatIsEqualTo(T value) {
        super.withValueThatIsEqualTo(value);
        return this;
    }

    @Override
    public <T> StringPropSelector withValueThatMatches(Predicate<T> pred){
        super.withValueThatMatches(pred);
        return this;
    }

    @Override
    public StringPropSelector withValueThatStartsWith(String value) {
        
        getPredicates().add((p)->{
            if(!(p.get() instanceof String)) {
                return false;
            }

            return ((String)p.get()).startsWith(value);
        });

        return this;
    }

    @Override
    public StringPropSelector withValueThatEndsWith(String value) {
        getPredicates().add((p)->{
            if(!(p.get() instanceof String)) {
                return false;
            }

            return ((String)p.get()).endsWith(value);
        });

        return this;
    }

    @Override
    public StringPropSelector withValueThatContains(String value) {
        getPredicates().add((p)->{
            if(!(p.get() instanceof String)) {
                return false;
            }

            return ((String)p.get()).contains(value);
        });

        return this;
    }

    @Override
    public StringPropSelector withValueThatMatches(String pattern) {
        getPredicates().add((p)->{
            if(!(p.get() instanceof String)) {
                return false;
            }

            return ((String)p.get()).matches(pattern);
        });

        return this;
    }

}

