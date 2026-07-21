package com.drakmyth.minecraft.manufactory.recipes;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

public final class ManufactoryRecipeSerializer {
    private static final Codec<ToolMaterial> TOOL_MATERIAL_CODEC = Codec.STRING.xmap(
            ManufactoryRecipeSerializer::materialByName,
            ManufactoryRecipeSerializer::materialName
    );

    private ManufactoryRecipeSerializer() {
    }

    public static <T extends ManufactoryRecipe> RecipeSerializer<T> create(Factory<T> factory) {
        MapCodec<T> mapCodec = RecordCodecBuilder.mapCodec(instance -> instance.group(
                Ingredient.CODEC.fieldOf("ingredient").forGetter(ManufactoryRecipe::getIngredient),
                ItemStack.CODEC.fieldOf("result").forGetter(ManufactoryRecipe::getResultItem),
                Codec.FLOAT.fieldOf("extraChance").forGetter(ManufactoryRecipe::getExtraChance),
                Codec.INT.listOf().fieldOf("extraAmounts").forGetter(recipe -> toList(recipe.getExtraAmounts())),
                TOOL_MATERIAL_CODEC.fieldOf("tierRequired").forGetter(ManufactoryRecipe::getTierRequired),
                Codec.INT.fieldOf("powerRequired").forGetter(ManufactoryRecipe::getPowerRequired),
                Codec.INT.fieldOf("processTime").forGetter(ManufactoryRecipe::getProcessTime)
        ).apply(instance, (ingredient, result, chance, amounts, material, power, time) ->
                factory.create(ingredient, result, chance, toArray(amounts), material, power, time)));

        StreamCodec<RegistryFriendlyByteBuf, T> streamCodec = new StreamCodec<>() {
            @Override
            public T decode(RegistryFriendlyByteBuf buffer) {
                Ingredient ingredient = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);
                ItemStack result = ItemStack.STREAM_CODEC.decode(buffer);
                float chance = buffer.readFloat();
                int[] amounts = new int[buffer.readVarInt()];
                for (int i = 0; i < amounts.length; i++) amounts[i] = buffer.readVarInt();
                ToolMaterial material = materialByName(buffer.readUtf());
                return factory.create(ingredient, result, chance, amounts, material, buffer.readVarInt(), buffer.readVarInt());
            }

            @Override
            public void encode(RegistryFriendlyByteBuf buffer, T recipe) {
                Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, recipe.getIngredient());
                ItemStack.STREAM_CODEC.encode(buffer, recipe.getResultItem());
                buffer.writeFloat(recipe.getExtraChance());
                int[] amounts = recipe.getExtraAmounts();
                buffer.writeVarInt(amounts.length);
                for (int amount : amounts) buffer.writeVarInt(amount);
                buffer.writeUtf(materialName(recipe.getTierRequired()));
                buffer.writeVarInt(recipe.getPowerRequired());
                buffer.writeVarInt(recipe.getProcessTime());
            }
        };
        return new RecipeSerializer<>(mapCodec, streamCodec);
    }

    private static List<Integer> toList(int[] values) {
        return java.util.Arrays.stream(values).boxed().toList();
    }

    private static int[] toArray(List<Integer> values) {
        return values.stream().mapToInt(Integer::intValue).toArray();
    }

    private static ToolMaterial materialByName(String name) {
        return switch (name.toLowerCase(java.util.Locale.ROOT)) {
            case "wood" -> ToolMaterial.WOOD;
            case "stone" -> ToolMaterial.STONE;
            case "copper" -> ToolMaterial.COPPER;
            case "iron" -> ToolMaterial.IRON;
            case "diamond" -> ToolMaterial.DIAMOND;
            case "gold" -> ToolMaterial.GOLD;
            case "netherite" -> ToolMaterial.NETHERITE;
            default -> throw new IllegalArgumentException("Unknown tool material: " + name);
        };
    }

    private static String materialName(ToolMaterial material) {
        if (material == ToolMaterial.WOOD) return "wood";
        if (material == ToolMaterial.STONE) return "stone";
        if (material == ToolMaterial.COPPER) return "copper";
        if (material == ToolMaterial.IRON) return "iron";
        if (material == ToolMaterial.DIAMOND) return "diamond";
        if (material == ToolMaterial.GOLD) return "gold";
        if (material == ToolMaterial.NETHERITE) return "netherite";
        throw new IllegalArgumentException("Unsupported tool material");
    }

    @FunctionalInterface
    public interface Factory<T extends ManufactoryRecipe> {
        T create(Ingredient ingredient, ItemStack result, float extraChance, int[] extraAmounts,
                ToolMaterial tierRequired, int powerRequired, int processTime);
    }
}
