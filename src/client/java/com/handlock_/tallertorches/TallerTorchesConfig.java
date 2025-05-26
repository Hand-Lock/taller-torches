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

/** Singleton con un solo parametro: altezza torcia in px (vanilla = 10). */
public final class TallerTorchesConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger("TallerTorches");

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path FILE = FabricLoader.getInstance()
            .getConfigDir().resolve("tallertorches.json");

    // ---------------------- campo esposto all'utente ----------------------
    public int torch_height_px = 13;          // default: torcia "più lunga" di 3 px

    // ---------------------- derivate, NON salvate ------------------------
    public transient double offset_y;         // +Δpx / 16
    public transient double offset_face;      // |Δpx| * tan(22.5°) / 16

    private static TallerTorchesConfig INSTANCE;
    private static final int VANILLA_PX = 10;
    private static final double TAN_22_5 = Math.tan(Math.toRadians(22.5));

    /* --------------------------------------------------------------------- */
    public static TallerTorchesConfig get() {
        if (INSTANCE == null) load();
        return INSTANCE;
    }

    /* ---- IO ---------------------------------------------------------------- */

    private static void load() {
        try {
            if (Files.notExists(FILE)) saveDefault();
            INSTANCE = GSON.fromJson(Files.readString(FILE), TallerTorchesConfig.class);
        } catch (IOException | JsonSyntaxException e) {
            LOGGER.warn("Failed to load config; falling back to defaults", e);
            INSTANCE = new TallerTorchesConfig();          // fallback
        }
        INSTANCE.computeDerived();
    }

    private static void saveDefault() throws IOException {
        Files.createDirectories(FILE.getParent());
        Files.writeString(FILE, GSON.toJson(new TallerTorchesConfig()));
    }

    /* ---- calcola offset_y / offset_face ogni volta che cambia torch_height_px ---- */
    private void computeDerived() {
        double deltaPx = torch_height_px - VANILLA_PX;
        offset_y    =  deltaPx / 16.0;
        offset_face = Math.abs(deltaPx) * TAN_22_5 / 16.0;
    }
}