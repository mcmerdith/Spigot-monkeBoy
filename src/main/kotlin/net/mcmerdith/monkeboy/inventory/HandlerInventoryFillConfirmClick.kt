package net.mcmerdith.monkeboy.inventory

import net.mcmerdith.monkeboy.events.FillEvent
import net.mcmerdith.monkeboy.util.InventoryUtil
import net.md_5.bungee.api.chat.ClickEvent
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.HumanEntity
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack

class HandlerInventoryFillConfirmClick : InventoryClickHandler() {
    override fun handle(clickedItem: ItemStack?, player: HumanEntity, event: InventoryClickEvent) {
        event.isCancelled = true

        when (clickedItem) {
            InventoryUtil.UI.EDIT_FILL.EXECUTE -> {
                HandlerInventoryEditFillClick.areas[player]?.let {
                    Bukkit.getPluginManager().callEvent(FillEvent(it, true))
                }
                InventoryUtil.close(player)
            }
            InventoryUtil.UI.EDIT_FILL.PREVIEW -> {
                InventoryUtil.close(player)

                val edit = TextComponent("${ChatColor.AQUA}[REOPEN DIALOG]")
                edit.clickEvent = ClickEvent(ClickEvent.Action.RUN_COMMAND, "/fillconfirm")

                // Send the message
                player.spigot().sendMessage(edit)
            }
            InventoryUtil.UI.EDIT_FILL.MASK_AND_EXECUTE -> {
                HandlerInventoryEditFillClick.areas[player]?.let {
                    InventoryUtil.open(player, InventoryUtil.getFillEdit(it.options))
                }
            }
            InventoryUtil.UI.EDIT_FILL.CANCEL -> {
                HandlerInventoryEditFillClick.areas[player]?.expire()
                InventoryUtil.close(player)
            }
        }
    }
}