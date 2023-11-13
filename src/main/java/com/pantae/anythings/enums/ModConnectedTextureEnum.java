package com.pantae.anythings.enums;

import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

public enum ModConnectedTextureEnum implements StringRepresentable {
    SIDE("side"),
    NONE("none");
    private final String name;

    ModConnectedTextureEnum(String name) {
        this.name = name;
    }

    @Override
    public @NotNull String getSerializedName() {
        return this.name;
    }
}
