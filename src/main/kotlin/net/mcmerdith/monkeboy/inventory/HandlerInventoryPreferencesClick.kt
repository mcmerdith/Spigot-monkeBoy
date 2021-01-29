package net.mcmerdith.monkeboy.inventory

import net.mcmerdith.monkeboy.config.UserPrefsConfig
import net.mcmerdith.monkeboy.enum.ConfigKeys
import net.mcmerdith.monkeboy.util.InventoryUtil
import org.bukkit.entity.HumanEntity
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack

class HandlerInventoryPreferencesClick : InventoryClickHandler() {
    override fun handle(clickedItem: ItemStack?, player: HumanEntity, event: InventoryClickEvent) {
        event.isCancelled = true

        val fillwarnings = ConfigKeys.PREF_FILLWARNING.pass(listOf(event.whoClicked.uniqueId.toString()))

        when (event.currentItem) {
            InventoryUtil.UI.PREFS.FILL_WARNINGS.ENABLED,
            InventoryUtil.UI.PREFS.FILL_WARNINGS.DISABLED -> {
                val config = UserPrefsConfig.config
                val state = !(config.getBoolean(fillwarnings, true))
                config.set(fillwarnings, state)
                UserPrefsConfig.save()

                InventoryUtil.open(event.whoClicked, InventoryUtil.getPreferencesMenu(event.whoClicked))
            }
        }
    }
}