/*
*  SPDX-License-Identifier: LGPL-3.0-only
*  Copyright (c) 2020 Drakmyth. All rights reserved.
*/
package com.drakmyth.minecraft.manufactory;

import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

public class LogMarkers {
    // Manufactory Markers
    public static final Marker CONTAINER = MarkerManager.getMarker("CONTAINER");
    public static final Marker INTERACTION = MarkerManager.getMarker("INTERACTION");
    public static final Marker MACHINE = MarkerManager.getMarker("MACHINE");
    public static final Marker POWERNETWORK = MarkerManager.getMarker("POWERNETWORK");
    public static final Marker REGISTRATION = MarkerManager.getMarker("REGISTRATION");
    public static final Marker RENDERING = MarkerManager.getMarker("RENDERING");

    // Minecraft Markers
    public static final Marker NETWORK = MarkerManager.getMarker("NETWORK");
    public static final Marker SOUNDS = MarkerManager.getMarker("SOUNDS");

    // Forge Markers
    public static final Marker AXFORM = MarkerManager.getMarker("AXFORM");
    public static final Marker CAPABILITIES = MarkerManager.getMarker("CAPABILITIES");
    public static final Marker CLASSDUMP = MarkerManager.getMarker("CLASSDUMP");
    public static final Marker CLASSLOADING = MarkerManager.getMarker("CLASSLOADING"); // parent: MODLAUNCHER
    public static final Marker CLIENTHOOKS = MarkerManager.getMarker("CLIENTHOOKS");
    public static final Marker CONFIG = MarkerManager.getMarker("CONFIG");
    public static final Marker CORE = net.minecraftforge.fml.loading.LogMarkers.CORE;
    public static final Marker COREMOD = MarkerManager.getMarker("COREMOD");
    public static final Marker COREMODLOG = MarkerManager.getMarker("COREMODLOG"); // parent: COREMOD
    public static final Marker CRAFTHELPER = MarkerManager.getMarker("CRAFTHELPER");
    public static final Marker DISTXFORM = MarkerManager.getMarker("DISTXFORM");
    public static final Marker EVENTBUS = MarkerManager.getMarker("EVENTBUS");
    public static final Marker FMLHANDSHAKE = MarkerManager.getMarker("FMLHANDSHAKE");
    public static final Marker FMLNETWORK = MarkerManager.getMarker("FMLNETWORK");
    public static final Marker FORGE = MarkerManager.getMarker("FORGE");
    public static final Marker FORGEHOOKS = MarkerManager.getMarker("FORGEHOOKS");
    public static final Marker FORGEMOD = MarkerManager.getMarker("FORGEMOD"); // parent: LOADING
    public static final Marker LAUNCHPLUGIN = MarkerManager.getMarker("LAUNCHPLUGIN"); // parent: MODLAUNCHER
    public static final Marker LOADING = net.minecraftforge.fml.loading.LogMarkers.LOADING;
    public static final Marker MODELLOADING = MarkerManager.getMarker("MODELLOADING");
    public static final Marker MODLAUNCHER = MarkerManager.getMarker("MODLAUNCHER");
    public static final Marker NETREGISTRY = MarkerManager.getMarker("NETREGISTRY");
    public static final Marker REFLECTION = MarkerManager.getMarker("REFLECTION");
    public static final Marker REGISTRIES = MarkerManager.getMarker("REGISTRIES");
    public static final Marker REGISTRYDUMP = MarkerManager.getMarker("REGISTRYDUMP");
    public static final Marker SCAN = net.minecraftforge.fml.loading.LogMarkers.SCAN;
    public static final Marker SERVERHOOKS = MarkerManager.getMarker("SERVERHOOKS");
    public static final Marker SIMPLENET = MarkerManager.getMarker("SIMPLENET");
    public static final Marker SPLASH = net.minecraftforge.fml.loading.LogMarkers.SPLASH;
    public static final Marker USERNAMECACHE = MarkerManager.getMarker("USERNAMECACHE");
    public static final Marker WP = MarkerManager.getMarker("WP");

    // Log4J Markers
    public static final Marker CATCHING = MarkerManager.getMarker("CATCHING"); // parent: EXCEPTION
    public static final Marker ENTER = MarkerManager.getMarker("ENTER"); // parent: FLOW
    public static final Marker EVENT = MarkerManager.getMarker("EVENT");
    public static final Marker EXCEPTION = MarkerManager.getMarker("EXCEPTION");
    public static final Marker EXIT = MarkerManager.getMarker("EXIT"); // parent: FLOW
    public static final Marker FLOW = MarkerManager.getMarker("FLOW");
    public static final Marker LOOKUP = MarkerManager.getMarker("LOOKUP");
    public static final Marker SHUTDOWN_HOOK = MarkerManager.getMarker("SHUTDOWN HOOK");
    public static final Marker THROWING = MarkerManager.getMarker("THROWING"); // parent: EXCEPTION
}
