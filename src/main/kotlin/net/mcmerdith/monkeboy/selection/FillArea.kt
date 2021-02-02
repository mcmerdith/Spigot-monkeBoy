package net.mcmerdith.monkeboy.selection

import net.mcmerdith.monkeboy.Main
import net.mcmerdith.monkeboy.inventory.HandlerInventoryEditFillClick
import net.mcmerdith.monkeboy.util.BlockUtil
import net.mcmerdith.monkeboy.util.ChatUtil
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.HumanEntity
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitTask
import org.bukkit.util.Vector

class FillArea(val start: Location, val finish: Location, val type: Material, val player: HumanEntity? = null, val options: MutableList<BlockUtil.FillOption> = mutableListOf()) : TNArea() {
    val b1 = start.toVector()
    val b2 = finish.toVector()

    val fillStart = Vector.getMinimum(b1, b2)
    val fillFinish = Vector.getMaximum(b1, b2)

    val world = start.world
    val noWorld = world != finish.world || world == null

    val particles: BukkitTask? = (player as? Player)?.let { drawBorderFor(it, fillStart, fillFinish) }

    init {
        if (player is Player) {
            ChatUtil.actionInfo(player, "Generated fill command ($fillStart -> $fillFinish)")
        }
    }

    override fun execute() {
        if (player is Player) ChatUtil.actionInfo(player, "Filling $fillStart -> $fillFinish with ${type.name}")
        Bukkit.getScheduler().runTask(Main.getInstance(), Runnable {
            when {
                noWorld -> {
                    if (player is Player) ChatUtil.actionError(player, "The 2 points must be in the same dimension")
                }
                else -> {
                    val useIncludes = options.any { it.type == BlockUtil.FillOption.Type.IF }
                    val includeMats = options.filter { it.type == BlockUtil.FillOption.Type.IF }.map { it.mat }
                    val excludeMats = options.filter { it.type == BlockUtil.FillOption.Type.NOT }.map { it.mat }

                    for (x in fillStart.blockX..fillFinish.blockX)
                        for (y in fillStart.blockY..fillFinish.blockY)
                            for (z in fillStart.blockZ..fillFinish.blockZ) {
                                val block = start.world?.getBlockAt(x, y, z) ?: continue

                                if (useIncludes) {
                                    if (!includeMats.contains(block.type)) continue
                                    // Matched excluded item
                                } else {
                                    if (excludeMats.contains(block.type)) continue
                                    // Did not match included item
                                }

                                block.type = type
                            }

                    if (player is Player) ChatUtil.actionSuccess(player, "Complete")
                }
            }

            expire(false)
        })
    }

    override fun expire(notifyPlayer: Boolean) {
        particles?.cancel()
        HandlerInventoryEditFillClick.areas.remove(player)
        if (notifyPlayer && player is Player) ChatUtil.actionError(player, "Fill Job ($fillStart -> $fillFinish) expired")
    }
}