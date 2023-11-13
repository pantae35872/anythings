package com.pantae.anythings.enums;

public enum ModSoundEnums {
    ;
    private int weight;
    private double volume;

    ModSoundEnums(int weight, double volume) {
        this.weight = weight;
        this.volume = volume;
    }

    public int getWeight() {
        return this.weight;
    }

    public double getVolume() {
        return this.volume;
    }
}
