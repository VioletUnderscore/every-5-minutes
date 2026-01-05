package net.violetunderscore.every5minutes.returns;

import java.util.Random;

public class returnRandoms {
    public static String returnMob() {
        String[] mobList = {
                "minecraft:elder_guardian",
                "minecraft:wither_skeleton",
                "minecraft:stray",
                "minecraft:husk",
                "minecraft:zombie_villager",
                "minecraft:evoker",
                "minecraft:vex",
                "minecraft:vindicator",
                "minecraft:illusioner",
                "minecraft:creeper",
                "minecraft:skeleton",
                "minecraft:spider",
                "minecraft:giant",
                "minecraft:zombie",
                "minecraft:slime",
                "minecraft:ghast",
                "minecraft:piglin",
                "minecraft:piglin_brute",
                "minecraft:hoglin",
                "minecraft:enderman",
                "minecraft:cave_spider",
                "minecraft:silverfish",
                "minecraft:blaze",
                "minecraft:magma_cube",
                "minecraft:ender_dragon",
                "minecraft:wither",
                "minecraft:witch",
                "minecraft:endermite",
                "minecraft:guardian",
                "minecraft:shulker",
                "minecraft:pig",
                "minecraft:sheep",
                "minecraft:cow",
                "minecraft:chicken",
                "minecraft:wolf",
                "minecraft:mooshroom",
                "minecraft:snow_golem",
                "minecraft:iron_golem",
                "minecraft:horse",
                "minecraft:rabbit",
                "minecraft:polar_bear",
                "minecraft:villager",
                "minecraft:breeze",
                "minecraft:warden",
                "minecraft:bee",
                "minecraft:bogged",
                "minecraft:phantom",
                "minecraft:pillager",
                "minecraft:silverfish",
                "minecraft:ravanger",
                "minecraft:vex"
        };
        Random random = new Random();
        return mobList[random.nextInt(0, mobList.length - 1)];
    }

    public static String returnColor() {
        String[] colorList = {
                "Red",
                "Orange",
                "Yellow",
                "Green",
                "Blue",
                "Purple",
                "White",
                "Gray",
                "Black",
                "Brown"
        };
        Random random = new Random();
        return colorList[random.nextInt(0, colorList.length - 1)];
    }

    public static String returnPotionEffect() {
        String[] effectList = {
                "minecraft:speed",
                "minecraft:slowness",
                "minecraft:haste",
                "minecraft:mining_fatigue",
                "minecraft:strength",
                "minecraft:instant_health",
                "minecraft:instant_damage",
                "minecraft:jump_boost",
                "minecraft:nausea",
                "minecraft:regeneration",
                "minecraft:resistance",
                "minecraft:fire_resistance",
                "minecraft:water_breathing",
                "minecraft:invisibility",
                "minecraft:blindness",
                "minecraft:night_vision",
                "minecraft:hunger",
                "minecraft:weakness",
                "minecraft:poison",
                "minecraft:wither",
                "minecraft:health_boost",
                "minecraft:absorption",
                "minecraft:saturation",
                "minecraft:glowing",
                "minecraft:levitation",
                "minecraft:luck",
                "minecraft:unluck",
                "minecraft:slow_falling",
                "minecraft:conduit_power",
                "minecraft:dolphins_grace",
                "minecraft:bad_omen",
                "minecraft:hero_of_the_village",
                "minecraft:darkness",
                "minecraft:wind_charged"
        };
        Random random = new Random();
        return effectList[random.nextInt(effectList.length)];
    }
}
