package dev.neonclient.cosmetics;

import dev.neonclient.NeonConfig;
import dev.neonclient.cosmetics.Piece.Anim;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/** Registro de todos os cosméticos. Para criar um novo: escreva um método e registre em static {}. */
public final class Cosmetics {
    public static final List<Cosmetic> ALL = new ArrayList<>();

    static {
        // COSTAS
        reg("cape_neon", "Capa Neon", Slot.BACK, Cosmetics::capeNeon);
        reg("cape_chama", "Capa de Chamas", Slot.BACK, Cosmetics::capeChama);
        reg("asas_anjo", "Asas de Anjo", Slot.BACK,
                () -> wings("gray", "white", false, new float[]{16, 15, 13, 10, 6}, 2.4f, 20, 12));
        reg("asas_dragao", "Asas de Dragão", Slot.BACK,
                () -> wings("darkpurple", "purple", false, new float[]{14, 13, 11, 8}, 3f, 25, 14));
        reg("asas_cristal", "Asas de Cristal", Slot.BACK,
                () -> wings("white", "cyan", true, new float[]{13, 15, 13, 10, 7}, 2.4f, 18, 10));
        reg("asas_fogo", "Asas de Fogo", Slot.BACK,
                () -> wings("red", "orange", true, new float[]{15, 14, 12, 9, 6, 3}, 2.2f, 22, 14));
        reg("mochila", "Mochila", Slot.BACK, Cosmetics::mochila);
        // CABEÇA
        reg("halo", "Halo", Slot.HEAD, Cosmetics::halo);
        reg("coroa", "Coroa Real", Slot.HEAD, Cosmetics::coroa);
        reg("chapeu_bruxa", "Chapéu de Bruxa", Slot.HEAD, Cosmetics::chapeuBruxa);
        reg("orelhas_gato", "Orelhas de Gato", Slot.HEAD, Cosmetics::orelhasGato);
        reg("chifres", "Chifres de Diabinho", Slot.HEAD, Cosmetics::chifres);
        // ROSTO
        reg("oculos_neon", "Óculos Neon", Slot.FACE, Cosmetics::oculosNeon);
        reg("bigode", "Bigode", Slot.FACE, Cosmetics::bigode);
        // CAUDA
        reg("rabo_raposa", "Rabo de Raposa", Slot.TAIL, Cosmetics::raboRaposa);
        // AURA
        reg("orbitais_neon", "Orbitais Neon", Slot.AURA, Cosmetics::orbitaisNeon);
        reg("orbitais_fogo", "Orbitais de Fogo", Slot.AURA, Cosmetics::orbitaisFogo);
        // PET
        reg("pet_chibi", "Pet Chibi", Slot.PET, Cosmetics::petChibi);
        reg("pet_slime", "Pet Slime Neon", Slot.PET, Cosmetics::petSlime);
        reg("pet_orbe", "Pet Orbe", Slot.PET, Cosmetics::petOrbe);
    }

    private Cosmetics() {}

    private static void reg(String id, String name, Slot slot, Supplier<List<Piece>> factory) {
        ALL.add(new Cosmetic(id, name, slot, factory));
    }

    public static Cosmetic byId(String id) {
        for (Cosmetic c : ALL) if (c.id.equals(id)) return c;
        return null;
    }

    public static List<Cosmetic> forSlot(Slot slot) {
        List<Cosmetic> out = new ArrayList<>();
        for (Cosmetic c : ALL) if (c.slot == slot) out.add(c);
        return out;
    }

    /** Cosmético equipado no slot (ou null). */
    public static Cosmetic equipped(Slot slot) {
        String id = NeonConfig.INSTANCE.equipped.get(slot.name());
        return id == null ? null : byId(id);
    }

    // ------------------------------------------------------------------ COSTAS

    private static Piece cape(Piece p) {
        return p.pivot(0, 0, 2.2f).anim(Anim.SWAY, 70, 0, 0);
    }

    private static List<Piece> capeNeon() {
        List<Piece> l = new ArrayList<>();
        l.add(cape(Piece.box("deepblue", -5, 0, 0, 10, 17, 1)));
        l.add(cape(Piece.box("cyan", -5.6f, 0, -0.2f, 0.8f, 17.4f, 1.4f).glow()));
        l.add(cape(Piece.box("cyan", 4.8f, 0, -0.2f, 0.8f, 17.4f, 1.4f).glow()));
        l.add(cape(Piece.box("cyan", -5.6f, 16.6f, -0.2f, 11.2f, 0.8f, 1.4f).glow()));
        return l;
    }

    private static List<Piece> capeChama() {
        List<Piece> l = new ArrayList<>();
        l.add(cape(Piece.box("red", -5, 0, 0, 10, 17, 1)));
        l.add(cape(Piece.box("orange", -5.2f, 10, -0.15f, 10.4f, 7.2f, 1.3f).glow()));
        l.add(cape(Piece.box("yellow", -4, 17, 0.1f, 2, 2, 0.8f).glow()));
        l.add(cape(Piece.box("yellow", -1, 17, 0.1f, 2, 3, 0.8f).glow()));
        l.add(cape(Piece.box("yellow", 2, 17, 0.1f, 2, 2, 0.8f).glow()));
        return l;
    }

    private static Piece wing(Piece p, float side, float rest, float flap) {
        return p.pivot(side * 2, 2, 2.5f)
                .pivotRot(0, -side * rest, 0)
                .anim(Anim.WAVE, -side * flap, 0.3f, 0);
    }

    private static List<Piece> wings(String bone, String feather, boolean glowFeather,
                                     float[] lengths, float rowH, float rest, float flap) {
        List<Piece> l = new ArrayList<>();
        for (int s = -1; s <= 1; s += 2) {
            float side = s;
            float topLen = lengths[0] + 2;
            l.add(wing(Piece.box(bone, side > 0 ? 0 : -topLen, 0, 0, topLen, 1.6f, 1.2f), side, rest, flap));
            for (int i = 0; i < lengths.length; i++) {
                float len = lengths[i];
                Piece p = Piece.box(feather, side > 0 ? 0 : -len, 1.6f + i * rowH, 0.2f, len, rowH, 0.8f);
                if (glowFeather) p.glow();
                l.add(wing(p, side, rest, flap));
            }
        }
        return l;
    }

    private static List<Piece> mochila() {
        List<Piece> l = new ArrayList<>();
        l.add(Piece.box("brown", -3.5f, 1, 0, 7, 9, 3.5f).pivot(0, 0, 2.1f));
        l.add(Piece.box("darkbrown", -3.7f, 0.8f, 0, 7.4f, 3.8f, 3.7f).pivot(0, 0, 2.1f));
        l.add(Piece.box("gold", -1, 3.2f, 3.6f, 2, 1.4f, 0.5f).pivot(0, 0, 2.1f));
        l.add(Piece.box("gray", -2.5f, 6, 3.4f, 5, 3, 0.5f).pivot(0, 0, 2.1f));
        return l;
    }

    // ------------------------------------------------------------------ CABEÇA

    private static List<Piece> halo() {
        List<Piece> l = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            double a = i * Math.PI / 4;
            float x = (float) (Math.cos(a) * 5.5);
            float z = (float) (Math.sin(a) * 5.5);
            l.add(Piece.centered("gold", 3.2f, 0.8f, 1.2f).glow().head()
                    .pivot(0, -12.5f, 0).at(x, 0, z)
                    .rot(0, (float) Math.toDegrees(a) + 90f, 0)
                    .anim(Anim.BOB, 0.8f, 0.1f, 0));
        }
        return l;
    }

    private static List<Piece> coroa() {
        List<Piece> l = new ArrayList<>();
        l.add(Piece.box("gold", -4.5f, -9.5f, -4.5f, 9, 2, 1).head());
        l.add(Piece.box("gold", -4.5f, -9.5f, 3.5f, 9, 2, 1).head());
        l.add(Piece.box("gold", -4.5f, -9.5f, -3.5f, 1, 2, 7).head());
        l.add(Piece.box("gold", 3.5f, -9.5f, -3.5f, 1, 2, 7).head());
        float[][] spikes = {{-4.5f, -4.5f}, {-0.5f, -4.5f}, {3.5f, -4.5f}, {-4.5f, 3.5f}, {-0.5f, 3.5f},
                {3.5f, 3.5f}, {-4.5f, -0.5f}, {3.5f, -0.5f}};
        for (float[] s : spikes) l.add(Piece.box("gold", s[0], -12.2f, s[1], 1, 2.7f, 1).head());
        l.add(Piece.box("red", -0.8f, -9.3f, -5.0f, 1.6f, 1.6f, 0.6f).glow().head());
        return l;
    }

    private static List<Piece> chapeuBruxa() {
        List<Piece> l = new ArrayList<>();
        l.add(Piece.box("darkpurple", -7, -9.5f, -7, 14, 1, 14).head());
        l.add(Piece.box("purple", -4.5f, -13.5f, -4.5f, 9, 4, 9).head());
        l.add(Piece.box("gold", -4.8f, -11f, -4.8f, 9.6f, 1.3f, 9.6f).head());
        l.add(Piece.box("purple", -3, -17.5f, -3, 6, 4, 6).head());
        l.add(Piece.box("purple", -1.8f, -20.5f, -1.8f, 3.6f, 3, 3.6f).head());
        l.add(Piece.box("purple", -0.9f, -23, -0.9f, 1.8f, 2.5f, 1.8f).head());
        return l;
    }

    private static List<Piece> orelhasGato() {
        List<Piece> l = new ArrayList<>();
        for (int s = -1; s <= 1; s += 2) {
            float side = s;
            l.add(Piece.centered("pink", 2.6f, 2.6f, 1.4f).head().at(side * 2.8f, -9.8f, -1.2f).rot(0, 0, -side * 12));
            l.add(Piece.centered("pink", 1.6f, 1.8f, 1.0f).head().at(side * 3.3f, -11.7f, -1.2f).rot(0, 0, -side * 12));
            l.add(Piece.centered("lightpink", 1.2f, 1.6f, 0.4f).head().at(side * 2.8f, -10f, -2.0f).rot(0, 0, -side * 12));
        }
        return l;
    }

    private static List<Piece> chifres() {
        List<Piece> l = new ArrayList<>();
        for (int s = -1; s <= 1; s += 2) {
            float side = s;
            l.add(Piece.centered("darkpurple", 2f, 2f, 2f).head().at(side * 3f, -9.5f, -1.5f).rot(0, 0, -side * 15));
            l.add(Piece.centered("red", 1.6f, 2.5f, 1.6f).head().at(side * 3.7f, -11.5f, -1.5f).rot(0, 0, -side * 25));
            l.add(Piece.centered("red", 1f, 2f, 1f).head().at(side * 4.6f, -13.3f, -1.5f).rot(0, 0, -side * 35));
        }
        return l;
    }

    // ------------------------------------------------------------------ ROSTO

    private static List<Piece> oculosNeon() {
        List<Piece> l = new ArrayList<>();
        l.add(Piece.centered("cyan", 3f, 2f, 0.6f).glow().head().at(2.1f, -4.3f, -4.4f));
        l.add(Piece.centered("cyan", 3f, 2f, 0.6f).glow().head().at(-2.1f, -4.3f, -4.4f));
        l.add(Piece.centered("black", 1.2f, 0.6f, 0.6f).head().at(0, -4.7f, -4.4f));
        l.add(Piece.centered("black", 0.5f, 0.6f, 4.4f).head().at(4.3f, -4.7f, -2.3f));
        l.add(Piece.centered("black", 0.5f, 0.6f, 4.4f).head().at(-4.3f, -4.7f, -2.3f));
        return l;
    }

    private static List<Piece> bigode() {
        List<Piece> l = new ArrayList<>();
        l.add(Piece.centered("black", 5.2f, 0.9f, 0.7f).head().at(0, -2.3f, -4.4f));
        l.add(Piece.centered("black", 1.2f, 0.7f, 0.6f).head().at(3.2f, -1.9f, -4.4f).rot(0, 0, 20));
        l.add(Piece.centered("black", 1.2f, 0.7f, 0.6f).head().at(-3.2f, -1.9f, -4.4f).rot(0, 0, -20));
        return l;
    }

    // ------------------------------------------------------------------ CAUDA

    private static List<Piece> raboRaposa() {
        List<Piece> l = new ArrayList<>();
        float[] sizes = {3f, 4f, 4.5f, 4f, 3f};
        for (int i = 0; i < sizes.length; i++) {
            String color = i == sizes.length - 1 ? "white" : "orange";
            l.add(Piece.centered(color, sizes[i], sizes[i], 3.2f)
                    .pivot(0, 11, 2)
                    .at(0, i * 1.0f, 2.0f + i * 2.6f)
                    .anim(Anim.WAVE, 12, 0.15f, 0));
        }
        return l;
    }

    // ------------------------------------------------------------------ AURA

    private static List<Piece> orbitaisNeon() {
        List<Piece> l = new ArrayList<>();
        float[] ys = {-4, 0, 4, 0};
        for (int i = 0; i < 4; i++) {
            l.add(Piece.centered("cyan", 2f, 2f, 2f).glow()
                    .pivot(0, 6, 0).at(10, ys[i], 0)
                    .anim(Anim.ORBIT, 0, 7f, i * 90f));
        }
        return l;
    }

    private static List<Piece> orbitaisFogo() {
        List<Piece> l = new ArrayList<>();
        String[] colors = {"orange", "yellow", "red", "orange", "yellow"};
        for (int i = 0; i < 5; i++) {
            l.add(Piece.centered(colors[i], 2f, 2f, 2f).glow()
                    .pivot(0, 6, 0).at(11, (i % 3 - 1) * 4f, 0)
                    .anim(Anim.ORBIT, 0, -6f, i * 72f));
        }
        return l;
    }

    // ------------------------------------------------------------------ PETS (flutuam ao lado direito)

    private static Piece pet(Piece p) {
        return p.pivot(-14, -3, 0).anim(Anim.BOB, 1.5f, 0.12f, 0);
    }

    private static List<Piece> petChibi() {
        List<Piece> l = new ArrayList<>();
        l.add(pet(Piece.centered("skin", 5, 5, 5).at(0, -4, 0)));
        l.add(pet(Piece.centered("pink", 5.8f, 2.2f, 5.8f).at(0, -6.4f, 0.1f)));
        l.add(pet(Piece.centered("pink", 5.8f, 5, 1.2f).at(0, -3.6f, 2.8f)));
        l.add(pet(Piece.centered("pink", 5.8f, 1.2f, 1f).at(0, -5.4f, -2.6f)));
        l.add(pet(Piece.centered("cyan", 1, 1, 0.3f).glow().at(1.2f, -3.6f, -2.75f)));
        l.add(pet(Piece.centered("cyan", 1, 1, 0.3f).glow().at(-1.2f, -3.6f, -2.75f)));
        l.add(pet(Piece.centered("red", 1.6f, 1.4f, 1.2f).at(2.8f, -7.4f, 0)));
        l.add(pet(Piece.centered("deepblue", 3.6f, 4, 2.4f).at(0, 0.2f, 0)));
        l.add(pet(Piece.centered("skin", 1, 3, 1).at(2.4f, 0.2f, 0)));
        l.add(pet(Piece.centered("skin", 1, 3, 1).at(-2.4f, 0.2f, 0)));
        l.add(pet(Piece.centered("white", 1.3f, 2.6f, 1.3f).at(0.9f, 3.4f, 0)));
        l.add(pet(Piece.centered("white", 1.3f, 2.6f, 1.3f).at(-0.9f, 3.4f, 0)));
        return l;
    }

    private static List<Piece> petSlime() {
        List<Piece> l = new ArrayList<>();
        l.add(pet(Piece.centered("cyan", 6, 5, 6).glow().at(0, 0, 0)));
        l.add(pet(Piece.centered("black", 1, 1.2f, 0.3f).at(1.4f, -0.5f, -3.1f)));
        l.add(pet(Piece.centered("black", 1, 1.2f, 0.3f).at(-1.4f, -0.5f, -3.1f)));
        l.add(pet(Piece.centered("black", 2, 0.5f, 0.3f).at(0, 1.2f, -3.1f)));
        return l;
    }

    private static List<Piece> petOrbe() {
        List<Piece> l = new ArrayList<>();
        l.add(Piece.centered("white", 3, 3, 3).glow().pivot(-14, -3, 0));
        float[] ys = {-2, 0, 2, 0};
        for (int i = 0; i < 4; i++) {
            l.add(Piece.centered("cyan", 1.4f, 1.4f, 1.4f).glow()
                    .pivot(-14, -3, 0).at(5, ys[i], 0)
                    .anim(Anim.ORBIT, 0, 9f, i * 90f));
        }
        return l;
    }
}
