package world.cepi.mounts

import net.kyori.adventure.text.Component
import net.minestom.server.adventure.audience.Audiences
import net.minestom.server.coordinate.Point
import net.minestom.server.entity.Player
import net.minestom.server.event.EventFilter
import net.minestom.server.event.EventNode
import net.minestom.server.event.player.*
import net.minestom.server.listener.PlayerVehicleListener
import net.minestom.server.network.packet.client.play.ClientSteerVehiclePacket
import net.minestom.server.network.packet.server.play.VehicleMovePacket
import world.cepi.kstom.event.listen
import world.cepi.kstom.event.listenOnly
import world.cepi.mob.events.MobSpawnHook.hook
import world.cepi.mob.mob.mobEgg
import kotlin.experimental.and

object MountHook {

    val playerDisconnectNode = EventNode.type("mountDisconnectNode", EventFilter.PLAYER)

    private fun spawnHook(player: Player, position: Point) {
        player.heldMount ?: return

        val generatedMob = player.mobEgg!!.generateMob()!!

        generatedMob.eventNode.listenOnly(::hookInteract)

        generatedMob.mob.setInstance(player.instance!!, position)
    }

    fun hookUseOnBlock(event: PlayerUseItemOnBlockEvent) =
        spawnHook(event.player, event.position.add(.0, 1.0, .0))

    private fun hookInteract(event: PlayerEntityInteractEvent) {
        event.target.addPassenger(event.player)

        playerDisconnectNode.listen<PlayerPacketEvent> {
            removeWhen {
                player.vehicle == null
            }
            handler {
                if (packet !is ClientSteerVehiclePacket) return@handler

                if (((packet as ClientSteerVehiclePacket).flags and 0x02) != 0x02.toByte()) return@handler
                player.vehicle?.removePassenger(player)

            }
        }
    }
}