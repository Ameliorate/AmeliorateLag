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

package pw.amel.amelioratelag.command

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import pw.amel.amelioratelag.AmeliorateLag
import pw.amel.amelioratelag.COMMAND_RESULTS_PER_PAGE
import pw.amel.amelioratelag.ChunkLocation
import java.lang.NumberFormatException

object EntityHeatmap: CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, commandName: String, args: Array<out String>): Boolean {
        val page = args.getOrNull(0) ?: "1"
        val programmerPage = try {
            page.toInt() - 1
        } catch (e: NumberFormatException) {
            return false
        }
        val start = programmerPage * COMMAND_RESULTS_PER_PAGE
        val end = start + COMMAND_RESULTS_PER_PAGE - 1

        val entityChunks = mutableMapOf<ChunkLocation, Int>()

        for (world in AmeliorateLag.instance.server.worlds) {
            for (entity in world.entities) {
                val chunkLocation = ChunkLocation(entity.location.blockX / 16, entity.location.blockZ / 16, entity.location.world!!)
                entityChunks[chunkLocation] = (entityChunks[chunkLocation] ?: 0) + 1
            }
        }

        val rankedResults: List<ChunkLocation> = entityChunks.entries.sortedByDescending { it.value }.filter { it.value > 1 }.map { it.key }
        // sort the map by the value, find chunks that have been loaded more than once, and then get only the key into a list

        val numberOfPages: Int = rankedResults.size / COMMAND_RESULTS_PER_PAGE + 1

        sender.sendMessage("~-~ Entity Hotspots Page [$page/$numberOfPages] ~-~")
        for (i in start..end) {
            val chunkLocation = rankedResults.getOrNull(i) ?: continue // continue if null

            sender.sendMessage("$chunkLocation: ${entityChunks[chunkLocation]}")
        }

        return true
    }
}