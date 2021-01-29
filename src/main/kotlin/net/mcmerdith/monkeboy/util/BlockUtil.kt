package net.mcmerdith.monkeboy.util

import net.mcmerdith.monkeboy.Main
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.HumanEntity
import org.bukkit.util.Vector

object BlockUtil {
    class FillOption(val type: Type, val mat: Material) {
        enum class Type(val value: String) {
            IF("INCLUDE"),
            NOT("EXCLUDE");

            companion object {
                fun from(input: String): Type? {
                    return values().firstOrNull { it.value == input }
                }
            }
        }

        fun matches(mat: Material): Boolean {
            return when (type) {
                Type.IF -> this.mat == mat
                Type.NOT -> this.mat != mat
            }
        }

        companion object {
            fun from(input: String): FillOption? {
                val regex = Regex("""(INCLUDE|EXCLUDE):(.*)""")
                return regex.matchEntire(input)?.groupValues?.let {
                    val type = Type.from(it[1])
                    val mat = Material.matchMaterial(it[2])

                    if (type == null || mat == null) null
                    else FillOption(type, mat)
                }
            }
        }

        override fun toString(): String {
            return "${type.value}:${mat.name}"
        }
    }

    class FillArea(val start: Location, val finish: Location, val type: Material, val player: HumanEntity? = null, val options: MutableList<FillOption> = mutableListOf()) {
        fun execute() {
            Bukkit.getScheduler().runTask(Main.getInstance(), Runnable {
                val log = player != null

                val b1 = start.toVector()
                val b2 = finish.toVector()

                when {
                    start.world != finish.world -> {
                        if (log) ChatUtil.error(player!!, "The 2 points must be in the same dimension")
                    }
                    else -> {
                        val fillStart = Vector.getMinimum(b1, b2)
                        val fillFinish = Vector.getMaximum(b1, b2)

                        val useIncludes = options.any { it.type == FillOption.Type.IF }
                        val includeMats = options.filter { it.type == FillOption.Type.IF }.map { it.mat }
                        val excludeMats = options.filter { it.type == FillOption.Type.NOT }.map { it.mat }

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
                    }
                }
            })
        }
    }
}