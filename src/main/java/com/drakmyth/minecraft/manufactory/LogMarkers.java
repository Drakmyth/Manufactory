/*
*  SPDX-License-Identifier: LGPL-3.0-only
*  Copyright (c) 2020 Drakmyth. All rights reserved.
*/
package com.drakmyth.minecraft.manufactory;

import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

public class LogMarkers {
    
    // Manufactory Markers
    public static final Marker CONTAINER = MarkerFactory.getMarker("CONTAINER");
    public static final Marker INTERACTION = MarkerFactory.getMarker("INTERACTION");
    public static final Marker MACHINE = MarkerFactory.getMarker("MACHINE");
    public static final Marker POWERNETWORK = MarkerFactory.getMarker("POWERNETWORK");
    public static final Marker REGISTRATION = MarkerFactory.getMarker("REGISTRATION");
    public static final Marker RENDERING = MarkerFactory.getMarker("RENDERING");

    // Minecraft Markers
    public static final Marker NETWORK = MarkerFactory.getMarker("NETWORK");
    public static final Marker SOUNDS = MarkerFactory.getMarker("SOUNDS");

    // Forge Markers
    public static final Marker AXFORM = MarkerFactory.getMarker("AXFORM");
    public static final Marker CAPABILITIES = MarkerFactory.getMarker("CAPABILITIES");
    public static final Marker CLASSDUMP = MarkerFactory.getMarker("CLASSDUMP");
    public static final Marker CLASSLOADING = MarkerFactory.getMarker("CLASSLOADING"); // parent: MODLAUNCHER
    public static final Marker CLIENTHOOKS = MarkerFactory.getMarker("CLIENTHOOKS");
    public static final Marker CONFIG = MarkerFactory.getMarker("CONFIG");
    public static final Marker CORE = net.minecraftforge.fml.loading.LogMarkers.CORE;
    public static final Marker COREMOD = MarkerFactory.getMarker("COREMOD");
    public static final Marker COREMODLOG = MarkerFactory.getMarker("COREMODLOG"); // parent: COREMOD
    public static final Marker CRAFTHELPER = MarkerFactory.getMarker("CRAFTHELPER");
    public static final Marker DISTXFORM = MarkerFactory.getMarker("DISTXFORM");
    public static final Marker EVENTBUS = MarkerFactory.getMarker("EVENTBUS");
    public static final Marker FMLHANDSHAKE = MarkerFactory.getMarker("FMLHANDSHAKE");
    public static final Marker FMLNETWORK = MarkerFactory.getMarker("FMLNETWORK");
    public static final Marker FORGE = MarkerFactory.getMarker("FORGE");
    public static final Marker FORGEHOOKS = MarkerFactory.getMarker("FORGEHOOKS");
    public static final Marker FORGEMOD = MarkerFactory.getMarker("FORGEMOD"); // parent: LOADING
    public static final Marker LAUNCHPLUGIN = MarkerFactory.getMarker("LAUNCHPLUGIN"); // parent: MODLAUNCHER
    public static final Marker LOADING = net.minecraftforge.fml.loading.LogMarkers.LOADING;
    public static final Marker MODELLOADING = MarkerFactory.getMarker("MODELLOADING");
    public static final Marker MODLAUNCHER = MarkerFactory.getMarker("MODLAUNCHER");
    public static final Marker NETREGISTRY = MarkerFactory.getMarker("NETREGISTRY");
    public static final Marker REFLECTION = MarkerFactory.getMarker("REFLECTION");
    public static final Marker REGISTRIES = MarkerFactory.getMarker("REGISTRIES");
    public static final Marker REGISTRYDUMP = MarkerFactory.getMarker("REGISTRYDUMP");
    public static final Marker SCAN = net.minecraftforge.fml.loading.LogMarkers.SCAN;
    public static final Marker SERVERHOOKS = MarkerFactory.getMarker("SERVERHOOKS");
    public static final Marker SIMPLENET = MarkerFactory.getMarker("SIMPLENET");
    public static final Marker SPLASH = net.minecraftforge.fml.loading.LogMarkers.SPLASH;
    public static final Marker USERNAMECACHE = MarkerFactory.getMarker("USERNAMECACHE");
    public static final Marker WP = MarkerFactory.getMarker("WP");

    // Log4J Markers
    public static final Marker CATCHING = MarkerFactory.getMarker("CATCHING"); // parent: EXCEPTION
    public static final Marker ENTER = MarkerFactory.getMarker("ENTER"); // parent: FLOW
    public static final Marker EVENT = MarkerFactory.getMarker("EVENT");
    public static final Marker EXCEPTION = MarkerFactory.getMarker("EXCEPTION");
    public static final Marker EXIT = MarkerFactory.getMarker("EXIT"); // parent: FLOW
    public static final Marker FLOW = MarkerFactory.getMarker("FLOW");
    public static final Marker LOOKUP = MarkerFactory.getMarker("LOOKUP");
    public static final Marker SHUTDOWN_HOOK = MarkerFactory.getMarker("SHUTDOWN HOOK");
    public static final Marker THROWING = MarkerFactory.getMarker("THROWING"); // parent: EXCEPTION
}
