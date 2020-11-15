package pw.amel.amelioratelag.command

import org.bukkit.Location
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import pw.amel.amelioratelag.AmeliorateLag
import pw.amel.amelioratelag.COMMAND_RESULTS_PER_PAGE
import java.lang.NumberFormatException
import kotlin.math.pow
import kotlin.math.sqrt

object CountPlayerEntities: CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, commandName: String, args: Array<out String>): Boolean {
        val page = args.getOrNull(0) ?: "1"
        val programmerPage = try {
            page.toInt() - 1
        } catch (e: NumberFormatException) {
            return false
        }
        val start = programmerPage * COMMAND_RESULTS_PER_PAGE
        val end = start + COMMAND_RESULTS_PER_PAGE - 1

        val playerEntities = mutableMapOf<Player, Int>()

        for (world in AmeliorateLag.instance.server.worlds) {
            for (entity in world.entities) {
                var maybePlayer: Player? = null
                var maybePlayerDistance: Double = Double.POSITIVE_INFINITY
                for (player in AmeliorateLag.instance.server.onlinePlayers) {
                    val distance = distance(player.location, entity.location)
                    if (distance < maybePlayerDistance) {
                        maybePlayerDistance = distance
                        maybePlayer = player
                    }
                }

                if (maybePlayer != null) {
                    playerEntities[maybePlayer] = (playerEntities[maybePlayer] ?: 0) + 1
                }
            }
        }

        val rankedResults: List<Player> = playerEntities.entries.sortedByDescending { it.value }.map { it.key }
        // sort the map by the value and then get only the key into a list

        val numberOfPages: Int = rankedResults.size / COMMAND_RESULTS_PER_PAGE + 1

        sender.sendMessage("~-~ Player Entity Loading Page [$page/$numberOfPages] ~-~")
        for (i in start..end) {
            val player = rankedResults.getOrNull(i) ?: continue // continue if null

            sender.sendMessage("${player.name}: ${playerEntities[player]}")
        }

        return true
    }

    /**
     * Simple distance between locations with desired behaviour regarding infinity
     */
    fun distance(playerLocation: Location, entityLocation: Location): Double {
        if (playerLocation.world != entityLocation.world) {
            return Double.POSITIVE_INFINITY
        }

        val x = playerLocation.x - entityLocation.x
        val z = playerLocation.z - entityLocation.z

        return sqrt(x.pow(2) + z.pow(2))
    }
}