package net.mcmerdith.monkeboy.inventory

import net.mcmerdith.monkeboy.events.CloneEvent
import net.mcmerdith.monkeboy.selection.CloneArea
import net.mcmerdith.monkeboy.util.InventoryUtil
import net.md_5.bungee.api.chat.ClickEvent
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.HumanEntity
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack

class HandlerInventoryCloneConfirmClick : InventoryClickHandler() {
    override fun handle(clickedItem: ItemStack?, player: HumanEntity, event: InventoryClickEvent) {
        event.isCancelled = true

        when (clickedItem) {
            InventoryUtil.UI.CLONE.EXECUTE -> {
                CloneArea.ALL[player]?.let {
                    Bukkit.getPluginManager().callEvent(CloneEvent(it, true))
                }
                InventoryUtil.close(player)
            }
            InventoryUtil.UI.CLONE.PREVIEW -> {
                InventoryUtil.close(player)

                val edit = TextComponent("${ChatColor.AQUA}[REOPEN DIALOG]")
                edit.clickEvent = ClickEvent(ClickEvent.Action.RUN_COMMAND, "/cloneconfirm")

                // Send the message
                player.spigot().sendMessage(edit)
            }
            InventoryUtil.UI.CLONE.CANCEL -> {
                CloneArea.ALL[player]?.expire()
                InventoryUtil.close(player)
            }
        }
    }
}