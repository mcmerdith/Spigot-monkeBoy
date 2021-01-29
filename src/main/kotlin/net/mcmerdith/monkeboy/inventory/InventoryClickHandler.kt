package net.mcmerdith.monkeboy.inventory

import org.bukkit.Material
import org.bukkit.entity.HumanEntity
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack

abstract class InventoryClickHandler {
    fun clickEvent(event: InventoryClickEvent) {
        // FILTER OUT ANYTHING THATS IRRELEVANT
        val item = event.currentItem
        if (item?.type == Material.AIR) return

        handle(item, event.whoClicked, event)
    }

    abstract fun handle(clickedItem: ItemStack?, player: HumanEntity, event: InventoryClickEvent)
}