package net.mcmerdith.monkeboy.inventory

import net.mcmerdith.monkeboy.enum.Inventories
import net.mcmerdith.monkeboy.events.FillEvent
import net.mcmerdith.monkeboy.util.BlockUtil
import net.mcmerdith.monkeboy.util.InventoryUtil
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.HumanEntity
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack

class HandlerInventoryEditFillClick : InventoryClickHandler() {
    companion object {
        private val exclude = mutableMapOf<HumanEntity, Boolean>()
        val areas = mutableMapOf<HumanEntity, BlockUtil.FillArea>()
        fun register(player: HumanEntity, area: BlockUtil.FillArea) {
            areas[player] = area
        }
        fun addOption(player: HumanEntity, material: Material) {
            areas[player]?.options?.add(BlockUtil.FillOption(if (exclude[player] == true) BlockUtil.FillOption.Type.NOT else BlockUtil.FillOption.Type.IF, material))
            InventoryUtil.open(player, InventoryUtil.getFillEdit(areas[player]?.options ?: listOf()))
        }
        fun reset(player: HumanEntity) {
            exclude.remove(player)
            areas.remove(player)
        }
    }

    override fun handle(clickedItem: ItemStack?, player: HumanEntity, event: InventoryClickEvent) {
        event.isCancelled = true

        when (clickedItem) {
            InventoryUtil.UI.EDIT_FILL.ADD_EXCLUDE,
            InventoryUtil.UI.EDIT_FILL.ADD_INCLUDE -> {
                val inv = InventoryUtil.getBlockSelector(Inventories.EDIT_FILL_SELECT.invName())
                inv.register(player)
                inv.show()

                exclude[player] = clickedItem == InventoryUtil.UI.EDIT_FILL.ADD_EXCLUDE
            }
            InventoryUtil.UI.EDIT_FILL.EXECUTE -> {
                InventoryUtil.close(player)

                val area = areas[player] ?: return

                Bukkit.getPluginManager().callEvent(FillEvent(area, true))
            }
            else -> {
                val item = clickedItem ?: return
                val data = item.itemMeta?.lore?.get(0) ?: return
                val option = BlockUtil.FillOption.from(data) ?: return

                val opts = areas[player]?.options ?: return
                opts.removeIf { it.mat == option.mat && it.type == option.type }

                InventoryUtil.open(player, InventoryUtil.getFillEdit(opts))
            }
        }
    }
}