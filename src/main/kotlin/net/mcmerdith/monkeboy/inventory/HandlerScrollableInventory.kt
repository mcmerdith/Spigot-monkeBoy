package net.mcmerdith.monkeboy.inventory

import net.mcmerdith.monkeboy.util.InventoryUtil
import org.bukkit.entity.HumanEntity
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack

class HandlerScrollableInventory : InventoryClickHandler() {
    override fun handle(clickedItem: ItemStack?, player: HumanEntity, event: InventoryClickEvent) {
        when (clickedItem) {
            InventoryUtil.UI.NAV.BACK -> {
                event.isCancelled = true
                InventoryUtil.scrollables[player]?.prevInv()
            }
            InventoryUtil.UI.NAV.FORWARD -> {
                event.isCancelled = true
                InventoryUtil.scrollables[player]?.nextInv()
            }
            InventoryUtil.UI.NAV.HOME -> {
                event.isCancelled = true
                InventoryUtil.open(player, InventoryUtil.getMenu())
            }
        }
    }
}