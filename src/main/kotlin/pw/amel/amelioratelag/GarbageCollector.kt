package pw.amel.amelioratelag

import org.bukkit.NamespacedKey
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.entity.Tameable
import org.bukkit.persistence.PersistentDataType
import org.bukkit.scheduler.BukkitRunnable
import pw.amel.amelioratelag.GarbageCollector.ProcessingResult.*
import pw.amel.amelioratelag.MarkBredAnimalsAsDomesticated.isDomesticated

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
        if (entity.customName != null) {
            return NEVER_DESPAWN
        }

        if (entity is Tameable && entity.isTamed) {
            return NEVER_DESPAWN
            // TODO: make entities that are packed together despawn after a long time (chunkloaded, thus taking server perf)
        }

        if (entity.isDomesticated()) {
            return NEVER_DESPAWN
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

        val data = entity.persistentDataContainer
        if (!data.has(GCKey, PersistentDataType.BYTE)) {
            data[GCKey, PersistentDataType.BYTE] = 0
        } else {
            data[GCKey, PersistentDataType.BYTE] = (data[GCKey, PersistentDataType.BYTE]!! + 1).toByte()
        }

        val threshold = COLLECTION_MAP[entity.type]!!
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