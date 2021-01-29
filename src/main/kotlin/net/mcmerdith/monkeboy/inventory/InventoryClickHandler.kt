package net.mcmerdith.monkeboy.inventory

import org.bukkit.event.inventory.InventoryClickEvent

abstract class InventoryClickHandler {
    abstract fun clickEvent(event: InventoryClickEvent)
}