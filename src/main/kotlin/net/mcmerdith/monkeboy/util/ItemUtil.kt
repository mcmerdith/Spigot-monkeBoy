package net.mcmerdith.monkeboy.util

import net.mcmerdith.monkeboy.Main
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

object ItemUtil {
    val BLOCKS = mutableListOf<Material>()
    val SOLID_BLOCKS = mutableListOf<Material>()
    val ITEMS = mutableListOf<Material>()
    val ALL_ITEMS = mutableListOf<Material>()

    lateinit var timestampKey: NamespacedKey

    fun initialize() {
        this.timestampKey = NamespacedKey(Main.getInstance(), "fill-timestamp")

        Material.values().forEach {
            if (it.isBlock) {
                if (it.isSolid) {
                    SOLID_BLOCKS.add(it)
                }
                BLOCKS.add(it)
            } else if (it.isItem) {
                ITEMS.add(it)
            }

            ALL_ITEMS.add(it)
        }
    }

    fun getName(item: Material) = getName(ItemStack(item))
    fun getName(item: ItemStack?): String {
        val dn = item?.itemMeta?.displayName ?: ""

        return if (dn == "") {
            item?.type?.name?.capitalize()?.replace("_", " ") ?: "Nothing"
        } else {
            dn
        }
    }

    fun setLore(item: ItemStack, lore: String) = setLore(item, listOf(lore))
    fun setLore(item: ItemStack, lore: List<String>): ItemStack {
        val newItem = ItemStack(item)
        val meta = newItem.itemMeta
        meta?.lore = lore
        newItem.itemMeta = meta

        return newItem
    }

    fun appendLore(item: ItemStack, lore: String) = appendLore(item, listOf(lore))
    fun appendLore(item: ItemStack, lore: List<String>): ItemStack {
        val newItem = ItemStack(item)

        val meta = newItem.itemMeta
        val itemLore = meta?.lore
        itemLore?.addAll(lore)
        meta?.lore = itemLore

        newItem.itemMeta = meta

        return newItem
    }

    fun getFillItem(mat: Material, first: Location? = null, second: Location? = null, previous: ItemStack? = null, matOverride: Material? = null): ItemStack {
        val item = ItemStack(previous ?: InventoryUtil.newGUIItem(matOverride ?: mat, "Fill Area", mat.name))

        // Attach a timestamp
        val meta = item.itemMeta
        meta?.persistentDataContainer?.set(timestampKey, PersistentDataType.LONG, System.currentTimeMillis())
        item.itemMeta = meta

        if (first == null) return item
        if (second == null) return appendLore(item, Util.locationToString(first))
        return appendLore(item, listOf(Util.locationToString(first), Util.locationToString(second)))
    }
}
