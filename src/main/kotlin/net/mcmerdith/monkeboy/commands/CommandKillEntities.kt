package net.mcmerdith.monkeboy.commands

import net.mcmerdith.monkeboy.util.ChatUtil
import net.mcmerdith.monkeboy.util.ItemUtil
import net.md_5.bungee.api.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.*

class CommandKillEntities : TNCommand() {
    val types: Map<String, (e: Entity) -> Boolean> = mapOf(
        "all" to { true },
        "player" to { e: Entity -> e is Player },
        "passive" to { e: Entity -> e is Animals },
        "hostile" to { e: Entity -> e is Monster },
        "living" to { e: Entity -> e is Creature },
        "nonliving" to { e: Entity -> e !is Creature },
        "item" to { e: Entity -> e is Item }
    )

    init {
        addComplete(0, types.keys.toList())
        addComplete(1, listOf("true", "false"))
        addComplete(2, listOf("true", "false"))
        addComplete(3, listOf("10", "100", "1000", "10000"))
    }

    override fun executeCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Int {
        if (isConsole(sender)) return 0
        sender as Player

        val type = (args.getOrNull(0) ?: "hostile").split('|')
        val named = (args.getOrNull(1) ?: "false").toBoolean()
        val players = (args.getOrNull(2) ?: "false").toBoolean()
        val radius = (args.getOrNull(3) ?: "-1").toDoubleOrNull() ?: -1.0
        val drops = (args.getOrNull(4) ?: "false").toBoolean()

        val entities = if (radius < 0) {
            sender.world.entities
        } else {
            sender.getNearbyEntities(radius, radius, radius)
        }

        var affected = 0

        val entitiesKilled = mutableMapOf<EntityType, Int>()
        val itemsKilled = mutableMapOf<String, Int>()

        ChatUtil.info(sender, "${ChatColor.BOLD}Entity Kill Parameters")
        ChatUtil.info(sender, "  - Types: ${type.joinToString("|")}")
        ChatUtil.info(sender, "  - Include Named Entities: $named")
        ChatUtil.info(sender, "  - Include Players: $named")
        ChatUtil.info(sender, "  - Within: ${if (radius < 0) "World" else "$radius Blocks"}")
        ChatUtil.info(sender, "  - Mob Drops: ${if (radius < 0) "World" else "$radius Blocks"}")

        entities.forEach {
            val kill = shouldKill(it, type, named, players)
            if (kill) {
                if (drops && it is Damageable) {
                    it.health = 0.0
                } else {
                    it.remove()
                }
                affected++
                when {
                    it is Item -> {
                        val itemName = ItemUtil.getName(it.itemStack)
                        val temp = itemsKilled.getOrDefault(itemName, 0) + 1
                        itemsKilled[itemName] = temp
                    }
                    it.customName != null -> {
                        ChatUtil.info(sender, "Killed Named Entity: ${it.customName ?: it.name}")
                    }
                    else -> {
                        val temp = entitiesKilled.getOrDefault(it.type, 0) + 1
                        entitiesKilled[it.type] = temp
                    }
                }
            }
        }

        if (entitiesKilled.isNotEmpty() || itemsKilled.isNotEmpty()) ChatUtil.info(sender, "${ChatColor.BOLD}Entity Summary:")
        itemsKilled.forEach { (t, u) ->
            ChatUtil.info(sender, if (u == 1) "  - ITEM $t" else "  - $u ITEM $t")
        }
        entitiesKilled.forEach { (t, u) ->
            ChatUtil.info(sender, "  - $u ${t.name}")
        }

        ChatUtil.info(sender, "Affected $affected / ${entities.size} entities")

        return 2
    }

    private fun shouldKill(entity: Entity, type: List<String>, includeNamed: Boolean, includePlayer: Boolean): Boolean {
        if (!includeNamed && entity.customName != null) return false
        if (!includePlayer && (entity is Player || type.contains("player"))) return false

        type.forEach {
            if (types[it]?.invoke(entity) == true) return true
        }

        return false
    }
}
