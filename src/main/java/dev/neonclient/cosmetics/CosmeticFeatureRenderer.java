package dev.neonclient.cosmetics;

import dev.neonclient.NeonConfig;
import dev.neonclient.NeonRenderState;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.RotationAxis;

/** Desenha os cosméticos equipados no jogador local (visíveis na 3ª pessoa / F5). */
public class CosmeticFeatureRenderer extends FeatureRenderer<PlayerEntityRenderState, PlayerEntityModel> {

    public CosmeticFeatureRenderer(FeatureRendererContext<PlayerEntityRenderState, PlayerEntityModel> context) {
        super(context);
    }

    @Override
    public void render(MatrixStack matrices, OrderedRenderCommandQueue queue, int light,
                       PlayerEntityRenderState state, float limbAngle, float limbDistance) {
        if (!NeonConfig.INSTANCE.cosmeticsEnabled) return;

        NeonRenderState ns = (NeonRenderState) state;
        if (!ns.neon$isLocal()) return; // sem servidor próprio, só você vê seus cosméticos

        float time = ns.neon$time();
        float speed = ns.neon$speed();

        for (Slot slot : Slot.values()) {
            Cosmetic cosmetic = Cosmetics.equipped(slot);
            if (cosmetic == null) continue;
            for (Piece piece : cosmetic.pieces()) {
                renderPiece(piece, matrices, queue, light, state, time, speed);
            }
        }
    }

    private void renderPiece(Piece p, MatrixStack matrices, OrderedRenderCommandQueue queue, int light,
                             PlayerEntityRenderState state, float time, float speed) {
        matrices.push();

        if (p.anchor == Piece.Anchor.HEAD) {
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(state.relativeHeadYaw));
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(state.pitch));
        }

        matrices.translate(p.px / 16f, p.py / 16f, p.pz / 16f);

        float rx = p.prx, ry = p.pry, rz = p.prz, dy = 0f;
        switch (p.anim) {
            case WAVE -> ry += (float) Math.sin(time * p.freq + p.phase) * p.amp;
            case SWAY -> rx += Math.min(55f, speed * p.amp) + (float) Math.sin(time * 0.12f) * 1.5f;
            case BOB -> dy = (float) Math.sin(time * p.freq + p.phase) * p.amp;
            case ORBIT -> ry += time * p.freq + p.phase;
            default -> { }
        }
        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(rz));
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(ry));
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(rx));

        matrices.translate(p.ox / 16f, (p.oy + dy) / 16f, p.oz / 16f);

        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(p.rz));
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(p.ry));
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(p.rx));

        int pieceLight = p.glow ? LightmapTextureManager.MAX_LIGHT_COORDINATE : light;
        queue.submitModelPart(p.part, matrices, RenderLayer.getEntityCutoutNoCull(p.texture),
                pieceLight, OverlayTexture.DEFAULT_UV, null);

        matrices.pop();
    }
}
