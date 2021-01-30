package net.mcmerdith.monkeboy.inventory

import net.mcmerdith.monkeboy.util.InventoryUtil
import org.bukkit.Material
import org.bukkit.entity.HumanEntity
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack

class HandlerInventoryEditFillSelectorClick : InventoryClickHandler() {
    override fun handle(clickedItem: ItemStack?, player: HumanEntity, event: InventoryClickEvent) {
        event.isCancelled = true

        clickedItem ?: return

        var type = clickedItem.type
        if (clickedItem == InventoryUtil.UI.ITEMS.AIR_ITEM) type = Material.AIR

        HandlerInventoryEditFillClick.addOption(player, type)
    }
}