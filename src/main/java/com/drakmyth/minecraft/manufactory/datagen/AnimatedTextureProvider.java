/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */

package com.drakmyth.minecraft.manufactory.datagen;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.data.IDataProvider;
import net.minecraft.util.ResourceLocation;

public abstract class AnimatedTextureProvider implements IDataProvider {
    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();
    private final Map<ResourceLocation, Integer> data = new TreeMap<>();
    private final DataGenerator gen;
    private final String modid;

    public AnimatedTextureProvider(DataGenerator gen, String modid) {
        this.gen = gen;
        this.modid = modid;
    }

    protected abstract void registerAnimatedTextures();

    @Override
    public void act(DirectoryCache cache) throws IOException {
        registerAnimatedTextures();
        if (data.isEmpty()) return;

        for (Entry<ResourceLocation, Integer> entry : data.entrySet()) {
            ResourceLocation key = entry.getKey();
            Path path = this.gen.getOutputFolder().resolve("assets/" + key.getNamespace() + "/textures/" + key.getPath() + ".mcmeta");
            IDataProvider.save(GSON, cache, toJson(entry.getValue()), path);
        }
    }

    @Override
    public String getName() {
        return "AnimatedTextures: " + modid;
    }

    public void texture(ResourceLocation texture) {
        texture(texture, null);
    }

    public void texture(ResourceLocation texture, Integer frametime) {
        data.put(texture, frametime);
    }

    public ResourceLocation mcLoc(String path) {
        return new ResourceLocation("minecraft", path);
    }

    public ResourceLocation modLoc(String path) {
        return new ResourceLocation(modid, path);
    }

    private JsonElement toJson(Integer frametime) {
        JsonObject animation = new JsonObject();
        if (frametime != null) {
            animation.addProperty("frametime", frametime);
        }
        JsonObject root = new JsonObject();
        root.add("animation", animation);
        return root;
    }
}
