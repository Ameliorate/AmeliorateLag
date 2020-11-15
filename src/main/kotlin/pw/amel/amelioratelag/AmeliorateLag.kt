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

import org.bukkit.plugin.java.JavaPlugin
import pw.amel.amelioratelag.command.ChunkLoadingHeatmap
import pw.amel.amelioratelag.command.EntityHeatmap
import pw.amel.amelioratelag.tracking.MarkBredAnimalsAsDomesticated

class AmeliorateLag: JavaPlugin() {
	companion object {
		private var instanceStorage: AmeliorateLag? = null

		internal val instance: AmeliorateLag
			get() = instanceStorage!!
	}
	
	override fun onEnable() {
		instanceStorage = this
		initCollectionMap()
		initDomesticatedMobPackingCollection()

		server.pluginManager.registerEvents(MarkBredAnimalsAsDomesticated, this)
		server.pluginManager.registerEvents(ChunkLoadingHeatmap, this)

		getCommand("chunkloadheatmap")?.setExecutor(ChunkLoadingHeatmap)
		getCommand("entityheatmap")?.setExecutor(EntityHeatmap)

		GarbageCollector.runTaskTimer(this, GARBAGE_COLLECTION_INTERVAL_TICKS, GARBAGE_COLLECTION_INTERVAL_TICKS)
		ChunkLoadingHeatmap.runTaskTimer(this, CHUNK_HEATMAP_INTERVAL_SECONDS * 20, CHUNK_HEATMAP_INTERVAL_SECONDS * 20)
	}
}
