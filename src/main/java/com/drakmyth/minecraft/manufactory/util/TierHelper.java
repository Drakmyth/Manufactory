package com.drakmyth.minecraft.manufactory.util;

import java.util.List;
import net.minecraft.world.item.ToolMaterial;

public final class TierHelper {
    private static final List<ToolMaterial> MATERIALS = List.of(
            ToolMaterial.WOOD,
            ToolMaterial.STONE,
            ToolMaterial.COPPER,
            ToolMaterial.IRON,
            ToolMaterial.DIAMOND,
            ToolMaterial.NETHERITE
    );

    private TierHelper() {
    }

    public static int compare(ToolMaterial first, ToolMaterial second) {
        return Integer.compare(index(first), index(second));
    }

    public static ToolMaterial max(ToolMaterial first, ToolMaterial second) {
        return compare(first, second) > 0 ? first : second;
    }

    public static ToolMaterial min(ToolMaterial first, ToolMaterial second) {
        return compare(first, second) < 0 ? first : second;
    }

    private static int index(ToolMaterial material) {
        int index = MATERIALS.indexOf(material);
        return index < 0 ? 0 : index;
    }
}
