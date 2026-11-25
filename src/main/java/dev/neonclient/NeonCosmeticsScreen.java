package dev.neonclient;

import dev.neonclient.cosmetics.Cosmetic;
import dev.neonclient.cosmetics.Cosmetics;
import dev.neonclient.cosmetics.Slot;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;

/** Escolha de cosméticos: um botão por slot (clique para trocar), e um X para tirar. */
public class NeonCosmeticsScreen extends NeonScreen {

    public NeonCosmeticsScreen(Screen parent) {
        super("Cosméticos", parent);
    }

    @Override
    protected int contentHeight() {
        return Slot.values().length * 24;
    }

    @Override
    protected void addContent(int left, int top, int width) {
        int y = top;
        for (Slot slot : Slot.values()) {
            ButtonWidget cycle = ButtonWidget.builder(label(slot), b -> {
                cycle(slot);
                b.setMessage(label(slot));
            }).dimensions(left, y, width - 24, 20).build();
            addDrawableChild(cycle);

            addDrawableChild(ButtonWidget.builder(Text.literal("X"), b -> {
                NeonConfig.INSTANCE.equipped.remove(slot.name());
                NeonConfig.save();
                cycle.setMessage(label(slot));
            }).dimensions(left + width - 20, y, 20, 20).build());
            y += 24;
        }
    }

    private Text label(Slot slot) {
        Cosmetic current = Cosmetics.equipped(slot);
        String name = current == null ? "Nenhum" : current.name;
        return Text.literal(slot.label + ": " + name)
                .formatted(current == null ? Formatting.GRAY : Formatting.AQUA);
    }

    private void cycle(Slot slot) {
        List<Cosmetic> options = Cosmetics.forSlot(slot);
        Cosmetic current = Cosmetics.equipped(slot);
        int index = current == null ? -1 : options.indexOf(current);
        int next = index + 1;
        if (next >= options.size()) {
            NeonConfig.INSTANCE.equipped.remove(slot.name());
        } else {
            NeonConfig.INSTANCE.equipped.put(slot.name(), options.get(next).id);
        }
        NeonConfig.save();
    }
}
