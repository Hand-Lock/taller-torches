/*
 * SPDX-License-Identifier: LGPL-3.0-or-later
 * Copyright (C) 2025 HandLock_
 */

package com.handlock_.tallertorches;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Configurazione (singleton) di Taller Torches.
 * <p>
 *  ─ {@code torch_height_px}   : altezza in pixel della texture (vanilla = 10).<br>
 *  ─ {@code include_redstone}  : applica il tweak anche alle redstone-torch.
 */
public final class TallerTorchesConfig {

    /*────────────────────────── logger & I/O ───────────────────────────*/
    private static final Logger LOGGER = LoggerFactory.getLogger("TallerTorches");
    private static final Gson   GSON   = new GsonBuilder().setPrettyPrinting().create();
    private static final Path   FILE   = FabricLoader.getInstance()
            .getConfigDir()
            .resolve("tallertorches.json");

    /*────────────────────────── opzioni UTENTE ─────────────────────────*/
    /** Altezza della texture torch.png in pixel (10 = vanilla). */
    public int torch_height_px = 13;

    /** Estende il comportamento a (wall-)redstone-torch. */
    public boolean include_redstone = true;

    /*────────────────────────── derivate, NON salvate ──────────────────*/
    public transient double offset_y;    // +Δpx / 16
    public transient double offset_face; // |Δpx| · tan 22.5° / 16

    /*────────────────────────── impl. singleton ────────────────────────*/
    private static TallerTorchesConfig INSTANCE;
    private static final int    VANILLA_PX = 10;
    private static final double TAN_22_5   = Math.tan(Math.toRadians(22.5));

    public static TallerTorchesConfig get() {
        if (INSTANCE == null) load();
        return INSTANCE;
    }

    /*────────────────────────── caricamento/salvataggio ───────────────*/
    private static void load() {
        try {
            if (Files.notExists(FILE)) saveDefault();
            INSTANCE = GSON.fromJson(Files.readString(FILE), TallerTorchesConfig.class);
        } catch (IOException | JsonSyntaxException e) {
            LOGGER.warn("Failed to load config; falling back to defaults", e);
            INSTANCE = new TallerTorchesConfig(); // fallback
        }
        INSTANCE.computeDerived();
    }

    private static void saveDefault() throws IOException {
        Files.createDirectories(FILE.getParent());
        Files.writeString(FILE, GSON.toJson(new TallerTorchesConfig()));
    }

    /*────────────────────────── calcoli derivati ───────────────────────*/
    private void computeDerived() {
        double deltaPx   = torch_height_px - VANILLA_PX;
        offset_y    =  deltaPx / 16.0;
        offset_face = deltaPx * TAN_22_5 / 16.0;   // può essere negativo
    }
}
