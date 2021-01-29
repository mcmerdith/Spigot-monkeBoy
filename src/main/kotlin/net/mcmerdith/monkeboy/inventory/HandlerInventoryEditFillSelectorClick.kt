package net.mcmerdith.monkeboy.inventory

import net.mcmerdith.monkeboy.util.InventoryUtil
import org.bukkit.Material
import org.bukkit.entity.HumanEntity
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack

class HandlerInventoryEditFillSelectorClick : InventoryClickHandler() {
    override fun handle(clickedItem: ItemStack?, player: HumanEntity, event: InventoryClickEvent) {
        event.isCancelled = true

        val item = clickedItem ?: return

        var type = item.type
        if (item == InventoryUtil.UI.ITEMS.AIR_ITEM) type = Material.AIR

        HandlerInventoryEditFillClick.addOption(event.whoClicked, type)
    }
}