/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */

package com.drakmyth.minecraft.manufactory.datagen;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.data.IDataProvider;
import net.minecraft.util.ResourceLocation;

public abstract class AnimatedTextureProvider implements IDataProvider {
    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();
    private final Map<ResourceLocation, Builder> data = new TreeMap<>();
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

        for (Entry<ResourceLocation, Builder> entry : data.entrySet()) {
            ResourceLocation key = entry.getKey();
            Path path = this.gen.getOutputFolder().resolve("assets/" + key.getNamespace() + "/textures/" + key.getPath() + ".mcmeta");
            IDataProvider.save(GSON, cache, entry.getValue().toJson(), path);
        }
    }

    @Override
    public String getName() {
        return "AnimatedTextures: " + modid;
    }

    public Builder getBuilder(ResourceLocation texture) {
        Builder builder = new Builder();
        data.put(texture, builder);
        return builder;
    }

    public ResourceLocation mcLoc(String path) {
        return new ResourceLocation("minecraft", path);
    }

    public ResourceLocation modLoc(String path) {
        return new ResourceLocation(modid, path);
    }

    public class Builder {
        private Integer frametime;
        private int[] frames;

        public Builder() {
            frametime = null;
            frames = new int[0];
        }

        public Builder frametime(int frametime) {
            this.frametime = frametime;
            return this;
        }

        public Builder frames(int... frames) {
            this.frames = frames;
            return this;
        }

        public JsonElement toJson() {
            JsonObject animation = new JsonObject();
            if (frametime != null) {
                animation.addProperty("frametime", frametime);
            }
            if (frames.length > 0) {
                JsonArray framesArray = new JsonArray();
                Arrays.stream(frames).forEach(frame -> framesArray.add(frame));
                animation.add("frames", framesArray);
            }
            JsonObject root = new JsonObject();
            root.add("animation", animation);
            return root;
        }
    }
}
