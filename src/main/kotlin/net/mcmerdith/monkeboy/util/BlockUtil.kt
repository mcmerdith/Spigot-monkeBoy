package net.mcmerdith.monkeboy.util

import org.bukkit.Material

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
}