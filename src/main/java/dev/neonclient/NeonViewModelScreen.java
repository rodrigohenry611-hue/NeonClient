package dev.neonclient;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

/** View model (posição do item na mão) e swing (velocidade e estilo da animação). */
public class NeonViewModelScreen extends NeonScreen {

    public NeonViewModelScreen(Screen parent) {
        super("View Model e Swing", parent);
    }

    @Override
    protected int panelWidth() {
        return 380;
    }

    @Override
    protected int contentHeight() {
        return 6 * 24;
    }

    @Override
    protected void addContent(int left, int top, int width) {
        NeonConfig c = NeonConfig.INSTANCE;
        int col = (width - 8) / 2;
        int right = left + col + 8;

        // Coluna esquerda: posição e escala
        addDrawableChild(ButtonWidget.builder(onOff("View model", c.viewModelEnabled), b -> {
            c.viewModelEnabled = !c.viewModelEnabled;
            b.setMessage(onOff("View model", c.viewModelEnabled));
        }).dimensions(left, top, col, 20).build());
        addDrawableChild(new FloatSlider(left, top + 24, col, 20, "Pos X", -0.5f, 0.5f, c.vmX, v -> c.vmX = v));
        addDrawableChild(new FloatSlider(left, top + 48, col, 20, "Pos Y", -0.5f, 0.5f, c.vmY, v -> c.vmY = v));
        addDrawableChild(new FloatSlider(left, top + 72, col, 20, "Pos Z", -0.5f, 0.5f, c.vmZ, v -> c.vmZ = v));
        addDrawableChild(new FloatSlider(left, top + 96, col, 20, "Escala", 0.5f, 1.5f, c.vmScale, v -> c.vmScale = v));

        // Coluna direita: rotação e swing
        addDrawableChild(new FloatSlider(right, top, col, 20, "Rot X", -60f, 60f, c.vmRotX, v -> c.vmRotX = v));
        addDrawableChild(new FloatSlider(right, top + 24, col, 20, "Rot Y", -60f, 60f, c.vmRotY, v -> c.vmRotY = v));
        addDrawableChild(new FloatSlider(right, top + 48, col, 20, "Rot Z", -60f, 60f, c.vmRotZ, v -> c.vmRotZ = v));
        addDrawableChild(new FloatSlider(right, top + 72, col, 20, "Swing vel.", 0.5f, 2.0f, c.swingSpeed, v -> c.swingSpeed = v));
        addDrawableChild(ButtonWidget.builder(styleLabel(), b -> {
            c.swingStyle = SwingStyle.of(c.swingStyle).next().name();
            b.setMessage(styleLabel());
        }).dimensions(right, top + 96, col, 20).build());

        addDrawableChild(ButtonWidget.builder(Text.literal("Resetar valores"), b -> {
            c.resetViewModel();
            NeonConfig.save();
            clearAndInit();
        }).dimensions(left, top + 128, width, 20).build());
    }

    private Text styleLabel() {
        SwingStyle style = SwingStyle.of(NeonConfig.INSTANCE.swingStyle);
        return Text.literal("Swing: " + style.label).formatted(Formatting.AQUA);
    }
}
