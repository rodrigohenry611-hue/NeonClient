package dev.neonclient;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

/** Base visual (painel azul escuro com borda neon) para todas as telas do client. */
public abstract class NeonScreen extends Screen {
    protected static final int ACCENT = 0xFF35D0FF;
    protected static final int PANEL = 0xD0081522;

    protected final Screen parent;
    protected int panelX, panelY, panelW, panelH;

    protected NeonScreen(String title, Screen parent) {
        super(Text.literal(title));
        this.parent = parent;
    }

    protected int panelWidth() {
        return 340;
    }

    /** Altura do conteúdo, sem contar o botão Voltar/Fechar. */
    protected abstract int contentHeight();

    protected abstract void addContent(int left, int top, int width);

    @Override
    protected void init() {
        panelW = panelWidth();
        panelH = 44 + contentHeight() + 8 + 20 + 12;
        panelX = (width - panelW) / 2;
        panelY = (height - panelH) / 2;

        addContent(panelX + 16, panelY + 44, panelW - 32);

        addDrawableChild(ButtonWidget.builder(
                        Text.literal(parent == null ? "Fechar" : "Voltar"), b -> close())
                .dimensions(panelX + panelW / 2 - 50, panelY + 44 + contentHeight() + 8, 100, 20)
                .build());
    }

    protected static Text onOff(String name, boolean on) {
        return Text.literal(name + ": " + (on ? "ON" : "OFF"))
                .formatted(on ? Formatting.AQUA : Formatting.GRAY);
    }

    @Override
    public void renderBackground(DrawContext ctx, int mouseX, int mouseY, float delta) {
        super.renderBackground(ctx, mouseX, mouseY, delta);

        ctx.fill(panelX, panelY, panelX + panelW, panelY + panelH, PANEL);
        ctx.fill(panelX, panelY, panelX + panelW, panelY + 1, ACCENT);
        ctx.fill(panelX, panelY + panelH - 1, panelX + panelW, panelY + panelH, ACCENT);
        ctx.fill(panelX, panelY, panelX + 1, panelY + panelH, ACCENT);
        ctx.fill(panelX + panelW - 1, panelY, panelX + panelW, panelY + panelH, ACCENT);
        ctx.fill(panelX + 16, panelY + 34, panelX + panelW - 16, panelY + 35, 0x8035D0FF);
    }

    @Override
    public void render(DrawContext ctx, int mouseX, int mouseY, float delta) {
        super.render(ctx, mouseX, mouseY, delta);
        ctx.drawCenteredTextWithShadow(textRenderer, title, width / 2, panelY + 14, ACCENT);
    }

    @Override
    public void removed() {
        NeonConfig.save();
    }

    @Override
    public void close() {
        client.setScreen(parent);
    }
}
