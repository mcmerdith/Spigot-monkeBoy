package net.mcmerdith.monkeboy.events

import net.mcmerdith.monkeboy.enum.Inventories
import net.mcmerdith.monkeboy.inventory.HandlerScrollableInventory
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent

class EventInventoryClick : Listener {
    private val scrollable = HandlerScrollableInventory()

    @EventHandler
    fun onInventoryClick(event: InventoryClickEvent) {
        scrollable.clickEvent(event)
        if (event.isCancelled) return

        Inventories.values().filter { /*event.view.title.startsWith(it.invName())*/ event.inventory.holder == it.handler }.forEach {
            it.handler?.clickEvent(event)
        }
    }
}