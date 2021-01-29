package net.mcmerdith.monkeboy.inventory

import net.mcmerdith.monkeboy.util.InventoryUtil
import org.bukkit.entity.HumanEntity
import org.bukkit.inventory.Inventory

class ScrollableInventory(inventories: MutableList<Inventory>? = null) {
    lateinit var player: HumanEntity
    var current: Int = 0
    val invs = inventories ?: mutableListOf()

    fun addInventory(inventory: Inventory) {
        invs.add(inventory)
    }

    fun nextInv() {
        if (current < invs.size - 1) {
            current++
            show()
        }
    }

    fun prevInv() {
        if (current > 0) {
            current--
            show()
        }
    }

    fun show() {
        InventoryUtil.open(player, invs.getOrNull(current))
    }

    fun register(player: HumanEntity) {
        this.player = player
        InventoryUtil.scrollables[player] = this
    }
}