package dev.neonclient;

/** Estilos extras de swing (bater / colocar bloco / usar item). */
public enum SwingStyle {
    VANILLA("Vanilla"),
    SLASH("Corte"),
    POKE("Estocada"),
    CHOP("Machado"),
    SPIN("Giro");

    public final String label;

    SwingStyle(String label) {
        this.label = label;
    }

    public SwingStyle next() {
        SwingStyle[] all = values();
        return all[(ordinal() + 1) % all.length];
    }

    public static SwingStyle of(String name) {
        try {
            return valueOf(name);
        } catch (IllegalArgumentException | NullPointerException e) {
            return VANILLA;
        }
    }
}
