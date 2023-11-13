package com.pantae.anythings.util;

import net.minecraft.core.NonNullList;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ModGroupModel {
    private String name;
    private NonNullList<Float> origin;
    private int color;
    private List<Object> children;

    public ModGroupModel() {
        origin = NonNullList.withSize(3, 0F);
        children = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public int getColor() {
        return color;
    }

    public List<Object> getChildren() {
        return children;
    }

    public Object getChild(int index) {
        return children.get(index);
    }

    public NonNullList<Float> getOrigin() {
        return origin;
    }

    public ModGroupModel setChildren(List<Object> children) {
        this.children = children;
        return this;
    }

    public ModGroupModel setChild(Object child, int index) {
        this.children.set(index, child);
        return this;
    }

    public ModGroupModel setColor(int color) {
        this.color = color;
        return this;
    }

    public ModGroupModel setName(String name) {
        this.name = name;
        return this;
    }

    public ModGroupModel setOrigin(List<Float> origin) {
        NonNullList<Float> a = NonNullList.withSize(3, 0F);
        for (int i = 0; i < a.size(); i++) {
            a.set(i, origin.get(i));
        }
        this.origin = a;
        return this;
    }
}
