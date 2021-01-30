package net.mcmerdith.monkeboy.events

import net.mcmerdith.monkeboy.entity.Townsperson
import net.mcmerdith.monkeboy.inventory.HandlerInventoryEditFillClick
import net.mcmerdith.monkeboy.util.BlockUtil
import net.mcmerdith.monkeboy.util.ChatUtil
import net.mcmerdith.monkeboy.util.ItemUtil
import net.mcmerdith.monkeboy.util.Util
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.craftbukkit.v1_16_R2.CraftWorld
import org.bukkit.enchantments.Enchantment
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.persistence.PersistentDataType

class EventBlockPlace : Listener {
    @EventHandler
    fun onPlace(event: BlockPlaceEvent) {
        val player = event.player
        val block = event.block
        val location = block.location
        val type = block.type
        val item = event.player.inventory.itemInMainHand

        // TOWNSPERSON SPAWNING
        if (type == Material.GREEN_BED && item.itemMeta?.displayName == "Spawn Townsperson") {
            val townsperson = Townsperson(location)
            val server = (player.world as CraftWorld).handle
            server.addEntity(townsperson)
        }

        // SPECIAL ITEM LOGIC
        if (item.containsEnchantment(Enchantment.DURABILITY)) {
            event.isCancelled = true

            val meta = item.itemMeta
            val lore = meta?.lore

            // FILL BLOCK LOGIC
            val fillType = Material.matchMaterial(lore?.getOrNull(0) ?: "")
            if (fillType != null && lore != null) {
                if (player.isOp) {
                    val loc1 = Util.stringToLocation(lore.getOrNull(1) ?: "")

                    if (loc1 == null) {
                        player.inventory.setItemInMainHand(ItemUtil.getFillItem(fillType, location, previous = item))
                        ChatUtil.info(player, "Location Saved (${Util.locationToString(location)})")
                    } else {
                        player.inventory.setItemInMainHand(ItemUtil.getFillItem(fillType, matOverride = type))

                        val timestamp = meta.persistentDataContainer.get(ItemUtil.timestampKey, PersistentDataType.LONG)
                        if (timestamp != null && (System.currentTimeMillis() - timestamp) > 300000) {
                            ChatUtil.error(player, "WARNING: This data block is over 5 minutes old. For safety, the existing data has been cleared, and the fill has not been executed")
                            return
                        }

                        val area = BlockUtil.FillArea(loc1, location, fillType, player)
                        HandlerInventoryEditFillClick.register(event.player, area)
                        Bukkit.getPluginManager().callEvent(FillEvent(area))
                    }
                } else {
                    ChatUtil.error(player, "You don't have permission to do that")
                    return
                }
            }
        }
    }
}