package pw.amel.amelioratelag

import org.bukkit.NamespacedKey
import org.bukkit.entity.Entity
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityBreedEvent
import org.bukkit.persistence.PersistentDataType

object MarkBredAnimalsAsDomesticated: Listener {
    val DomesticatedKey = NamespacedKey(AmeliorateLag.instance, "domesticated")

    @EventHandler(ignoreCancelled = true)
    fun onEntityBreed(event: EntityBreedEvent) {
        markEntity(event.entity)
        markEntity(event.father)
        markEntity(event.mother)
    }

    fun markEntity(entity: Entity) {
        val data = entity.persistentDataContainer
        data[DomesticatedKey, PersistentDataType.BYTE] = 1
    }

    fun Entity.isDomesticated(): Boolean {
        return persistentDataContainer.has(DomesticatedKey, PersistentDataType.BYTE)
    }
}

