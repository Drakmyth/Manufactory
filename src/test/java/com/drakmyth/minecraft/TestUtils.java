// package com.drakmyth.minecraft;

// import java.util.UUID;
// import com.drakmyth.minecraft.manufactory.Reference;
// import com.mojang.authlib.GameProfile;
// import net.minecraft.server.level.ServerPlayer;
// import net.minecraft.server.MinecraftServer;
// import net.minecraft.world.level.Level;
// import net.minecraft.server.level.ServerLevel;
// import net.minecraftforge.common.util.FakePlayerFactory;
// import net.minecraftforge.event.world.WorldEvent;
// import net.minecraftforge.eventbus.api.SubscribeEvent;
// import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
// import net.minecraftforge.fml.event.server.FMLServerStartingEvent;

// @EventBusSubscriber(modid = Reference.MOD_ID, bus = EventBusSubscriber.Bus.FORGE)
// public class TestUtils {
//     private static MinecraftServer server;
//     private static ServerWorld world;
//     private static ServerPlayerEntity player;

//     @SubscribeEvent
//     public static void fmlServerStarting(FMLServerStartingEvent event) {
//         server = event.getServer();
//     }

//     @SubscribeEvent
//     public static void worldLoad(WorldEvent.Load event) {
//         if (world != null) return;
//         if (!(event.getWorld() instanceof ServerWorld)) return;
//         world = (ServerWorld)event.getWorld();
//         if (world.getDimensionKey() != World.OVERWORLD) {
//             world = null;
//         } else {
//             GameProfile profile = new GameProfile(new UUID(0, 0), "TestPlayer");
//             player = FakePlayerFactory.get(world, profile);
//         }
//     }

//     public static MinecraftServer getServer() {
//         return server;
//     }

//     public static ServerWorld getWorld() {
//         return world;
//     }

//     public static ServerPlayerEntity getPlayer() {
//         return player;
//     }
// }
