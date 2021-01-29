package net.mcmerdith.monkeboy.entity

import net.minecraft.server.v1_16_R2.*
import org.bukkit.Location
import org.bukkit.craftbukkit.v1_16_R2.CraftWorld

class Townsperson(spawn: Location) : EntityVillager(EntityTypes.VILLAGER, (spawn.world as CraftWorld?)!!.handle) {
    lateinit var bed: BlockPosition

    init {
        setPosition(spawn.x, spawn.y, spawn.z)
        customName = ChatComponentText("Townsperson")
        customNameVisible = true
        health = 20.0f
    }

    override fun initPathfinder() {
        goalSelector.a(0, PathfinderGoalFloat(this))
        goalSelector.a(1, PathfinderGoalAvoidTarget(this, EntityZombie::class.java, 8.0f, 0.5, 0.5))
        goalSelector.a(1, PathfinderGoalAvoidTarget(this, EntityEvoker::class.java, 12.0f, 0.5, 0.5))
        goalSelector.a(1, PathfinderGoalAvoidTarget(this, EntityVindicator::class.java, 8.0f, 0.5, 0.5))
        goalSelector.a(1, PathfinderGoalAvoidTarget(this, EntityVex::class.java, 8.0f, 0.5, 0.5))
        goalSelector.a(1, PathfinderGoalAvoidTarget(this, EntityPillager::class.java, 15.0f, 0.5, 0.5))
        goalSelector.a(1, PathfinderGoalAvoidTarget(this, EntityIllagerIllusioner::class.java, 12.0f, 0.5, 0.5))
        goalSelector.a(1, PathfinderGoalAvoidTarget(this, EntityZoglin::class.java, 10.0f, 0.5, 0.5))
        goalSelector.a(1, PathfinderGoalPanic(this, 0.5))
        goalSelector.a(4, PathfinderGoalMoveTowardsRestriction(this, 0.35))
        goalSelector.a(8, PathfinderGoalRandomStrollLand(this, 0.35))
        goalSelector.a(9, PathfinderGoalInteract(this, EntityVillager::class.java, 3.0f, 1.0f))
        goalSelector.a(10, PathfinderGoalLookAtPlayer(this, EntityInsentient::class.java, 8.0f))
    }

    override fun prepare(worldaccess: WorldAccess?, difficultydamagescaler: DifficultyDamageScaler?, enummobspawn: EnumMobSpawn?, groupdataentity: GroupDataEntity?, nbttagcompound: NBTTagCompound?): GroupDataEntity? {
        val gde = super.prepare(worldaccess, difficultydamagescaler, enummobspawn, groupdataentity, nbttagcompound)

        this.setSlot(EnumItemSlot.MAINHAND, ItemStack(Items.DIAMOND_SWORD))
        return gde
    }

    override fun saveData(nbttagcompound: NBTTagCompound) {
        super.saveData(nbttagcompound)
//        nbttagcompound.setInt("DespawnDelay", this.bq)
//        if (this.bp != null) {
//            nbttagcompound["WanderTarget"] = GameProfileSerializer.a(this.bp)
//        }
    }

    override fun loadData(nbttagcompound: NBTTagCompound) {
        super.loadData(nbttagcompound)
//        if (nbttagcompound.hasKeyOfType("DespawnDelay", 99)) {
//            this.bq = nbttagcompound.getInt("DespawnDelay")
//        }
//        if (nbttagcompound.hasKey("WanderTarget")) {
//            this.bp = GameProfileSerializer.b(nbttagcompound.getCompound("WanderTarget"))
//        }
//        setAgeRaw(Math.max(0, this.age))
    }
}