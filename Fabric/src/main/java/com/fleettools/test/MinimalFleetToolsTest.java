package com.fleettools.test;

import net.fabricmc.api.ModInitializer;

public class MinimalFleetToolsTest implements ModInitializer {
    @Override
    public void onInitialize() {
        System.out.println("[Fleet Tools Test] Minimal initialization - no commands, no mixins");
    }
}
