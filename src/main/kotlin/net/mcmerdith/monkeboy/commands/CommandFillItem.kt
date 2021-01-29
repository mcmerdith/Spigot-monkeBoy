package net.mcmerdith.monkeboy.commands

import net.mcmerdith.monkeboy.util.ItemUtil
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class CommandFillItem : TNCommand() {
    override fun executeCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Int {
        if (isConsole(sender)) return 0
        sender as Player

        val matString = args.getOrNull(0) ?: run {
            sender.sendMessage("${ChatColor.RED}You must provide a block to fill")
            return 1
        }

        val material = Material.matchMaterial(matString) ?: run {
            sender.sendMessage("${ChatColor.RED}No block by '$matString'")
            return 1
        }

        sender.inventory.addItem(ItemUtil.getFillItem(material))
        sender.sendMessage("${ChatColor.GREEN}A fill block has been added to your inventory")
        return 2
    }
}