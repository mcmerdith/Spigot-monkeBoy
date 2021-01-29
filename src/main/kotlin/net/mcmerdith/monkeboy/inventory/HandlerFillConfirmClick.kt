package net.mcmerdith.monkeboy.inventory

import net.mcmerdith.monkeboy.events.FillEvent
import net.mcmerdith.monkeboy.util.InventoryUtil
import net.md_5.bungee.api.chat.ClickEvent
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.event.inventory.InventoryClickEvent

class HandlerFillConfirmClick : InventoryClickHandler() {
    override fun clickEvent(event: InventoryClickEvent) {
        event.isCancelled = true

        when (event.currentItem) {
            InventoryUtil.UI.EDIT_FILL.EXECUTE -> {
                HandlerEditFillInventoryClick.areas[event.whoClicked]?.let {
                    Bukkit.getPluginManager().callEvent(FillEvent(it, true))
                }
                InventoryUtil.close(event.whoClicked)
            }
            InventoryUtil.UI.EDIT_FILL.PREVIEW -> {
                InventoryUtil.close(event.whoClicked)

                val edit = TextComponent("${ChatColor.AQUA}[REOPEN DIALOG]")
                edit.clickEvent = ClickEvent(ClickEvent.Action.RUN_COMMAND, "/fillconfirm")

                // Send the message
                event.whoClicked.spigot().sendMessage(edit)
            }
            InventoryUtil.UI.EDIT_FILL.MASK_AND_EXECUTE -> {
                HandlerEditFillInventoryClick.areas[event.whoClicked]?.let {
                    InventoryUtil.open(event.whoClicked, InventoryUtil.getFillEdit(it.options))
                }
            }
        }
    }
}