package net.mcmerdith.monkeboy.util

import net.mcmerdith.monkeboy.Main
import net.mcmerdith.monkeboy.config.UserPrefsConfig
import net.mcmerdith.monkeboy.enum.Inventories
import net.mcmerdith.monkeboy.inventory.ScrollableInventory
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.HumanEntity
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

object InventoryUtil {
    object UI {
        object NAV {
            val FORWARD = newGUIItem(Material.OAK_SIGN, "Next", "Next")
            val BACK = newGUIItem(Material.OAK_SIGN, "Back", "Back")
            val HOME = newGUIItem(Material.OAK_DOOR, "Home", "Home")
        }

        object MENU {
            val FILL = newGUIItem(Material.STONE, "Fill", "Fill an Area")
            val CLEAR = newGUIItem(Material.BARRIER, "Clear", "Clear an Area")
            val PREFS = newGUIItem(Material.PAPER, "User Preferences", "Preferences")
            val CLONE = newGUIItem(Material.CHISELED_POLISHED_BLACKSTONE, "Clone", "Clone an Area")
        }

        object PREFS {
            object FILL_WARNINGS {
                val ENABLED = newGUIItem(Material.LIME_CONCRETE, "Fill Area Warnings", "Enabled")
                val DISABLED = newGUIItem(Material.RED_CONCRETE, "Fill Area Warnings", listOf("Disabled", "Editing a fill job is also disabled"))

                fun enabled(enabled: Boolean): ItemStack {
                    return if (enabled) ENABLED else DISABLED
                }
            }
        }

        object EDIT_FILL {
            val ADD_EXCLUDE = newGUIItem(Material.OAK_SIGN, "Excluded Blocks", listOf("Click to add", "Replace everything NOT these blocks", "Exclusion is overriden by inclusion"))
            val ADD_INCLUDE = newGUIItem(Material.OAK_SIGN, "Matched Blocks", listOf("Click to add", "Replace ONLY these blocks", "If present, excluded blocks will be ignored"))
            val EXECUTE = newGUIItem(Material.COMMAND_BLOCK, "Execute", "Run the fill command now")
            val MASK_AND_EXECUTE = newGUIItem(Material.CHAIN_COMMAND_BLOCK, "Mask and Execute", "Edit the fill area's block masking")
            val PREVIEW = newGUIItem(Material.ENDER_EYE, "View Area", "Close the GUI and preview the area that will be filled")
            val CANCEL = newGUIItem(Material.BARRIER, "Cancel", "Cancel the fill job")
        }

        object CLONE {
            val EXECUTE = newGUIItem(Material.COMMAND_BLOCK, "Execute", "Clone the area")
            val PREVIEW = newGUIItem(Material.ENDER_EYE, "View Area", "Close the GUI and preview the area that will be cloned")
            val CANCEL = newGUIItem(Material.BARRIER, "Cancel", "Cancel the clone job")
        }

        object ITEMS {
            val CLEAR = newGUIItem(Material.BARRIER, "Clear Area", "AIR")
            val CLONE = newGUIItem(Material.CHISELED_POLISHED_BLACKSTONE, "Clone", "CLONE")
            val AIR_ITEM = newGUIItem(Material.BARRIER, "Air", "AIR")
            val AIR_ITEM_EXCLUDE = newGUIItem(Material.BARRIER, "Air", "EXCLUDE:AIR")
            val AIR_ITEM_INCLUDE = newGUIItem(Material.BARRIER, "Air", "INCLUDE:AIR")
        }
    }

    val scrollables = mutableMapOf<HumanEntity, ScrollableInventory>()

    /* SPECIFIC INVENTORIES */

    fun getMenu(): Inventory {
        val menu = newInv(4, Inventories.MENU.invName())
        setItem(4, 1, menu, UI.MENU.FILL)
        setItem(4, 2, menu, UI.MENU.CLEAR)
        setItem(1, 1, menu, UI.MENU.PREFS)
        setItem(7,1, menu, UI.MENU.CLONE)
        return menu
    }

    fun getTakeMenu(item: ItemStack) = getTakeMenu(listOf(item))
    fun getTakeMenu(items: List<ItemStack>): Inventory {
        val current = if (items.size > 9) items.subList(0, 9) else items
        val startIndex = (9 - current.size) / 2
        val inventory = newInv(1, Inventories.TAKE.invName())
        current.forEachIndexed { index, itemStack ->
            inventory.setItem(index + startIndex, itemStack)
        }

        return inventory
    }

    fun getPreferencesMenu(player: HumanEntity): Inventory {
        val inv = newInv(5, Inventories.PREFERENCES.invName())
        val fillWarnings = UserPrefsConfig.config.getBoolean("${player.uniqueId}.fillwarnings", true)
        setItem(1, 1, inv, UI.PREFS.FILL_WARNINGS.enabled(fillWarnings))

        return inv
    }

    fun getFillEdit(options: List<BlockUtil.FillOption>): Inventory {
        val exclude = options.filter { it.type == BlockUtil.FillOption.Type.NOT }.chunked(8).getOrNull(0) ?: listOf()
        val include = options.filter { it.type == BlockUtil.FillOption.Type.IF }.chunked(8).getOrNull(0) ?: listOf()

        val inv = newInv(4, Inventories.EDIT_FILL.invName())
        setItem(0, 0, inv, UI.EDIT_FILL.ADD_EXCLUDE)
        setItem(0, 1, inv, UI.EDIT_FILL.ADD_INCLUDE)
        setItem(4, 3, inv, UI.EDIT_FILL.EXECUTE)

        exclude.forEachIndexed { i,v ->
            setItem(1 + i, 0, inv, if (v.mat == Material.AIR) UI.ITEMS.AIR_ITEM_EXCLUDE else newGUIItem(v.mat, ItemUtil.getName(v.mat), v.toString()))
        }

        include.forEachIndexed { i,v ->
            setItem(1 + i, 1, inv, if (v.mat == Material.AIR) UI.ITEMS.AIR_ITEM_INCLUDE else newGUIItem(v.mat, ItemUtil.getName(v.mat), v.toString()))
        }

        return inv
    }

    fun getFillComplete(): Inventory {
        val inv = newInv(1, Inventories.FILL_CONFIRM.invName())

        setItem(3, 0, inv, UI.EDIT_FILL.EXECUTE)
        setItem(4, 0, inv, UI.EDIT_FILL.PREVIEW)
        setItem(5, 0, inv, UI.EDIT_FILL.MASK_AND_EXECUTE)
        setItem(8, 0, inv, UI.EDIT_FILL.CANCEL)

        return inv
    }

    fun getCloneComplete(): Inventory {
        val inv = newInv(1, Inventories.CLONE_CONFIRM.invName())

        setItem(3, 0, inv, UI.CLONE.EXECUTE)
        setItem(4, 0, inv, UI.CLONE.PREVIEW)
        setItem(5, 0, inv, UI.CLONE.CANCEL)

        return inv
    }

    fun getFillBlockSelector(name: String?): ScrollableInventory = getSelector(ItemUtil.SOLID_BLOCKS, name)
    fun getBlockSelector(name: String?): ScrollableInventory = getSelector(ItemUtil.BLOCKS, name)
    fun getItemSelector(name: String?): ScrollableInventory = getSelector(ItemUtil.ITEMS, name)
    fun getAllSelector(name: String?): ScrollableInventory = getSelector(ItemUtil.ALL_ITEMS, name)

    fun getSelector(items: List<Material>, name: String? = null): ScrollableInventory {
        val finalInv = ScrollableInventory()
        var currentInv: Inventory? = null

        items.forEachIndexed { i: Int, material: Material ->
            if (i % 36 == 0) {
                if (i != 0) finalInv.addInventory(currentInv!!)
                currentInv = newInv(4, "${name?.plus(" ")}${Inventories.SELECTOR.invName()}", true)
            }

            currentInv?.addItem(if (material == Material.AIR) UI.ITEMS.AIR_ITEM else ItemStack(material))
        }

        return finalInv
    }

    /* UTIL INVENTORY FUNCTIONS */

    fun newInv(rowsRaw: Int, name: String, scrollable: Boolean = false): Inventory {
        val hasHome = !name.startsWith(Inventories.MENU.invName())
        val rows = (rowsRaw + (if (scrollable || hasHome) 1 else 0)).coerceIn(0..6)

        val inv = Bukkit.createInventory(null, rows * 9, name)

        if (scrollable) {
            setItem(0, rows - 1, inv, UI.NAV.BACK)
            setItem(8, rows - 1, inv, UI.NAV.FORWARD)
        }

        if (hasHome) {
            setItem(4, rows - 1, inv, UI.NAV.HOME)
        }

        return inv
    }

    fun newGUIItem(mat: Material, name: String, identifier: String? = null) =
            newGUIItem(mat, name, if (identifier == null) listOf() else listOf(identifier))

    fun newGUIItem(mat: Material, name: String, lore: List<String>? = null): ItemStack {
        val item = ItemStack(mat)
        val meta = item.itemMeta
        meta?.setDisplayName("${ChatColor.GOLD}$name")
        meta?.lore = lore
        if (meta == null) println("$mat $name $lore")
        item.itemMeta = meta
        item.addUnsafeEnchantment(Enchantment.DURABILITY, 1)

        return item
    }

    fun setItem(column: Int, row: Int, inventory: Inventory, item: ItemStack) {
        inventory.setItem((row.coerceIn(0..5) * 9) + column.coerceIn(0..8), item)
    }

    fun isItemGUIItem(item: ItemStack, gui: ItemStack): Boolean {
        if (!item.containsEnchantment(Enchantment.DURABILITY)) return false
        if (item.itemMeta?.displayName != gui.itemMeta?.displayName) return false

        return true
    }

    /* PLAYER FUNCTIONS */

    fun open(player: HumanEntity, inventory: Inventory?) {
        if (inventory == null) {
            close(player)
            return
        }

        Bukkit.getScheduler().runTask(Main.getInstance(), Runnable {
            player.openInventory(inventory)
        })
    }

    fun close(player: HumanEntity) {
        Bukkit.getScheduler().runTask(Main.getInstance(), Runnable {
            player.closeInventory()
        })
    }
}