package world.cepi.mounts

import kotlinx.serialization.Serializable
import net.minestom.server.coordinate.Point
import net.minestom.server.coordinate.Vec
import net.minestom.server.entity.Entity
import net.minestom.server.entity.Player
import net.minestom.server.event.player.PlayerEntityInteractEvent
import net.minestom.server.instance.Instance
import net.minestom.server.tag.Tag
import world.cepi.kstom.event.listenOnly
import world.cepi.kstom.item.and
import world.cepi.kstom.item.get
import world.cepi.kstom.item.set
import world.cepi.kstom.item.withMeta
import world.cepi.mob.mob.Mob

@Serializable
data class Mount(
    val speed: Double = 1.0,
    val jumpHeight: Double = 1.0,
    val canControl: Boolean = true,
    val canDismount: Boolean = true
) {
    companion object {
        const val key = "mount"
    }

    fun generateEgg(mob: Mob = Mob()) = mob.generateEgg().and {
        withMeta {
            this[Tag.Byte("noSpawn")] = 1

            this[key] = this@Mount
        }
    }


    fun spawn(mob: Mob, instance: Instance, position: Point = Vec.ZERO) {
        val generatedMob = mob.generateMob()!!

        generatedMob.eventNode.listenOnly<PlayerEntityInteractEvent> {
            MountHook.hookInteract(this, this@Mount)
        }

        generatedMob.mob.setTag(Tag.Byte("isMount"), 1)

        generatedMob.mob.setInstance(instance, position)
    }

}

val Entity.isMount get() = getTag(Tag.Byte("isMount")) == 1.toByte()

val Player.heldMount: Mount?
    get() = this.itemInMainHand.meta.get(Mount.key)