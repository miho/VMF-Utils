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
import java.util.List;

import org.junit.Test;

import eu.mihosoft.vmf.runtime.core.VObject;

public class SelectorTest {
    @Test public void objectSelectionTest1(){

        // smart list
        List<VObject> valvesWithHighPressure = new ArrayList<>();

        Group root = Group.newInstance();

        Selector.selectObject().
            withProperty(Selector.selectStringProp().withName("name").
                withValueThatStartsWith("valve")).
            withProperty(Selector.selectDoubleProp().withName("pressure").
                withValueThatIsGreaterThan(100.0)).
            withProperty(Selector.selectListProp().withName("tags").whichContains("print-head")).    
        syncWith(root, valvesWithHighPressure);

        {
            Node node = Node.newBuilder().withName("valve-01").
                withPressure(89.0).withTags("print-head").build();
            root.getNodes().add(node);
        }
        {
            Node node = Node.newBuilder().withName("valve-02").
                withPressure(189.0).withTags("print-head").build();
            root.getNodes().add(node);
        }
        {
            Node node = Node.newBuilder().withName("valve-03").
                withPressure(129.0).build();
            root.getNodes().add(node);
        }
        {
            Node node = Node.newBuilder().withName("tank").
                withPressure(161.8).withTags("print-head").build();
            root.getNodes().add(node);
        }
        {
            Node node = Node.newBuilder().withName("tank").
                withPressure(192.1).withTags("tank").build();
            root.getNodes().add(node);
        }

        valvesWithHighPressure.forEach(valve->System.out.println(valve));

    } 
}

