package world.cepi.mounts

import kotlinx.serialization.Serializable
import net.minestom.server.coordinate.Point
import net.minestom.server.coordinate.Vec
import net.minestom.server.entity.Entity
import net.minestom.server.entity.Player
import net.minestom.server.instance.Instance
import net.minestom.server.tag.Tag
import world.cepi.kstom.item.get
import world.cepi.mob.mob.Mob

@Serializable
class Mount() {

    companion object {
        const val key = "mount"
    }

    fun spawn(mob: Mob, instance: Instance, position: Point = Vec.ZERO) {
        val generatedMob = mob.generateMob()!!

        generatedMob.mob.setTag(Tag.Byte("isMount"), 1)

        generatedMob.mob.setInstance(instance, position)
    }

}

val Entity.isMount get() = getTag(Tag.Byte("isMount")) == 1.toByte()

val Player.heldMount: Mount?
    get() = this.itemInMainHand.meta.get(Mount.key)