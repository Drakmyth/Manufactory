package com.drakmyth.minecraft.manufactory.blocks;

import net.minecraft.util.StringRepresentable;

public enum ReceptacleCell implements StringRepresentable {
   NONE("none"),
   EMPTY("empty"),
   FULL("full");

   private final String name;

   private ReceptacleCell(String pName) {
      this.name = pName;
   }

   public String toString() {
      return this.getSerializedName();
   }

   public String getSerializedName() {
      return this.name;
   }
}