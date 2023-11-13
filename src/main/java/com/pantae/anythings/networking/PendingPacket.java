package com.pantae.anythings.networking;

import com.pantae.anythings.enums.ModPacketS2CEnum;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PendingPacket {
    public static Map<ModPacketS2CEnum, List<? super ModPacket>> PendingS2C = new ConcurrentHashMap<>();

    public static<O extends ModPacket> void addS2C(ModPacketS2CEnum key, O value) {
        PendingS2C.computeIfAbsent(key, k -> new ArrayList<>()).add(value);
    }

    @SuppressWarnings("unchecked")
    public static <E extends ModPacket> E getS2C(ModPacketS2CEnum key) {
        List<? super ModPacket> values = PendingS2C.get(key);
        if (values != null && !values.isEmpty()) {
            Object specificValue = values.remove(0);
            if (values.isEmpty()) {
                PendingS2C.remove(key);
            }
            return (E) specificValue;
        }
        return null;
    }
}
