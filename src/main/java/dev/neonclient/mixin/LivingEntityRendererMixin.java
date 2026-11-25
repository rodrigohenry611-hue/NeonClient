package dev.neonclient.mixin;

import dev.neonclient.NeonRenderState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererMixin {

    @Inject(
            method = "updateRenderState(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;F)V",
            at = @At("TAIL")
    )
    private void neon$updateState(LivingEntity entity, LivingEntityRenderState state,
                                  float tickProgress, CallbackInfo ci) {
        if (state instanceof NeonRenderState neonState) {
            Vec3d velocity = entity.getVelocity();
            neonState.neon$set(
                    entity == MinecraftClient.getInstance().player,
                    entity.age + tickProgress,
                    (float) velocity.horizontalLength(),
                    entity.isSneaking()
            );
        }
    }
}
