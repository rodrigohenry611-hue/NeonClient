package dev.neonclient.cosmetics;

import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.util.Identifier;

/**
 * Uma peça (caixa colorida) de um cosmético.
 * Unidades em pixels do modelo (16 px = 1 bloco). Eixo Y do modelo aponta PARA BAIXO,
 * costas do jogador = +Z, esquerda do jogador = +X. Origem = pescoço.
 *
 * Ordem das transformações: âncora -> pivô -> rotação do pivô + animação -> offset -> rotação própria.
 */
public class Piece {
    public enum Anchor { BODY, HEAD }

    public enum Anim {
        NONE,   // parado
        WAVE,   // balança em Y (asas, rabo)
        SWAY,   // balança em X conforme a velocidade (capa)
        BOB,    // sobe e desce
        ORBIT   // gira em Y em volta do pivô
    }

    final ModelPart part;
    final Identifier texture;
    boolean glow = false;
    Anchor anchor = Anchor.BODY;
    float px, py, pz;
    float prx, pry, prz;
    float ox, oy, oz;
    float rx, ry, rz;
    Anim anim = Anim.NONE;
    float amp, freq, phase;

    private Piece(String color, float x, float y, float z, float w, float h, float d) {
        ModelData data = new ModelData();
        data.getRoot().addChild("p",
                ModelPartBuilder.create().uv(0, 0).cuboid(x, y, z, w, h, d),
                ModelTransform.NONE);
        this.part = TexturedModelData.of(data, 64, 64).createModel().getChild("p");
        this.texture = Identifier.of("neonclient", "textures/cosmetic/" + color + ".png");
    }

    /** Caixa com canto em (x,y,z) relativo ao pivô. */
    public static Piece box(String color, float x, float y, float z, float w, float h, float d) {
        return new Piece(color, x, y, z, w, h, d);
    }

    /** Caixa centrada na origem (use .at() para posicionar). */
    public static Piece centered(String color, float w, float h, float d) {
        return new Piece(color, -w / 2f, -h / 2f, -d / 2f, w, h, d);
    }

    public Piece glow() { this.glow = true; return this; }
    public Piece head() { this.anchor = Anchor.HEAD; return this; }
    public Piece pivot(float x, float y, float z) { px = x; py = y; pz = z; return this; }
    public Piece pivotRot(float x, float y, float z) { prx = x; pry = y; prz = z; return this; }
    public Piece at(float x, float y, float z) { ox = x; oy = y; oz = z; return this; }
    public Piece rot(float x, float y, float z) { rx = x; ry = y; rz = z; return this; }

    public Piece anim(Anim a, float amp, float freq, float phase) {
        this.anim = a; this.amp = amp; this.freq = freq; this.phase = phase;
        return this;
    }
}
