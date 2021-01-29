package net.mcmerdith.monkeboy.inventory

import net.mcmerdith.monkeboy.util.InventoryUtil
import net.mcmerdith.monkeboy.util.ItemUtil
import org.bukkit.Material
import org.bukkit.event.inventory.InventoryClickEvent

class HandlerFillInventoryClick : InventoryClickHandler() {
    override fun clickEvent(event: InventoryClickEvent) {
        event.isCancelled = true

        val item = event.currentItem ?: return
        if (item.type == Material.AIR) return
        InventoryUtil.open(event.whoClicked, InventoryUtil.getTakeMenu(ItemUtil.getFillItem(item.type)))
    }
}