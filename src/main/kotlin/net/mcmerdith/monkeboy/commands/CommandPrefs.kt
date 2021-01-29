package net.mcmerdith.monkeboy.commands

import net.mcmerdith.monkeboy.util.InventoryUtil
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class CommandPrefs : TNCommand() {
    override fun executeCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Int {
        if (isConsole(sender)) return 0
        sender as Player

        InventoryUtil.open(sender, InventoryUtil.getPreferencesMenu(sender))
        return 2
    }
}