package net.mcmerdith.monkeboy.util

import org.bukkit.ChatColor
import org.bukkit.command.CommandSender

object ChatUtil {
    fun error(player: CommandSender, msg: String) {
        player.sendMessage("${ChatColor.RED}$msg")
    }
    fun success(player: CommandSender, msg: String) {
        player.sendMessage("${ChatColor.GREEN}$msg")
    }
    fun royal(player: CommandSender, msg: String) {
        player.sendMessage("${ChatColor.GOLD}$msg")
    }
    fun info(player: CommandSender, msg: String) {
        player.sendMessage("${ChatColor.AQUA}$msg")
    }
}