package com.pantae.anythings.abstracts;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.HashMap;
import java.util.function.Supplier;

public abstract  class ModRegistries<T, E> {
    protected final DeferredRegister<T> REGISTER;
    public final HashMap<E, RegistryObject<T>> MAP = new HashMap<>();

    public ModRegistries(DeferredRegister<T> register) {
        this.REGISTER = register;
        initializeRegistries();
    }

    protected abstract void initializeRegistries();

    public RegistryObject<T> getRegistry(E enumValue) {
        return MAP.get(enumValue);
    }

    public void register(IEventBus _eventBus) {
        this.REGISTER.register(_eventBus);
    }

    public <I extends T> RegistryObject<I> register(final String name, final Supplier<? extends I> sup) {
        return this.REGISTER.register(name, sup);
    }
}
