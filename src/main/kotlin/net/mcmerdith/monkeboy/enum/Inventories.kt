package net.mcmerdith.monkeboy.enum

import net.mcmerdith.monkeboy.inventory.*
import org.bukkit.ChatColor

enum class Inventories(private val invName: String, val handler: InventoryClickHandler?) {
    MENU("monke Menu", HandlerInventoryMenuClick()),
    PREFERENCES("Preferences", HandlerInventoryPreferencesClick()),
    SELECTOR("Select", null),
    TAKE("Take your block", null),
    FILL_SELECTOR("Fill", HandlerInventoryFillSelectorClick()),
    FILL_CONFIRM("Confirm Fill", HandlerInventoryFillConfirmClick()),
    EDIT_FILL("Edit Fill", HandlerInventoryEditFillClick()),
    EDIT_FILL_SELECT("Edit Fill", HandlerInventoryEditFillSelectorClick()),
    CLONE_CONFIRM("Confirm Clone", HandlerInventoryCloneConfirmClick());

    fun invName(): String {
        return "${ChatColor.BOLD}$invName"
    }
}