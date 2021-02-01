package net.mcmerdith.monkeboy.util

import org.bukkit.Bukkit
import org.bukkit.Location
import kotlin.random.Random

object Util {
    fun locationToString(loc: Location, pretty: Boolean = false): String {
        if (pretty) return "${loc.blockX},${loc.blockY},${loc.blockZ}"
        return "${loc.world?.name ?: "unknown"}:${loc.blockX}:${loc.blockY}:${loc.blockZ}"
    }

    fun stringToLocation(loc: String): Location? {
        val regex = Regex("""(.*):(.*):(.*):(.*)""")

        regex.matchEntire(loc)?.groupValues?.let { groups ->
            val world = Bukkit.getWorld(groups.getOrNull(1) ?: "unknown")
            val x = groups.getOrNull(2)?.toDoubleOrNull()
            val y = groups.getOrNull(3)?.toDoubleOrNull()
            val z = groups.getOrNull(4)?.toDoubleOrNull()

            return if (world == null || x == null || y == null || z == null) null else Location(world, x, y, z)
        }

        return null
    }

    fun randomString(length: Int, chars: CharArray = "abcdefghijklmnopqrstuvwxyz".toCharArray()): String {
        return (1..length.coerceAtLeast(1))
            .map { Random.nextInt(0, chars.size) }
            .map(chars::get)
            .joinToString("")
    }
}