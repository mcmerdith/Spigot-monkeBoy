package net.mcmerdith.monkeboy.selection

import net.mcmerdith.monkeboy.Main
import net.mcmerdith.monkeboy.util.ChatUtil
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.block.Container
import org.bukkit.entity.HumanEntity
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitTask
import org.bukkit.util.Vector

class CloneArea(val player: Player?, val location1: Location) : TNArea() {
    companion object {
        val ALL = mutableMapOf<HumanEntity, CloneArea>()
    }

    lateinit var location2: Location
    lateinit var destination: Location

    lateinit var originStart: Vector
    lateinit var originFinish: Vector
    lateinit var originBox: BukkitTask

    lateinit var destinationStart: Vector
    lateinit var destinationFinish: Vector
    lateinit var destinationBox: BukkitTask

    private val hasPlayer = player != null

    private val loc1Particle: BukkitTask = Bukkit.getScheduler().runTaskTimer(Main.getInstance(), Runnable {
        if (hasPlayer) spawnParticle(player!!, location1)
    }, 10, 60)

    init {
        if (hasPlayer) ChatUtil.info(player!!, "Set origin to ${location1.toVector()}")
    }

    fun next(location: Location) {
        var message = "Set "
        if (!this::location2.isInitialized) {
            location2 = location
            calculateOrigin()
            message += "finish"
        } else if (!this::destination.isInitialized) {
            destination = location
            calculateDestination()
            message = "target"
        }
        if (hasPlayer) ChatUtil.info(player!!, "$message to ${location.toVector()}")
    }

    private fun calculateOrigin() {
        val vl1 = location1.toVector()
        val vl2 = location2.toVector()

        originStart = Vector.getMinimum(vl1, vl2)
        originFinish = Vector.getMaximum(vl1, vl2)
        if (hasPlayer) originBox = drawBorderFor(player!!, originStart, originFinish)
    }

    private fun calculateDestination() {
        val vl1 = destination.toVector()
        val vl2 = vl1.clone().add(originFinish.clone().subtract(originStart))

        destinationStart = Vector.getMinimum(vl1, vl2)
        destinationFinish = Vector.getMaximum(vl1, vl2)
        if (hasPlayer) destinationBox = drawBorderFor(player!!, destinationStart, destinationFinish)
    }

    override fun execute() {
        if (
            ready()
        ) {
            if (hasPlayer) ChatUtil.info(player!!, "Cloning $location1 -> $location2 to $destination")
            Bukkit.getScheduler().runTask(Main.getInstance(), Runnable {
                val distances = originFinish.subtract(originStart)
                for (x in 0..distances.blockX)
                    for (y in 0..distances.blockY)
                        for (z in 0..distances.blockZ) {
                            val offset = Vector(x, y, z)

                            val target = destinationStart.clone().add(offset)
                            val original = originStart.clone().add(offset)

                            val targetBlock = Location(location1.world, target.x, target.y, target.z).block
                            val originalBlock = Location(location1.world, original.x, original.y, original.z).block

                            targetBlock.type = originalBlock.type
                            targetBlock.blockData = originalBlock.blockData

                            val origState = originalBlock.state
                            val targetState = targetBlock.state

                            if (origState is Container && targetState is Container) {
                                origState.inventory.contents.forEachIndexed { i, v ->
                                    targetState.inventory.setItem(i, v)
                                }
                            }
                        }
                if (hasPlayer) ChatUtil.success(player!!, "Complete")

                expire(false)
            })
        } else {
            if (hasPlayer) ChatUtil.error(player!!, "Something went wrong cloning the area")
        }
    }

    fun ready(): Boolean {
        return this::location2.isInitialized &&
                this::destination.isInitialized &&
                this::originStart.isInitialized &&
                this::originFinish.isInitialized &&
                this::destinationStart.isInitialized &&
                location1.world == location2.world && location1.world == destination.world &&
                location1.world != null
    }

    override fun expire(notifyPlayer: Boolean) {
        if (hasPlayer) ALL.remove(player!!)

        loc1Particle.cancel()
        watchdog.cancel()
        if (::originBox.isInitialized) originBox.cancel()
        if (::destinationBox.isInitialized) destinationBox.cancel()

        if (hasPlayer && notifyPlayer) {
            val str =
                if (ready()) "Clone Job ($originStart -> $originFinish to $destinationStart) expired" else "Clone Job expired"
            ChatUtil.error(player!!, str)
        }
    }
}