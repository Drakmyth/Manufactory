package com.drakmyth.minecraft.manufactory.datagen;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import com.google.common.base.Preconditions;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.server.packs.PackType;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.data.ExistingFileHelper;

public abstract class AnimatedTextureProvider implements DataProvider {
    private final Map<ResourceLocation, Builder> data = new TreeMap<>();
    private final DataGenerator gen;
    private final String modid;
    private final ExistingFileHelper existingFileHelper;

    public AnimatedTextureProvider(DataGenerator generator, String modid, ExistingFileHelper existingFileHelper) {
        this.gen = generator;
        this.modid = modid;
        this.existingFileHelper = existingFileHelper;
    }

    protected abstract void registerAnimatedTextures();

    @Override
    public void run(CachedOutput cache) throws IOException {
        registerAnimatedTextures();
        if (data.isEmpty()) return;

        for (Entry<ResourceLocation, Builder> entry : data.entrySet()) {
            ResourceLocation key = entry.getKey();
            Path path = this.gen.getOutputFolder().resolve("assets/" + key.getNamespace() + "/textures/" + key.getPath() + ".png.mcmeta");
            DataProvider.saveStable(cache, entry.getValue().toJson(), path);
        }
    }

    @Override
    public String getName() {
        return "AnimatedTextures: " + modid;
    }

    public Builder getBuilder(ResourceLocation texture) {
        Preconditions.checkNotNull(texture, "Texture must not be null");
        boolean textureExists = existingFileHelper.exists(texture, PackType.CLIENT_RESOURCES, ".png", "textures");
        Preconditions.checkArgument(textureExists, "Texture %s does not exist in any known resource pack", texture);
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
