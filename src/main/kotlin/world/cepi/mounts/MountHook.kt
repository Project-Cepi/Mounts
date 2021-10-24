package world.cepi.mounts

import net.minestom.server.coordinate.Point
import net.minestom.server.entity.Player
import net.minestom.server.event.EventFilter
import net.minestom.server.event.EventNode
import net.minestom.server.event.player.*
import net.minestom.server.network.packet.client.play.ClientSteerVehiclePacket
import world.cepi.kstom.event.listen
import world.cepi.kstom.event.listenOnly
import world.cepi.mob.mob.mobEgg
import kotlin.experimental.and

object MountHook {

    val playerVehicleNode = EventNode.type("mountNode", EventFilter.PLAYER)

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

        playerVehicleNode.listen<PlayerPacketEvent> {
            removeWhen {
                player.vehicle == null
            }
            handler {
                if (packet !is ClientSteerVehiclePacket) return@handler

                val steerPacket = packet as ClientSteerVehiclePacket
                val vehicle = player.vehicle!!

                if ((steerPacket.flags and 0x02) == 0x02.toByte()) {
                    player.vehicle?.removePassenger(player)
                    return@handler
                }

                if (steerPacket.flags and 0x01 == 0x01.toByte() && vehicle.isOnGround) {
                    vehicle.velocity = vehicle.velocity.add(.0, 1.0, 0.0)
                }

                vehicle.velocity = vehicle.velocity.add(player.position.direction().normalize().mul(steerPacket.forward.toDouble()).withY(.0))

            }
        }
    }
}