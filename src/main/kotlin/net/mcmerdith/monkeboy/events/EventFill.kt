package net.mcmerdith.monkeboy.events

import net.mcmerdith.monkeboy.config.UserPrefsConfig
import net.mcmerdith.monkeboy.enum.ConfigKeys
import net.mcmerdith.monkeboy.inventory.HandlerInventoryEditFillClick
import net.mcmerdith.monkeboy.util.ChatUtil
import net.mcmerdith.monkeboy.util.InventoryUtil
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

class EventFill : Listener {
    @EventHandler
    fun onFill(event: FillEvent) {
        val player = event.area.player

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

                if (event.isOversized()) {
                    ChatUtil.error(player, "${event.getArea()} is larger than the suggested maximum size (32,768 blocks)")
                    ChatUtil.error(player, "Use caution when filling large areas")
                }

                InventoryUtil.open(event.area.player, InventoryUtil.getFillComplete())
            }
        }
    }
}