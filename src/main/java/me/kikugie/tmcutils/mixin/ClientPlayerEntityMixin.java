package me.kikugie.tmcutils.mixin;

import me.kikugie.tmcutils.config.Configs;
import me.kikugie.tmcutils.features.WorldEditSync;
import me.kikugie.tmcutils.networking.WorldEditNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin {
    @Inject(method = "setClientPermissionLevel", at = @At("RETURN"))
    private void turnOffPerf(int permissionLevel, CallbackInfo ci) {
        if (permissionLevel >= 0 && Configs.FeatureConfigs.AUTO_PERF_OFF.getBooleanValue() && WorldEditNetworkHandler.isWorldEditConnected()) {
            WorldEditSync.turnOffPerf();
        }
    }
}
