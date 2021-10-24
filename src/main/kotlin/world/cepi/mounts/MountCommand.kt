package world.cepi.mounts

import net.minestom.server.tag.Tag
import world.cepi.kstom.command.arguments.literal
import world.cepi.kstom.command.kommand.Kommand
import world.cepi.kstom.item.and
import world.cepi.kstom.item.withMeta
import world.cepi.mob.util.MobUtils
import world.cepi.kstom.item.set

object MountCommand : Kommand({
    val create by literal

    syntax(create).onlyPlayers {
        // Player needs a mob egg
        if (!MobUtils.hasMobEgg(sender)) return@onlyPlayers

        val mount = Mount()

        player.itemInMainHand = player.itemInMainHand.and {
            withMeta {
                this[Tag.Byte("noSpawn")] = 1

                this[Mount.key] = mount
            }
        }
    }
}, "mount")