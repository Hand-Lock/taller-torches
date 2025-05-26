package com.handlock_.tallertorches;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/** Tiny self-contained config. */
public final class TallerTorchesConfig {

    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    private static final Path FILE = FabricLoader.getInstance()
            .getConfigDir().resolve("tallertorches.json");

    /** valori di fallback / default */
    public double offset_y    = 0.1875;  // 3 px
    public double offset_face = 0.1250;  // 2 px

    // --- Singleton ---------------------------------------------------------

    private static TallerTorchesConfig INSTANCE;

    public static TallerTorchesConfig get() {
        if (INSTANCE == null) load();
        return INSTANCE;
    }

    // --- Lettura / scrittura ----------------------------------------------

    public static void load() {
        try {
            if (Files.notExists(FILE)) {
                saveDefault();                   // crea file con default leggibili
            }
            String json = Files.readString(FILE);
            INSTANCE = GSON.fromJson(json, TallerTorchesConfig.class);
        } catch (IOException | JsonSyntaxException e) {
            // in caso di errore usa default in memoria e logga lo stacktrace
            e.printStackTrace();
            INSTANCE = new TallerTorchesConfig();
        }
    }

    private static void saveDefault() throws IOException {
        String json = GSON.toJson(new TallerTorchesConfig());
        Files.createDirectories(FILE.getParent());
        Files.writeString(FILE, json);
    }
}
