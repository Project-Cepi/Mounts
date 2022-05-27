package world.cepi.mounts

import net.minestom.server.command.builder.arguments.ArgumentType
import world.cepi.kepi.command.subcommand.applyHelp
import world.cepi.kepi.messages.sendFormattedTranslatableMessage
import world.cepi.kstom.command.arguments.literal
import world.cepi.kstom.command.kommand.Kommand
import world.cepi.mob.mob.mobEgg
import world.cepi.mounts.subcommand.PropertySubcommand

object MountCommand : Kommand({
    val create by literal
    val forceDismount by literal

    applyHelp {
        """
            The mount system allows you to create ridable <blue>mobs.
            
            To create a mount, hold no item or an item with a mob.
            
            You can modify the mob with the various other subcommands.
        """.trimIndent()
    }

    syntax(create) {
        player.itemInMainHand = if (player.mobEgg != null) player.itemInMainHand else Mount().generateEgg()
        player.sendFormattedTranslatableMessage("mount", "create")
    }.onlyPlayers()

    syntax(forceDismount) {
        player.vehicle?.removePassenger(player)
    }.onlyPlayers()

    addSubcommands(
        PropertySubcommand("speed", ArgumentType.Double("speed")) { copy(speed = it) },
        PropertySubcommand("jumpHeight", ArgumentType.Double("jumpHeight")) { copy(jumpHeight = it) },
        PropertySubcommand("canControl", ArgumentType.Boolean("canControl")) { copy(canControl = it)},
        PropertySubcommand("canDismount", ArgumentType.Boolean("canDismount")) { copy(canDismount = it) }
    )
}, "mount")