package dev.neonclient.cosmetics;

/** Cada slot aceita um cosmético por vez. */
public enum Slot {
    BACK("Costas"),
    HEAD("Cabeça"),
    FACE("Rosto"),
    TAIL("Cauda"),
    AURA("Aura"),
    PET("Pet");

    public final String label;

    Slot(String label) {
        this.label = label;
    }
}
