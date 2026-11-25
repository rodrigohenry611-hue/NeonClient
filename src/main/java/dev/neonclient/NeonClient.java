package dev.neonclient;

import dev.neonclient.cosmetics.CosmeticFeatureRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityFeatureRendererRegistrationCallback;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

public class NeonClient implements ClientModInitializer {
    public static final String MOD_ID = "neonclient";

    private static KeyBinding openMenuKey;

    @Override
    public void onInitializeClient() {
        NeonConfig.load();

        // A partir da 1.21.9 a categoria da tecla é um objeto KeyBinding.Category
        openMenuKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.neonclient.open_menu",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_RIGHT_SHIFT,
                KeyBinding.Category.create(Identifier.of(MOD_ID, "main"))
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openMenuKey.wasPressed()) {
                client.setScreen(new NeonMenuScreen(client.currentScreen));
            }
        });

        // Desenha os cosméticos em cima do modelo do jogador
        LivingEntityFeatureRendererRegistrationCallback.EVENT.register(
                (entityType, entityRenderer, helper, context) -> {
                    if (entityRenderer instanceof PlayerEntityRenderer<?> playerRenderer) {
                        FeatureRendererContext<PlayerEntityRenderState, PlayerEntityModel> ctx =
                                (FeatureRendererContext<PlayerEntityRenderState, PlayerEntityModel>) playerRenderer;
                        helper.register(new CosmeticFeatureRenderer(ctx));
                    }
                });
    }
}
