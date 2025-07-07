// This is a debug version of your phantom mixin with more logging
// Replace your phantom mixin with this temporarily to debug

package com.yourmod.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerWorld.class)
public abstract class MixinServerWorld_NoPhantoms {
    @Inject(method = "spawnEntity", at = @At("HEAD"), cancellable = true)
    private void denyPhantomEntitySpawn(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        ServerWorld serverWorld = (ServerWorld) (Object) this;
        
        // Debug logging
        if (entity.getType() == EntityType.PHANTOM) {
            System.out.println("=== PHANTOM SPAWN DETECTED ===");
            System.out.println("World: " + serverWorld.getRegistryKey().getValue());
            System.out.println("Is Overworld: " + serverWorld.getRegistryKey().getValue().equals(new Identifier("minecraft", "overworld")));
            
            if (serverWorld.getRegistryKey().getValue().equals(new Identifier("minecraft", "overworld"))) {
                System.out.println("BLOCKING PHANTOM SPAWN IN OVERWORLD");
                cir.setReturnValue(false); // Cancel phantom spawn in the overworld
            } else {
                System.out.println("ALLOWING PHANTOM SPAWN IN OTHER DIMENSION");
            }
        }
    }
}
