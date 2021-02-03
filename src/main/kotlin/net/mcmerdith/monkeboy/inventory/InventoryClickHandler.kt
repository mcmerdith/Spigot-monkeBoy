package net.mcmerdith.monkeboy.inventory

import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.HumanEntity
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder
import org.bukkit.inventory.ItemStack

abstract class InventoryClickHandler : InventoryHolder {
    override fun getInventory(): Inventory {
        return Bukkit.createInventory(this, 9)
    }

    fun clickEvent(event: InventoryClickEvent) {
        // FILTER OUT ANYTHING THATS IRRELEVANT
        val item = event.currentItem
        if (item?.type == Material.AIR) return

        handle(item, event.whoClicked, event)
    }

    abstract fun handle(clickedItem: ItemStack?, player: HumanEntity, event: InventoryClickEvent)
}