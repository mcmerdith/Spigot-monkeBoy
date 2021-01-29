package net.mcmerdith.monkeboy.enum

import org.bukkit.ChatColor

enum class Inventories(val invName: String) {
    MENU("monke Menu"),
    PREFERENCES("Preferences"),
    SELECTOR("Select"),
    FILL("Fill"),
    FILLEDIT("Edit Fill"),
    FILLEDIT_SELECT("FE"),
    FILLCOMPLETE("FC"),
    TAKE("Take your block");

    fun invName(): String {
        return "${ChatColor.BOLD}$invName"
    }
}