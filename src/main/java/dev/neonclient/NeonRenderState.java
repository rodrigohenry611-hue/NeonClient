package dev.neonclient;

/** Dados extras anexados ao render state dos personagens (via mixin). */
public interface NeonRenderState {
    void neon$set(boolean local, float time, float speed, boolean sneaking);
    boolean neon$isLocal();
    float neon$time();
    float neon$speed();
    boolean neon$isSneaking();
}
