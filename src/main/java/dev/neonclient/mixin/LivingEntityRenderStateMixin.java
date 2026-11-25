package dev.neonclient.mixin;

import dev.neonclient.NeonRenderState;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(LivingEntityRenderState.class)
public class LivingEntityRenderStateMixin implements NeonRenderState {
    @Unique private boolean neonLocal;
    @Unique private float neonTime;
    @Unique private float neonSpeed;
    @Unique private boolean neonSneaking;

    @Override
    public void neon$set(boolean local, float time, float speed, boolean sneaking) {
        this.neonLocal = local;
        this.neonTime = time;
        this.neonSpeed = speed;
        this.neonSneaking = sneaking;
    }

    @Override public boolean neon$isLocal() { return neonLocal; }
    @Override public float neon$time() { return neonTime; }
    @Override public float neon$speed() { return neonSpeed; }
    @Override public boolean neon$isSneaking() { return neonSneaking; }
}
