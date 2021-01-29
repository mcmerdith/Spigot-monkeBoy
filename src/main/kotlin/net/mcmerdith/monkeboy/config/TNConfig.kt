package net.mcmerdith.monkeboy.config

import net.mcmerdith.monkeboy.Main
import org.bukkit.configuration.InvalidConfigurationException
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File
import java.io.IOException

abstract class TNConfig(val name: String) {
    lateinit var file: File
    lateinit var config: YamlConfiguration

    fun initialize() {
        val plugin = Main.getInstance()

        file = File(plugin.dataFolder, "$name.yml")
        if (!file.exists()) {
            file.parentFile.mkdirs()
            plugin.saveResource("$name.yml", false)
        }

        config = YamlConfiguration()

        try {
            config.load(file)
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: InvalidConfigurationException) {
            e.printStackTrace()
        }
    }

    fun save() {
        config.save(file)
    }
}