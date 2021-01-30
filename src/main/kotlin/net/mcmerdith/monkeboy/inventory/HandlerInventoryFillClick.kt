package net.mcmerdith.monkeboy.inventory

import net.mcmerdith.monkeboy.util.InventoryUtil
import net.mcmerdith.monkeboy.util.ItemUtil
import org.bukkit.Material
import org.bukkit.entity.HumanEntity
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack

class HandlerInventoryFillClick : InventoryClickHandler() {
    override fun handle(clickedItem: ItemStack?, player: HumanEntity, event: InventoryClickEvent) {
        event.isCancelled = true

        clickedItem ?: return
        if (clickedItem.type == Material.AIR) return
        InventoryUtil.open(player, InventoryUtil.getTakeMenu(ItemUtil.getFillItem(clickedItem.type)))
    }
}