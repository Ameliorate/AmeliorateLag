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

import org.bukkit.NamespacedKey
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.persistence.PersistentDataType
import org.bukkit.scheduler.BukkitRunnable
import pw.amel.amelioratelag.GarbageCollector.ProcessingResult.*
import pw.amel.amelioratelag.MarkBredAnimalsAsDomesticated.isDomesticated
import pw.amel.amelioratelag.MarkEggNamedMobs.isEggSpawned

object GarbageCollector: BukkitRunnable() {
    val GCKey = NamespacedKey(AmeliorateLag.instance, "GCKey")

    override fun run() {
        for (world in AmeliorateLag.instance.server.worlds) {
            if (world.name in EXCLUDED_WORLDS) {
                continue
            }

            for (entity in world.entities) {
                if (entity.type in EXCLUDED_ENTITIES) {
                    continue
                }

                if (processEntity(entity) == DESPAWN) {
                    entity.remove()
                }
            }
        }
    }

    /**
     * Runs the garbage collector on a specific entity
     *
     * This function mutates the entity's PersistantData to help determine if it should be despawned in the future.
     *
     * @param entity The entity to be evaluated.
     */
    fun processEntity(entity: Entity): ProcessingResult {
        val data = entity.persistentDataContainer
        if (entity.customName != null && !entity.isEggSpawned) {
            return NEVER_DESPAWN
        }

        var doMobPackingDespawn = false

        if (entity.isDomesticated) {
            if (entity.getNearbyEntities(DOMESTICATED_MOB_PACKING_SCAN_RADIUS,
                            DOMESTICATED_MOB_PACKING_SCAN_RADIUS,
                            DOMESTICATED_MOB_PACKING_SCAN_RADIUS).size > DOMESTICATED_MOB_PACKING_THRESHOLD) {
                doMobPackingDespawn = true
            } else {
                return NEVER_DESPAWN
            }
        }

        for (maybePlayer in entity.getNearbyEntities(PLAYER_SCAN_DISTANCE, PLAYER_SCAN_DISTANCE, PLAYER_SCAN_DISTANCE)) {
            if (maybePlayer is Player) {
                return NEVER_DESPAWN
                // Maybe a bad idea? This makes players AFK at spawners lag the server slightly, but prevents mobs
                // the player is actively fighting from despawning.

                // Should be okay with a very low value that can't be spread over a large farm, and a reasonable mob
                // packing value
            }
        }

        if (!data.has(GCKey, PersistentDataType.BYTE)) {
            data[GCKey, PersistentDataType.BYTE] = 0
        } else {
            data[GCKey, PersistentDataType.BYTE] = (data[GCKey, PersistentDataType.BYTE]!! + 1).toByte()
        }

        val threshold = if (doMobPackingDespawn) {
            DOMESTICATED_MOB_PACKING_COLLECTION[entity.type]!!
        } else {
            COLLECTION_MAP[entity.type]!!
        }

        val gc = data[GCKey, PersistentDataType.BYTE]!!

        if (gc > threshold) {
            return DESPAWN
        } else {
            return FUTURE_DESPAWN
        }
    }

    /**
     * Returned from processEntity to indicate what should be done with a given entity
     */
    enum class ProcessingResult {
        /**
         * The entity should be immediately despawned.
         */
        DESPAWN,

        /**
         * Do nothing yet, but the entity will eventually be despawned.
         */
        FUTURE_DESPAWN,

        /**
         * The entity is explicitly marked to be never ever despawned.
         */
        NEVER_DESPAWN,
    }
}