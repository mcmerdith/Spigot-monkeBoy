package net.mcmerdith.monkeboy.inventory

import net.mcmerdith.monkeboy.util.InventoryUtil
import org.bukkit.event.inventory.InventoryClickEvent

class HandlerScrollableInventory : InventoryClickHandler() {
    override fun clickEvent(event: InventoryClickEvent) {
        when (event.currentItem) {
            InventoryUtil.UI.NAV.BACK -> {
                event.isCancelled = true
                InventoryUtil.scrollables[event.whoClicked]?.prevInv()
            }
            InventoryUtil.UI.NAV.FORWARD -> {
                event.isCancelled = true
                InventoryUtil.scrollables[event.whoClicked]?.nextInv()
            }
            InventoryUtil.UI.NAV.HOME -> {
                event.isCancelled = true
                InventoryUtil.open(event.whoClicked, InventoryUtil.getMenu())
            }
        }
    }
}