package net.mcmerdith.monkeboy.events

import net.mcmerdith.monkeboy.enum.Inventories
import net.mcmerdith.monkeboy.inventory.*
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent

class EventInventoryClick : Listener {
    private val scrollable = HandlerScrollableInventory()
    private val clickHandlers = mapOf(
            Inventories.MENU.invName() to HandlerMenuInventoryClick(),
            Inventories.FILL.invName() to HandlerFillInventoryClick(),
            Inventories.PREFERENCES.invName() to HandlerPrefsInventoryClick(),
            Inventories.FILLEDIT.invName() to HandlerEditFillInventoryClick(),
            Inventories.FILLEDIT_SELECT.invName() to HandlerSelectorEditFillInventoryClick(),
            Inventories.FILLCOMPLETE.invName() to HandlerFillConfirmClick()
    )

    @EventHandler
    fun onInventoryClick(event: InventoryClickEvent) {
        scrollable.clickEvent(event)
        if (event.isCancelled) return
        clickHandlers.filter { event.view.title.startsWith(it.key) }.forEach {
            it.value.clickEvent(event)
        }
    }
}