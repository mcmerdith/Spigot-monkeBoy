package net.mcmerdith.monkeboy.inventory

import net.mcmerdith.monkeboy.util.InventoryUtil
import org.bukkit.Material
import org.bukkit.event.inventory.InventoryClickEvent

class HandlerSelectorEditFillInventoryClick : InventoryClickHandler() {
    override fun clickEvent(event: InventoryClickEvent) {
        event.isCancelled = true

        val item = event.currentItem

        if (item == null || item.type == Material.AIR) return

        var type = item.type
        if (item == InventoryUtil.UI.ITEMS.AIR_ITEM) type = Material.AIR

        HandlerEditFillInventoryClick.addOption(event.whoClicked, type)
    }
}