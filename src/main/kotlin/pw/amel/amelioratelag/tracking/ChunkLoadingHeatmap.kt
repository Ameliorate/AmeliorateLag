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

package pw.amel.amelioratelag.tracking

import org.bukkit.ChatColor
import org.bukkit.World
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.world.ChunkLoadEvent
import org.bukkit.scheduler.BukkitRunnable
import pw.amel.amelioratelag.CHUNK_HEATMAP_INTERVAL_SECONDS
import java.lang.NumberFormatException
import java.time.Instant

object ChunkLoadingHeatmap: Listener, BukkitRunnable(), CommandExecutor {
    data class ChunkLocation(val x: Int, val z: Int, val world: World) {
        override fun toString(): String {
            return "${ChatColor.AQUA}[${x * 16} ${z * 16} ${world.name}]${ChatColor.RESET}"
        }
    }

    val chunkLoadTimes = mutableMapOf<Instant, ChunkLocation>()

    @EventHandler
    fun onChunkLoad(event: ChunkLoadEvent) {
        assert(Instant.now() !in chunkLoadTimes)

        chunkLoadTimes[Instant.now()] = ChunkLocation(event.chunk.x, event.chunk.z, event.chunk.world)
    }

    /**
     * Every 5 minutes, remove the old entries
     */
    override fun run() {
        garbageCollectChunkLoadTimes()
    }

    fun garbageCollectChunkLoadTimes() {
        for (time in chunkLoadTimes.keys.toSet()) {
            if (time.plusSeconds(CHUNK_HEATMAP_INTERVAL_SECONDS).isBefore(Instant.now())) {
                chunkLoadTimes.remove(time)
            }
        }
    }

    const val RESULTS_PER_PAGE = 10

    override fun onCommand(sender: CommandSender, command: Command, commandName: String, args: Array<out String>): Boolean {
        val page = args.getOrNull(0) ?: "1"
        val programmerPage = try {
            page.toInt() - 1
        } catch (e: NumberFormatException) {
            return false
        }
        val start = programmerPage * RESULTS_PER_PAGE
        val end = start + RESULTS_PER_PAGE - 1

        garbageCollectChunkLoadTimes()

        val chunkLoadCounts = mutableMapOf<ChunkLocation, Int>()

        for (chunk in chunkLoadTimes.values) {
            chunkLoadCounts[chunk] = (chunkLoadCounts[chunk] ?: 0) + 1
        }

        val rankedResults: List<ChunkLocation> = chunkLoadCounts.entries.sortedBy { it.value }.filter { it.value > 1 }.map { it.key }
        // sort the map by the value, and then get only the key into a list

        val numberOfPages: Int = rankedResults.size / RESULTS_PER_PAGE + 1

        sender.sendMessage("~-~ Chunkloading Hotspots Page [$page/$numberOfPages] ~-~")
        for (i in start..end) {
            val chunkLocation = rankedResults.getOrNull(i) ?: continue // continue if null

            sender.sendMessage("$chunkLocation: ${chunkLoadCounts[chunkLocation]}")
        }

        return true
    }
}