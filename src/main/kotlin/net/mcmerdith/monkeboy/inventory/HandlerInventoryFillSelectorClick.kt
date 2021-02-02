package net.mcmerdith.monkeboy.inventory

import net.mcmerdith.monkeboy.util.InventoryUtil
import net.mcmerdith.monkeboy.util.ItemUtil
import org.bukkit.Material
import org.bukkit.entity.HumanEntity
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack

class HandlerInventoryFillSelectorClick : InventoryClickHandler() {
    override fun handle(clickedItem: ItemStack?, player: HumanEntity, event: InventoryClickEvent) {
        event.isCancelled = true

        clickedItem ?: return
        if (clickedItem.type == Material.AIR) return
        val item = if (clickedItem == InventoryUtil.UI.ITEMS.AIR_ITEM) ItemUtil.getFillItem(Material.AIR, matOverride = clickedItem.type) else ItemUtil.getFillItem(clickedItem.type)
        InventoryUtil.open(player, InventoryUtil.getTakeMenu(item))
    }
}