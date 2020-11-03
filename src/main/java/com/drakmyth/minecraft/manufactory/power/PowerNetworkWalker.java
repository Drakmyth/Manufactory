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

import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;

public class PowerNetworkWalker {
    public static List<PowerNetworkNode> walk(Map<BlockPos, Direction[]> allNodes, BlockPos start) {
        Queue<PowerNetworkNode> nodesToVisit = new ArrayDeque<>();
        Map<BlockPos, PowerNetworkNode> visitedNodes = new HashMap<>();
        nodesToVisit.add(new PowerNetworkNode(start, allNodes.get(start)));

        while (!nodesToVisit.isEmpty()) {
            PowerNetworkNode currentNode = nodesToVisit.remove();
            for (Direction dir : currentNode.getDirections()) {
                BlockPos posToVisit = currentNode.getPos().offset(dir);
                if (visitedNodes.containsKey(posToVisit) || !allNodes.containsKey(posToVisit)) continue;
                nodesToVisit.add(new PowerNetworkNode(posToVisit, allNodes.get(posToVisit)));
            }
            visitedNodes.put(currentNode.getPos(), currentNode);
        }

        return new ArrayList<>(visitedNodes.values());
    }
}
