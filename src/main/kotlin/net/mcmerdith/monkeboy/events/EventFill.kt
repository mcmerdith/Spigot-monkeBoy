package net.mcmerdith.monkeboy.events

import net.mcmerdith.monkeboy.config.UserPrefsConfig
import net.mcmerdith.monkeboy.enum.ConfigKeys
import net.mcmerdith.monkeboy.inventory.HandlerInventoryEditFillClick
import net.mcmerdith.monkeboy.util.ChatUtil
import net.mcmerdith.monkeboy.util.InventoryUtil
import net.mcmerdith.monkeboy.util.Util
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

class EventFill : Listener {
    @EventHandler
    fun onFill(event: FillEvent) {
        val command = "(${Util.locationToString(event.area.start, true)} -> ${Util.locationToString(event.area.finish, true)}), ${event.getArea()} blocks"

        val player = event.area.player
        val log = (player != null)

        if (player == null) {
            // Server fill event, not fired by a player
            event.area.execute()
        } else {
            // Player executed
            if (event.warned || (!event.isOversized() && !UserPrefsConfig.config.getBoolean(ConfigKeys.PREF_FILLWARNING.pass(listOf(event.area.player.uniqueId.toString())), true))) {
                /* Execute the fill command
                * EITHER
                * Confirmation disabled, < 32768 blocks
                * OR
                * Confirmed
                */
                ChatUtil.royal(player, "Filling $command")
                event.area.execute()
                HandlerInventoryEditFillClick.reset(event.area.player)
            } else {
                /* Something is preventing the fill from executing
                * EITHER
                * Confirmation enabled, hasn't confirmed
                * OR
                * > 32768 blocks
                */
                event.isCancelled = true

                // Log event to player
                ChatUtil.royal(player, "Generated Fill Command: $command")
                if (event.isOversized()) {
                    ChatUtil.error(player, "${event.getArea()} is larger than the suggested maximum size (32,768 blocks)")
                    ChatUtil.error(player, "Use caution when filling large areas")
                }

                InventoryUtil.open(event.area.player, InventoryUtil.getFillComplete())

                // Arguments for command
//                val fillArgs = "${Util.locationToString(event.area.start)} ${Util.locationToString(event.area.finish)} ${event.area.type.name}"
//
//                val builder = ComponentBuilder()
//
//                // Build the execute button
//                val execute = TextComponent("${ChatColor.GREEN}[EXECUTE]")
//                execute.clickEvent = ClickEvent(ClickEvent.Action.RUN_COMMAND, "/executefill $fillArgs ${event.area.options?.joinToString() ?: ""}")
//
//                // Build the edit button
//                val edit = TextComponent("${ChatColor.AQUA}[MASK AND EXECUTE]")
//                edit.clickEvent = ClickEvent(ClickEvent.Action.RUN_COMMAND, "/editfill $fillArgs")
//
//                // Send the message
//                player.spigot().sendMessage(*builder.append(execute).append(" ").append(edit).create())
            }
        }
    }
}