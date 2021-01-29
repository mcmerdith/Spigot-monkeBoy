package net.mcmerdith.monkeboy.inventory

import net.mcmerdith.monkeboy.enum.Inventories
import net.mcmerdith.monkeboy.util.InventoryUtil
import org.bukkit.entity.HumanEntity
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack

class HandlerInventoryMenuClick : InventoryClickHandler() {
    override fun handle(clickedItem: ItemStack?, player: HumanEntity, event: InventoryClickEvent) {
        event.isCancelled = true

        when (event.currentItem) {
            InventoryUtil.UI.MENU.FILL -> {
                val fill = InventoryUtil.getFillBlockSelector(Inventories.FILL.invName())
                fill.register(event.whoClicked)
                fill.show()
            }
            InventoryUtil.UI.MENU.CLEAR -> {
                InventoryUtil.open(event.whoClicked, InventoryUtil.getTakeMenu(InventoryUtil.UI.ITEMS.CLEAR))
            }
            InventoryUtil.UI.MENU.PREFS -> {
                InventoryUtil.open(event.whoClicked, InventoryUtil.getPreferencesMenu(event.whoClicked))
            }
        }
    }
}