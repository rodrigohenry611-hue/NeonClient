package dev.neonclient;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

/** Menu principal do client. */
public class NeonMenuScreen extends NeonScreen {

    private record Option(String label, BooleanSupplier get, Consumer<Boolean> set) {}

    private final List<Option> options = List.of(
            new Option("HUD de info (ping/coords)",
                    () -> NeonConfig.INSTANCE.infoHud, v -> NeonConfig.INSTANCE.infoHud = v),
            new Option("Efeitos de poção",
                    () -> NeonConfig.INSTANCE.potionHud, v -> NeonConfig.INSTANCE.potionHud = v),
            new Option("Durabilidade da armadura",
                    () -> NeonConfig.INSTANCE.armorHud, v -> NeonConfig.INSTANCE.armorHud = v),
            new Option("Hotbar customizada",
                    () -> NeonConfig.INSTANCE.customHotbar, v -> NeonConfig.INSTANCE.customHotbar = v),
            new Option("Tag do client no chat",
                    () -> NeonConfig.INSTANCE.chatTag, v -> NeonConfig.INSTANCE.chatTag = v),
            new Option("Mostrar cosméticos",
                    () -> NeonConfig.INSTANCE.cosmeticsEnabled, v -> NeonConfig.INSTANCE.cosmeticsEnabled = v),
            new Option("Skin da picareta",
                    () -> NeonConfig.INSTANCE.customPickaxe, v -> NeonConfig.INSTANCE.customPickaxe = v)
    );

    public NeonMenuScreen(Screen parent) {
        super("Neon Client", parent);
    }

    @Override
    protected int contentHeight() {
        return 24 + 8 + options.size() * 24;
    }

    @Override
    protected void addContent(int left, int top, int width) {
        int half = (width - 8) / 2;
        addDrawableChild(ButtonWidget.builder(Text.literal("Cosméticos"),
                        b -> client.setScreen(new NeonCosmeticsScreen(this)))
                .dimensions(left, top, half, 20).build());
        addDrawableChild(ButtonWidget.builder(Text.literal("View Model e Swing"),
                        b -> client.setScreen(new NeonViewModelScreen(this)))
                .dimensions(left + half + 8, top, half, 20).build());

        int y = top + 32;
        for (Option opt : options) {
            addDrawableChild(ButtonWidget.builder(onOff(opt.label(), opt.get().getAsBoolean()), button -> {
                opt.set().accept(!opt.get().getAsBoolean());
                button.setMessage(onOff(opt.label(), opt.get().getAsBoolean()));
                NeonConfig.save();
            }).dimensions(left, y, width, 20).build());
            y += 24;
        }
    }
}
