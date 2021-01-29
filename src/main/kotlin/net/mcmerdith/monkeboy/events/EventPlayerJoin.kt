package net.mcmerdith.monkeboy.events

import net.mcmerdith.monkeboy.Main
import net.mcmerdith.monkeboy.util.ChatUtil
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

class EventPlayerJoin : Listener {
    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        val cfg = Main.getInstance().config
        if(cfg.getBoolean("welcome.show")) {
            cfg.getStringList("welcome.message").forEach {
                ChatUtil.royal(event.player, it)
            }
        }

    }
}