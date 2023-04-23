package com.drakmyth.minecraft.manufactory.datagen;

import com.drakmyth.minecraft.manufactory.Reference;
import com.drakmyth.minecraft.manufactory.init.ModBlocks;
import com.drakmyth.minecraft.manufactory.init.ModItems;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;

public class ModLanguageProvider extends LanguageProvider {
    public ModLanguageProvider(DataGenerator generator, String locale) {
        super(generator, Reference.MOD_ID, locale);
    }

    @Override
    protected void addTranslations() {
        addBlock(ModBlocks.AMBER_BLOCK, "Amber Block");
        addBlock(ModBlocks.METALOSOL_BLOCK, "Metalosol");
        addBlock(ModBlocks.MECHANITE_BLOCK, "Mechanite Block");
        addBlock(ModBlocks.MECHANITE_PANEL, "Mechanite Panel");
        addBlock(ModBlocks.GRINDER, "Grinder");
        addBlock(ModBlocks.BALL_MILL, "Ball Mill");
        addBlock(ModBlocks.LATEX_COLLECTOR, "Latex Collector");
        addBlock(ModBlocks.POWER_CABLE, "Power Cable");
        addBlock(ModBlocks.SOLAR_PANEL, "Solar Panel");

        addItem(ModItems.AMBER, "Amber");
        addItem(ModItems.MOTOR_TIER0, "Basic Motor");
        addItem(ModItems.MOTOR_TIER1, "Magnetic Motor");
        addItem(ModItems.MOTOR_TIER2, "Compound Motor");
        addItem(ModItems.MOTOR_TIER3, "Induction Motor");
        addItem(ModItems.GRINDER_WHEEL_TIER0, "Wooden Grinder Wheel");
        addItem(ModItems.GRINDER_WHEEL_TIER1, "Stone Grinder Wheel");
        addItem(ModItems.GRINDER_WHEEL_TIER2, "Iron Grinder Wheel");
        addItem(ModItems.GRINDER_WHEEL_TIER3, "Diamond Grinder Wheel");
        addItem(ModItems.GRINDER_WHEEL_TIER4, "Netherite Grinder Wheel");
        addItem(ModItems.MILLING_BALL_TIER0, "Wooden Milling Ball");
        addItem(ModItems.MILLING_BALL_TIER1, "Stone Milling Ball");
        addItem(ModItems.MILLING_BALL_TIER2, "Iron Milling Ball");
        addItem(ModItems.MILLING_BALL_TIER3, "Diamond Milling Ball");
        addItem(ModItems.MILLING_BALL_TIER4, "Netherite Milling Ball");
        addItem(ModItems.POWER_SOCKET, "Power Socket");
        addItem(ModItems.BATTERY, "Battery");
        addItem(ModItems.REDSTONE_WIRE, "Redstone Wire");
        addItem(ModItems.COUPLING, "Coupling");
        addItem(ModItems.WRENCH, "Wrench");
        addItem(ModItems.COAGULATED_LATEX, "Coagulated Latex");
        addItem(ModItems.GROUND_COAL_ORE_ROUGH, "Ground Coal Ore (Rough)");
        addItem(ModItems.GROUND_DIAMOND_ORE_ROUGH, "Ground Diamond Ore (Rough)");
        addItem(ModItems.GROUND_EMERALD_ORE_ROUGH, "Ground Emerald Ore (Rough)");
        addItem(ModItems.GROUND_GOLD_ORE_ROUGH, "Ground Gold Ore (Rough)");
        addItem(ModItems.GROUND_IRON_ORE_ROUGH, "Ground Iron Ore (Rough)");
        addItem(ModItems.GROUND_COPPER_ORE_ROUGH, "Ground Copper Ore (Rough)");
        addItem(ModItems.GROUND_LAPIS_ORE_ROUGH, "Ground Lapis Lazuli Ore (Rough)");
        addItem(ModItems.GROUND_NETHER_QUARTZ_ORE_ROUGH, "Ground Nether Quartz Ore (Rough)");
        addItem(ModItems.GROUND_REDSTONE_ORE_ROUGH, "Ground Redstone Ore (Rough)");
        addItem(ModItems.GROUND_ANCIENT_DEBRIS_ROUGH, "Ground Ancient Debris (Rough)");
        addItem(ModItems.GROUND_COAL_ORE_FINE, "Ground Coal Ore (Fine)");
        addItem(ModItems.GROUND_DIAMOND_ORE_FINE, "Ground Diamond Ore (Fine)");
        addItem(ModItems.GROUND_EMERALD_ORE_FINE, "Ground Emerald Ore (Fine)");
        addItem(ModItems.GROUND_GOLD_ORE_FINE, "Ground Gold Ore (Fine)");
        addItem(ModItems.GROUND_IRON_ORE_FINE, "Ground Iron Ore (Fine)");
        addItem(ModItems.GROUND_COPPER_ORE_FINE, "Ground Copper Ore (Fine)");
        addItem(ModItems.GROUND_LAPIS_ORE_FINE, "Ground Lapis Lazuli Ore (Fine)");
        addItem(ModItems.GROUND_NETHER_QUARTZ_ORE_FINE, "Ground Nether Quartz Ore (Fine)");
        addItem(ModItems.GROUND_REDSTONE_ORE_FINE, "Ground Redstone Ore (Fine)");
        addItem(ModItems.GROUND_ANCIENT_DEBRIS_FINE, "Ground Ancient Debris (Fine)");
        addItem(ModItems.RUBBER, "Rubber");
        addItem(ModItems.TAPPING_KNIFE, "Tapping Knife");
        addItem(ModItems.SLURRIED_COAL_ORE_BUCKET, "Slurried Coal Ore Bucket");
        addItem(ModItems.SLURRIED_DIAMOND_ORE_BUCKET, "Slurried Diamond Ore Bucket");
        addItem(ModItems.SLURRIED_EMERALD_ORE_BUCKET, "Slurried Emerald Ore Bucket");
        addItem(ModItems.SLURRIED_GOLD_ORE_BUCKET, "Slurried Gold Ore Bucket");
        addItem(ModItems.SLURRIED_IRON_ORE_BUCKET, "Slurried Iron Ore Bucket");
        addItem(ModItems.SLURRIED_COPPER_ORE_BUCKET, "Slurried Copper Ore Bucket");
        addItem(ModItems.SLURRIED_LAPIS_ORE_BUCKET, "Slurried Lapis Lazuli Ore Bucket");
        addItem(ModItems.SLURRIED_NETHER_QUARTZ_ORE_BUCKET, "Slurried Nether Quartz Ore Bucket");
        addItem(ModItems.SLURRIED_REDSTONE_ORE_BUCKET, "Slurried Redstone Ore Bucket");
        addItem(ModItems.SLURRIED_ANCIENT_DEBRIS_BUCKET, "Slurried Ancient Debris Bucket");
        addItem(ModItems.ROCK_DRILL, "Rock Drill");
        addItem(ModItems.DRILL_HEAD_TIER0, "Basic Drill Head");
        addItem(ModItems.DRILL_HEAD_TIER1, "Tapered Drill Head");
        addItem(ModItems.DRILL_HEAD_TIER2, "Retractable Drill Head");
        addItem(ModItems.DRILL_HEAD_TIER3, "Pneumatic Drill Head");
        addItem(ModItems.DRILL_HEAD_TIER4, "Tri-Cone Drill Head");

        add("itemGroup.manufactory", "Manufactory");
    }
}
