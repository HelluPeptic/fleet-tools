package com.fleettools.data;

import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class KeepInventoryStorage {

    public static class StoredInventory {
        public final DefaultedList<ItemStack> mainInventory;
        public final DefaultedList<ItemStack> armorInventory;
        public final DefaultedList<ItemStack> offHandInventory;

        public StoredInventory(DefaultedList<ItemStack> main, DefaultedList<ItemStack> armor,
                DefaultedList<ItemStack> offHand) {
            this.mainInventory = main;
            this.armorInventory = armor;
            this.offHandInventory = offHand;
        }
    }

    private static final Map<UUID, StoredInventory> storedInventories = new HashMap<>();

    public static void storePlayerInventory(UUID playerId, DefaultedList<ItemStack> main,
            DefaultedList<ItemStack> armor, DefaultedList<ItemStack> offHand) {
        storedInventories.put(playerId, new StoredInventory(main, armor, offHand));
    }

    public static StoredInventory getStoredInventory(UUID playerId) {
        return storedInventories.get(playerId);
    }

    public static StoredInventory removeStoredInventory(UUID playerId) {
        return storedInventories.remove(playerId);
    }

    public static boolean hasStoredInventory(UUID playerId) {
        return storedInventories.containsKey(playerId);
    }
}