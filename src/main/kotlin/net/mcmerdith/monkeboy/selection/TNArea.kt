package net.mcmerdith.monkeboy.selection

import net.mcmerdith.monkeboy.Main
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Particle
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitTask
import org.bukkit.util.Vector

abstract class TNArea {
    private val timestamp = System.currentTimeMillis()
    val watchdog = Bukkit.getScheduler().runTaskTimer(Main.getInstance(), Runnable {
        if (System.currentTimeMillis() - timestamp > 300000) expire()
    }, 0, 60)

    fun drawBorderFor(player: Player, start: Vector, finish: Vector): BukkitTask {
        return Bukkit.getScheduler().runTaskTimer(Main.getInstance(), Runnable {
            listOf(
                Pair(start.y, start.z),
                Pair(start.y, finish.z),
                Pair(finish.y, start.z),
                Pair(finish.y, finish.z)
            ).forEach {
                for (x in start.blockX..finish.blockX) {
                    spawnParticle(
                        player,
                        Location(player.world, x.toDouble(), it.first, it.second)
                    )
                }
            }

            // Spawn on the Y Axis
            listOf(
                Pair(start.x, start.z),
                Pair(start.x, finish.z),
                Pair(finish.x, start.z),
                Pair(finish.x, finish.z)
            ).forEach {
                for (y in start.blockY..finish.blockY) {
                    spawnParticle(
                        player,
                        Location(player.world, it.first, y.toDouble(), it.second)
                    )
                }
            }

            // Spawn on the Z Axis
            listOf(
                Pair(start.x, start.y),
                Pair(start.x, finish.y),
                Pair(finish.x, start.y),
                Pair(finish.x, finish.y)
            ).forEach {
                for (z in start.blockZ..finish.blockZ) {
                    spawnParticle(
                        player,
                        Location(player.world, it.first, it.second, z.toDouble()),
                    )
                }

            }
        }, 0, 60)

    }

    fun spawnParticle(player: Player, location: Location) {
        player.spawnParticle(Particle.BARRIER, location.x + 0.5, location.y + 0.5, location.z + 0.5, 1)
    }

    abstract fun execute()
    abstract fun expire(notifyPlayer: Boolean = true)
}