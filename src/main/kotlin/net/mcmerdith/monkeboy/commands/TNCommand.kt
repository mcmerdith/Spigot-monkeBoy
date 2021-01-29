package net.mcmerdith.monkeboy.commands

import net.mcmerdith.monkeboy.Main
import net.mcmerdith.monkeboy.util.ChatUtil
import org.bukkit.Bukkit
import org.bukkit.command.*
import org.bukkit.entity.Player


abstract class TNCommand : CommandExecutor, TabCompleter {
    val completes = mutableMapOf<Int, List<String>>()

    fun addConditionalComplete(position: Int, complete: TNConditionalComplete) {}
    fun addComplete(position: Int, complete: List<String>) {
        val target = completes[position]?.toMutableList() ?: mutableListOf()
        target.addAll(complete)
        completes[position] = target.toList()
    }

    fun isConsole(sender: CommandSender): Boolean {
        return if (sender !is Player) {
            ChatUtil.error(sender, "You must be a player")
            true
        } else false
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        val result = executeCommand(sender, command, label, args)

        if (sender is BlockCommandSender) {
            val block = sender.block
            val x = block.x
            val y = block.y
            val z = block.z
            val server = sender.server

            val value = if (result == 2) 1 else 0
            Bukkit.getScheduler().runTaskLater(Main.getInstance(), Runnable {
                server.dispatchCommand(server.consoleSender, "data modify block $x $y $z SuccessCount set value $value")
            }, 1L)
        }

        println(result > 0)

        return result > 0
    }

    /***
     * @return 0: Invalid, 1: Fail, 2: Success
     */
    abstract fun executeCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Int

    override fun onTabComplete(sender: CommandSender, command: Command, label: String, args: Array<out String>): MutableList<String> {
        return completes[args.size - 1]?.toMutableList() ?: mutableListOf()
    }
}