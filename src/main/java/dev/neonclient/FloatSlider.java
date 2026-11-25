package dev.neonclient;

import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.text.Text;

import java.util.Locale;
import java.util.function.Consumer;

/** Slider simples para valores decimais. */
public class FloatSlider extends SliderWidget {
    private final String label;
    private final float min;
    private final float max;
    private final Consumer<Float> setter;

    public FloatSlider(int x, int y, int width, int height, String label,
                       float min, float max, float current, Consumer<Float> setter) {
        super(x, y, width, height, Text.empty(), (current - min) / (max - min));
        this.label = label;
        this.min = min;
        this.max = max;
        this.setter = setter;
        updateMessage();
    }

    private float current() {
        return (float) (min + (max - min) * value);
    }

    @Override
    protected void updateMessage() {
        setMessage(Text.literal(label + ": " + String.format(Locale.ROOT, "%.2f", current())));
    }

    @Override
    protected void applyValue() {
        setter.accept(current());
    }
}
