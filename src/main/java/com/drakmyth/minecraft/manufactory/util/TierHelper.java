package com.drakmyth.minecraft.manufactory.util;

import java.util.List;
import net.minecraft.world.item.Tier;
import net.minecraftforge.common.TierSortingRegistry;

public final class TierHelper {
    /**
     * Returns the relation between two Tiers according to {@link TierSortingRegistry#getSortedTiers}.
     * If an argument is not found, it is assumed to be the lowest.
     * If both arguments are not found, {@code 0} is returned.
     * 
     * @param t1 an argument.
     * @param t2 another argument.
     * @return <pre>
     * -1 if t1 is lower than t2
     * 0 if t1 and t2 are equal
     * 1 if t1 is higher than t2
     * </pre>
     */
    public static int compare(final Tier t1, final Tier t2) {
        List<Tier> tiers = TierSortingRegistry.getSortedTiers();
        int t1Index = tiers.indexOf(t1);
        int t2Index = tiers.indexOf(t2);

        return Integer.signum(t1Index - t2Index);
    }

    /**
     * Returns the higher of two tiers according to {@link TierSortingRegistry#getSortedTiers}.
     * If both arguments have the same value, {@code t2} is returned.
     * 
     * @param t1 an argument.
     * @param t2 another argument.
     * @return the higher of {@code t1} and {@code t2}.
     */
    public static Tier max(final Tier t1, final Tier t2) {
        return compare(t1, t2) > 0 ? t1 : t2;
    }

    /**
     * Returns the lower of two tiers according to {@link TierSortingRegistry#getSortedTiers}.
     * If both arguments have the same value, {@code t2} is returned.
     * 
     * @param t1 an argument.
     * @param t2 another argument.
     * @return the lower of {@code t1} and {@code t2}.
     */
    public static Tier min(final Tier t1, final Tier t2) {
        return compare(t1, t2) < 0 ? t1 : t2;
    }
}
