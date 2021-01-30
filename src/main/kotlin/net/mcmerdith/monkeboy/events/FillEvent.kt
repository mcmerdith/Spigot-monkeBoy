package net.mcmerdith.monkeboy.events

import net.mcmerdith.monkeboy.selection.FillArea
import org.bukkit.event.Cancellable
import org.bukkit.event.Event
import org.bukkit.event.HandlerList
import org.bukkit.util.Vector

class FillEvent(val area: FillArea, val warned: Boolean = false) : Event(), Cancellable {
    private var isCancelled = false

    override fun isCancelled(): Boolean {
        return isCancelled
    }

    override fun setCancelled(isCancelled: Boolean) {
        this.isCancelled = isCancelled
    }

    companion object {
        private val HANDLERS = HandlerList()
        @JvmStatic
        fun getHandlerList(): HandlerList {
            return HANDLERS
        }
    }

    override fun getHandlers(): HandlerList {
        return HANDLERS
    }

    fun getArea(): Int {
        val startVec = area.start.toVector()
        val finVec = area.finish.toVector()
        val vector = Vector.getMaximum(startVec, finVec).subtract(Vector.getMinimum(startVec, finVec)).add(Vector(1, 1, 1))
        return vector.blockX * vector.blockY * vector.blockZ
    }

    fun isOversized(): Boolean = getArea() > 32768
}