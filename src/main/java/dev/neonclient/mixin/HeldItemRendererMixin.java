package dev.neonclient.mixin;

import dev.neonclient.NeonConfig;
import dev.neonclient.SwingStyle;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Arm;
import net.minecraft.util.math.RotationAxis;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HeldItemRenderer.class)
public class HeldItemRendererMixin {

    /** View model: soma posição / rotação / escala depois da posição base do item. */
    @Inject(method = "applyEquipOffset", at = @At("TAIL"))
    private void neon$viewModel(MatrixStack matrices, Arm arm, float equipProgress, CallbackInfo ci) {
        NeonConfig c = NeonConfig.INSTANCE;
        if (!c.viewModelEnabled) return;

        float side = arm == Arm.RIGHT ? 1f : -1f; // espelha para a mão esquerda
        matrices.translate(c.vmX * side, c.vmY, c.vmZ);
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(c.vmRotX));
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(c.vmRotY * side));
        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(c.vmRotZ * side));
        matrices.scale(c.vmScale, c.vmScale, c.vmScale);
    }

    /** Swing: movimento extra por cima da animação do jogo (vale para bater, colocar bloco, usar item). */
    @Inject(method = "applySwingOffset", at = @At("TAIL"))
    private void neon$swingStyle(MatrixStack matrices, Arm arm, float swingProgress, CallbackInfo ci) {
        SwingStyle style = SwingStyle.of(NeonConfig.INSTANCE.swingStyle);
        if (style == SwingStyle.VANILLA || swingProgress <= 0f) return;

        float side = arm == Arm.RIGHT ? 1f : -1f;
        float s = (float) Math.sin(swingProgress * Math.PI); // 0 -> 1 -> 0

        switch (style) {
            case SLASH -> {
                matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(side * -s * 35f));
                matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(side * s * 15f));
            }
            case POKE -> matrices.translate(0f, 0f, -s * 0.4f);
            case CHOP -> matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-s * 40f));
            case SPIN -> matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(side * swingProgress * 360f));
            default -> { }
        }
    }
}
