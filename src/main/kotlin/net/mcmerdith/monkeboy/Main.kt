package net.mcmerdith.monkeboy

import net.mcmerdith.monkeboy.commands.*
import net.mcmerdith.monkeboy.config.NetworkConfig
import net.mcmerdith.monkeboy.config.UserPrefsConfig
import net.mcmerdith.monkeboy.events.EventBlockPlace
import net.mcmerdith.monkeboy.events.EventFill
import net.mcmerdith.monkeboy.events.EventInventoryClick
import net.mcmerdith.monkeboy.events.EventPlayerJoin
import net.mcmerdith.monkeboy.util.ItemUtil
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin

class Main : JavaPlugin() {
    companion object {
        private var INSTANCE: Main? = null
        fun getInstance() = INSTANCE!!
    }

    override fun onDisable() {
        logger.info("Disabled")
    }

    override fun onEnable() {
        INSTANCE = this

        config()

        ItemUtil.initialize()

        registerCommands()
        registerEvents()

        logger.info("Enabled")
    }

    fun config() {
        NetworkConfig.initialize()
        UserPrefsConfig.initialize()

        this.saveDefaultConfig()
    }

    fun registerCommands() {
        registerCommand("menu", CommandMenu())
        registerCommand("preferences", CommandPrefs())
        registerCommand("fillitem", CommandFillItem())
        registerCommand("fillconfirm", CommandFillConfirm())
    }

    fun registerEvents() {
        registerEvent(EventBlockPlace())
        registerEvent(EventInventoryClick())
        registerEvent(EventFill())
        registerEvent(EventPlayerJoin())
    }

    fun registerCommand(name: String, handler: TNCommand) {
        val command = getCommand(name)
        command?.setExecutor(handler)
        command?.tabCompleter = handler
    }

    fun registerEvent(event: Listener) {
        server.pluginManager.registerEvents(event, this)
    }
}