/*
 *  SPDX-License-Identifier: LGPL-3.0-only
 *  Copyright (c) 2020 Drakmyth. All rights reserved.
 */

package com.drakmyth.minecraft.manufactory.power;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import com.drakmyth.minecraft.manufactory.LogMarkers;
import com.drakmyth.minecraft.manufactory.util.LogHelper;

import com.mojang.logging.LogUtils;
import org.slf4j.Logger;

import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;

public class PowerNetworkWalker {
    private static final Logger LOGGER = LogUtils.getLogger();

    public static List<PowerNetworkNode> walk(Map<BlockPos, Direction[]> allNodes, BlockPos start) {
        LOGGER.debug(LogMarkers.POWERNETWORK, "Beginning Power Network branch walk from {}...", LogHelper.blockPos(start));
        Queue<PowerNetworkNode> nodesToVisit = new ArrayDeque<>();
        Map<BlockPos, PowerNetworkNode> visitedNodes = new HashMap<>();
        nodesToVisit.add(new PowerNetworkNode(start, allNodes.get(start)));

        while (!nodesToVisit.isEmpty()) {
            PowerNetworkNode currentNode = nodesToVisit.remove();
            for (Direction dir : currentNode.getDirections()) {
                BlockPos posToVisit = currentNode.getPos().relative(dir);
                if (visitedNodes.containsKey(posToVisit) || !allNodes.containsKey(posToVisit)) continue;
                nodesToVisit.add(new PowerNetworkNode(posToVisit, allNodes.get(posToVisit)));
            }
            visitedNodes.put(currentNode.getPos(), currentNode);
        }

        LOGGER.debug(LogMarkers.POWERNETWORK, "Power Network branch walk complete");
        return new ArrayList<>(visitedNodes.values());
    }
}
