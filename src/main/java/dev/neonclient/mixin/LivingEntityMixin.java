package dev.neonclient.mixin;

import dev.neonclient.NeonConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    /** Velocidade do swing do jogador local (só visual). */
    @Inject(method = "getHandSwingDuration", at = @At("RETURN"), cancellable = true)
    private void neon$swingSpeed(CallbackInfoReturnable<Integer> cir) {
        if ((Object) this != MinecraftClient.getInstance().player) return;
        float speed = NeonConfig.INSTANCE.swingSpeed;
        if (speed == 1f || speed <= 0f) return;
        cir.setReturnValue(Math.max(1, Math.round(cir.getReturnValueI() / speed)));
    }
}
