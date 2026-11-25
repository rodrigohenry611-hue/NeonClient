package dev.neonclient;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

/** Configuração do client, salva em config/neonclient.json. */
public class NeonConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path FILE =
            FabricLoader.getInstance().getConfigDir().resolve("neonclient.json");

    public static NeonConfig INSTANCE = new NeonConfig();

    // ---- Opções gerais ----
    public boolean infoHud = true;
    public boolean potionHud = true;
    public boolean armorHud = true;
    public boolean customHotbar = true;
    public boolean chatTag = true;
    public boolean cosmeticsEnabled = true;
    public boolean customPickaxe = true;

    // ---- Cosméticos: slot -> id do cosmético equipado ----
    public Map<String, String> equipped = new HashMap<>();

    // ---- View model (posição do item na mão) ----
    public boolean viewModelEnabled = true;
    public float vmX = 0f;     // blocos
    public float vmY = 0f;
    public float vmZ = 0f;
    public float vmScale = 1f;
    public float vmRotX = 0f;  // graus
    public float vmRotY = 0f;
    public float vmRotZ = 0f;

    // ---- Swing ----
    public float swingSpeed = 1f;          // 1 = normal, 2 = duas vezes mais rápido
    public String swingStyle = "VANILLA";  // ver SwingStyle

    public NeonConfig() {
        equipped.put("BACK", "cape_neon");
        equipped.put("PET", "pet_chibi");
    }

    public void resetViewModel() {
        viewModelEnabled = true;
        vmX = vmY = vmZ = 0f;
        vmScale = 1f;
        vmRotX = vmRotY = vmRotZ = 0f;
        swingSpeed = 1f;
        swingStyle = "VANILLA";
    }

    public static void load() {
        if (Files.exists(FILE)) {
            try (Reader reader = Files.newBufferedReader(FILE)) {
                NeonConfig loaded = GSON.fromJson(reader, NeonConfig.class);
                if (loaded != null) {
                    if (loaded.equipped == null) loaded.equipped = new HashMap<>();
                    INSTANCE = loaded;
                }
            } catch (IOException | RuntimeException e) {
                e.printStackTrace();
            }
        }
        save();
    }

    public static void save() {
        try (Writer writer = Files.newBufferedWriter(FILE)) {
            GSON.toJson(INSTANCE, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
