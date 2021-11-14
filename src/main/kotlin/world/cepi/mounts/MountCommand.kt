package world.cepi.mounts

import net.minestom.server.command.builder.arguments.ArgumentType
import net.minestom.server.tag.Tag
import world.cepi.kepi.messages.sendFormattedTranslatableMessage
import world.cepi.kstom.command.arguments.literal
import world.cepi.kstom.command.kommand.Kommand
import world.cepi.kstom.item.and
import world.cepi.kstom.item.withMeta
import world.cepi.kstom.item.set
import world.cepi.mob.mob.Mob
import world.cepi.mob.mob.mobEgg
import world.cepi.mounts.subcommand.PropertySubcommand

object MountCommand : Kommand({
    val create by literal

    syntax(create).onlyPlayers {
        player.itemInMainHand = if (player.mobEgg != null) player.itemInMainHand else Mount().generateEgg()
        player.sendFormattedTranslatableMessage("mount", "create")
    }

    addSubcommands(
        PropertySubcommand("speed", ArgumentType.Double("speed"), Mount::withSpeed),
        PropertySubcommand("jumpHeight", ArgumentType.Double("jumpHeight"), Mount::withJumpHeight)
    )
}, "mount")