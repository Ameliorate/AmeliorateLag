/**
 * AmeliorateLag: Ameliorates your lag problems by deleting the bad entities while keeping the good ones
 * Copyright (C) 2020 Amelorate
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package pw.amel.amelioratelag

import org.bukkit.entity.EntityType
import org.bukkit.entity.EntityType.*

/**
 * The number of individual results per page. Used in the commands of AmeliorateLag.
 */
const val COMMAND_RESULTS_PER_PAGE: Int = 10

/**
 * The amount of time the chunk heatmap should cover
 */
const val CHUNK_HEATMAP_INTERVAL_SECONDS: Long = 5 * 60 // 5 minutes in seconds

/**
 * The number of ticks between garbage collection cycles
 */
const val GARBAGE_COLLECTION_INTERVAL_TICKS: Long = 5  * 60 * 20 // 5 minutes, in ticks

/**
 * The distance to a player an entity has to be for it to not ever be despawned
 */
const val PLAYER_SCAN_DISTANCE: Double = 10.0

/**
 * The radius of the area the plugin will search around a domesticated animal for other mobs and slowly despawn them
 * if the packing exceeds DOMESTICATED_MOB_PACKING_THRESHOLD.
 */
const val DOMESTICATED_MOB_PACKING_SCAN_RADIUS: Double = 16.0

/**
 * The number of mobs around the domesticated animal that will increment its GC count.
 */
const val DOMESTICATED_MOB_PACKING_THRESHOLD: Int = 16

/**
 * Worlds that are not garbage collected
 */
val EXCLUDED_WORLDS = arrayOf("disabled_world", "another_disabled_world")

/**
 * Entities that should never be garbage collected
 */
val EXCLUDED_ENTITIES = arrayOf(ARMOR_STAND,
        BEE,
        BOAT,
        DROPPED_ITEM,
        ELDER_GUARDIAN,
        ENDER_CRYSTAL,
        ENDER_DRAGON,
        ENDER_PEARL,
        IRON_GOLEM,
        ITEM_FRAME,
        LEASH_HITCH,
        MINECART,
        MINECART_CHEST,
        MINECART_COMMAND,
        MINECART_FURNACE,
        MINECART_HOPPER,
        MINECART_MOB_SPAWNER,
        MINECART_TNT,
        PAINTING,
        PLAYER,
        SHULKER,
        TRIDENT,
        VILLAGER,
        WITHER)

/**
 * Indicates the entities maximum GC count that will cause them to despawn.
 *
 * To find when an entity will despawn, multiply the maximum GC count with the GARBAGE_COLLECTION_INTERVAL.
 */
val COLLECTION_MAP = mutableMapOf<EntityType, Int>()

/**
 * Sets the values of COLLECTION_MAP.
 */
fun initCollectionMap() {
    // Item-type entities
    // also projectiles
    // I recommend 2 for each of these, because they normally naturally despawn / are used up and 2 makes sure they
    // don't disappear anytime soon
    COLLECTION_MAP[AREA_EFFECT_CLOUD] = 2
    COLLECTION_MAP[ARROW] = 2 // flying or landed arrow
    COLLECTION_MAP[DRAGON_FIREBALL] = 2
    COLLECTION_MAP[EGG] = 2 // thrown egg
    COLLECTION_MAP[ENDER_PEARL] = 2 // thrown ender pearl
    COLLECTION_MAP[ENDER_SIGNAL] = 2 // thrown ender eye?
    COLLECTION_MAP[EVOKER_FANGS] = 2
    COLLECTION_MAP[EXPERIENCE_ORB] = 2
    COLLECTION_MAP[FALLING_BLOCK] = 2
    COLLECTION_MAP[FIREBALL] = 2 // ghast projectile
    COLLECTION_MAP[FIREWORK] = 2
    COLLECTION_MAP[FISHING_HOOK] = 2
    COLLECTION_MAP[LIGHTNING] = 2
    COLLECTION_MAP[LLAMA_SPIT] = 2
    COLLECTION_MAP[PRIMED_TNT] = 2
    COLLECTION_MAP[SHULKER_BULLET] = 2
    COLLECTION_MAP[SMALL_FIREBALL] = 2 // blaze projectile / firestarter ball in dispensor
    COLLECTION_MAP[SNOWBALL] = 2
    COLLECTION_MAP[SPECTRAL_ARROW] = 2
    COLLECTION_MAP[SPLASH_POTION] = 2
    COLLECTION_MAP[THROWN_EXP_BOTTLE] = 2

    // Fish-type mobs
    COLLECTION_MAP[COD] = 1
    COLLECTION_MAP[DOLPHIN] = 2
    COLLECTION_MAP[PUFFERFISH] = 2
    COLLECTION_MAP[SALMON] = 2
    COLLECTION_MAP[SQUID] = 2
    COLLECTION_MAP[TROPICAL_FISH] = 2
    COLLECTION_MAP[TURTLE] = 2

    // Hostile mobs
    COLLECTION_MAP[BLAZE] = 3
    COLLECTION_MAP[CAVE_SPIDER] = 3
    COLLECTION_MAP[CREEPER] = 1
    COLLECTION_MAP[DROWNED] = 1
    COLLECTION_MAP[ENDERMAN] = 2
    COLLECTION_MAP[ENDERMITE] = 5
    COLLECTION_MAP[EVOKER] = 5
    COLLECTION_MAP[GHAST] = 1
    COLLECTION_MAP[GIANT] = 5 // ???
    COLLECTION_MAP[GUARDIAN] = 2
    COLLECTION_MAP[HOGLIN] = 2
    COLLECTION_MAP[HUSK] = 1
    COLLECTION_MAP[ILLUSIONER] = 5
    COLLECTION_MAP[MAGMA_CUBE] = 2
    COLLECTION_MAP[PHANTOM] = 1
    COLLECTION_MAP[PIGLIN] = 1
    COLLECTION_MAP[PILLAGER] = 5
    COLLECTION_MAP[RAVAGER] = 5
    COLLECTION_MAP[SILVERFISH] = 3
    COLLECTION_MAP[SKELETON] = 1
    COLLECTION_MAP[SLIME] = 2
    COLLECTION_MAP[SPIDER] = 1
    COLLECTION_MAP[STRAY] = 1
    COLLECTION_MAP[VEX] = 5
    COLLECTION_MAP[VINDICATOR] = 5
    COLLECTION_MAP[WITCH] = 1
    COLLECTION_MAP[WITHER_SKELETON] = 4
    COLLECTION_MAP[ZOGLIN] = 1
    COLLECTION_MAP[ZOMBIE] = 1
    COLLECTION_MAP[ZOMBIE_VILLAGER] = 5
    COLLECTION_MAP[ZOMBIFIED_PIGLIN] = 1

    // Passive Mobs
    COLLECTION_MAP[BAT] = 1
    COLLECTION_MAP[CAT] = 5
    COLLECTION_MAP[CHICKEN] = 5
    COLLECTION_MAP[COW] = 5
    COLLECTION_MAP[DONKEY] = 5
    COLLECTION_MAP[FOX] = 5
    COLLECTION_MAP[HORSE] = 5
    COLLECTION_MAP[LLAMA] = 5
    COLLECTION_MAP[MULE] = 5
    COLLECTION_MAP[MUSHROOM_COW] = 5
    COLLECTION_MAP[OCELOT] = 5
    COLLECTION_MAP[PARROT] = 5
    COLLECTION_MAP[PIG] = 5
    COLLECTION_MAP[POLAR_BEAR] = 5
    COLLECTION_MAP[RABBIT] = 5
    COLLECTION_MAP[SHEEP] = 5
    COLLECTION_MAP[SKELETON_HORSE] = 5
    COLLECTION_MAP[SNOWMAN] = 5
    COLLECTION_MAP[STRIDER] = 5
    COLLECTION_MAP[TRADER_LLAMA] = 5
    COLLECTION_MAP[WANDERING_TRADER] = 5
    COLLECTION_MAP[WOLF] = 5
    COLLECTION_MAP[ZOMBIE_HORSE] = 5

    // what is unknown though
    COLLECTION_MAP[UNKNOWN] = 5

    // From here on, check the config and make sure nothing is missing.
    // If you're updating to future versions, this part will complain about missing entries

    for (entity in EXCLUDED_ENTITIES) {
        COLLECTION_MAP[entity] = -1
    }

    for (type in EntityType.values()) {
        if (type !in COLLECTION_MAP) {
            AmeliorateLag.instance.logger.warning("${type} mob exists but is not mentioned in config")
        }
    }
}

val DOMESTICATED_MOB_PACKING_COLLECTION = mutableMapOf<EntityType, Int>()

fun initDomesticatedMobPackingCollection() {
    // Domesticated animals must go over their threshold for 4.16 hours to be despawned.

    DOMESTICATED_MOB_PACKING_COLLECTION[DONKEY] = 50
    DOMESTICATED_MOB_PACKING_COLLECTION[HORSE] = 50
    DOMESTICATED_MOB_PACKING_COLLECTION[SHEEP] = 50
    DOMESTICATED_MOB_PACKING_COLLECTION[COW] = 50
    DOMESTICATED_MOB_PACKING_COLLECTION[MUSHROOM_COW] = 50
    DOMESTICATED_MOB_PACKING_COLLECTION[PIG] = 50
    DOMESTICATED_MOB_PACKING_COLLECTION[CHICKEN] = 50
    DOMESTICATED_MOB_PACKING_COLLECTION[WOLF] = 50
    DOMESTICATED_MOB_PACKING_COLLECTION[CAT] = 50
    DOMESTICATED_MOB_PACKING_COLLECTION[OCELOT] = 50
    DOMESTICATED_MOB_PACKING_COLLECTION[RABBIT] = 50
    DOMESTICATED_MOB_PACKING_COLLECTION[LLAMA] = 50
    DOMESTICATED_MOB_PACKING_COLLECTION[TURTLE] = 50
    DOMESTICATED_MOB_PACKING_COLLECTION[PANDA] = 50
    DOMESTICATED_MOB_PACKING_COLLECTION[FOX] = 50
    DOMESTICATED_MOB_PACKING_COLLECTION[BEE] = 50
    DOMESTICATED_MOB_PACKING_COLLECTION[HOGLIN] = 50
    DOMESTICATED_MOB_PACKING_COLLECTION[STRIDER] = 50
}