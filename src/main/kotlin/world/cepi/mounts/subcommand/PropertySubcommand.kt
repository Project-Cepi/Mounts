package world.cepi.mounts.subcommand

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.minestom.server.command.builder.arguments.Argument
import world.cepi.kepi.messages.sendFormattedTranslatableMessage
import world.cepi.kstom.command.kommand.Kommand
import world.cepi.mob.mob.mobEgg
import world.cepi.mounts.Mount
import world.cepi.mounts.heldMount

class PropertySubcommand<T>(val name: String, val arg: Argument<T>, val applyProperty: Mount.(T) -> Mount) : Kommand({
    syntax(arg) {
        player.heldMount?.let {
            player.itemInMainHand = applyProperty(it, !arg).generateEgg(player.mobEgg!!)
            player.sendFormattedTranslatableMessage("mount", "property.set",
                Component.text(name, NamedTextColor.BLUE),
                Component.text((!arg).toString(), NamedTextColor.BLUE)
            )
        }
    }
}, name)