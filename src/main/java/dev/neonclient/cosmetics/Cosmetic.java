package dev.neonclient.cosmetics;

import java.util.List;
import java.util.function.Supplier;

public final class Cosmetic {
    public final String id;
    public final String name;
    public final Slot slot;
    private final Supplier<List<Piece>> factory;
    private List<Piece> pieces;

    Cosmetic(String id, String name, Slot slot, Supplier<List<Piece>> factory) {
        this.id = id;
        this.name = name;
        this.slot = slot;
        this.factory = factory;
    }

    /** As peças são criadas na primeira vez que o cosmético é desenhado. */
    public List<Piece> pieces() {
        if (pieces == null) pieces = factory.get();
        return pieces;
    }
}
