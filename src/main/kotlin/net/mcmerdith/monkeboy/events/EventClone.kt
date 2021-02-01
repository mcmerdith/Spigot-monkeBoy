package net.mcmerdith.monkeboy.events

import net.mcmerdith.monkeboy.util.InventoryUtil
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

class EventClone : Listener {
    @EventHandler
    fun onClone(event: CloneEvent) {
        val player = event.area.player

        if (player == null) {
            // Server clone event, not fired by a player
            event.area.execute()
        } else {
            // Player executed
            if (event.warned) {
                /*
                * Confirmed
                */
                event.area.execute()
            } else {
                /*
                * Confirmation enabled, hasn't confirmed
                */
                event.isCancelled = true
                InventoryUtil.open(event.area.player, InventoryUtil.getCloneComplete())
            }
        }
    }
}