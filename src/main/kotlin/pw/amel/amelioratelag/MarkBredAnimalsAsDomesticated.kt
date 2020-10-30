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
import org.bukkit.entity.Tameable
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityBreedEvent
import org.bukkit.persistence.PersistentDataType

object MarkBredAnimalsAsDomesticated: Listener {
    val DomesticatedKey = NamespacedKey(AmeliorateLag.instance, "Domesticated")

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

    val Entity.isDomesticated: Boolean
        get() {
            return (this is Tameable && isTamed) || persistentDataContainer.has(DomesticatedKey, PersistentDataType.BYTE)
        }
}

