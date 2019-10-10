package eu.mihosoft.vmfutils.vmfmodel;

import eu.mihosoft.vmf.core.Container;
import eu.mihosoft.vmf.core.Contains;

interface Group extends Node {

    @Contains(opposite="parent")
    Node[] getNodes();
}

interface Node {
    @Container(opposite = "nodes")
    Group getParent();

    String getName();

    double getPressure();

    String[] getTags();
}