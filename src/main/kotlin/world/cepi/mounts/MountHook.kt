package world.cepi.mounts

import net.kyori.adventure.text.Component
import net.minestom.server.adventure.audience.Audiences
import net.minestom.server.coordinate.Point
import net.minestom.server.coordinate.Vec
import net.minestom.server.entity.Player
import net.minestom.server.event.EventFilter
import net.minestom.server.event.EventNode
import net.minestom.server.event.player.*
import net.minestom.server.network.packet.client.play.ClientSteerVehiclePacket
import world.cepi.kstom.event.listen
import world.cepi.kstom.event.listenOnly
import world.cepi.mob.events.MobSpawnHook.hookInteract
import world.cepi.mob.mob.mobEgg
import kotlin.experimental.and
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin

object MountHook {

    val playerVehicleNode = EventNode.type("mountNode", EventFilter.PLAYER)

    private fun spawnHook(player: Player, position: Point) {
        player.heldMount ?: return

        player.heldMount!!.spawn(player.mobEgg!!, player.instance!!, position)
    }

    // todo if anyone knows trig i havent taken it yet someone please simplify this, or if its geometry i forgot it
    fun yawToVec(yaw: Float) = Vec(cos(Math.toRadians((yaw + 90).toDouble())), 0.0, sin(Math.toRadians((yaw + 90).toDouble())))

    fun hookUseOnBlock(event: PlayerUseItemOnBlockEvent) =
        spawnHook(event.player, event.position.add(.0, 1.0, .0))

    fun hookInteract(event: PlayerEntityInteractEvent, mount: Mount) {
        if (event.target.hasPassenger()) return

        event.target.addPassenger(event.player)

        playerVehicleNode.listen<PlayerPacketEvent> {
            val vehicleUUID = event.player.vehicle!!.uuid
            removeWhen {
                player.vehicle?.uuid != vehicleUUID || player.isRemoved
            }
            filters += { packet is ClientSteerVehiclePacket }
            handler {
                val steerPacket = packet as ClientSteerVehiclePacket
                val vehicle = player.vehicle!!

                if ((steerPacket.flags and 0x02) == 0x02.toByte()) {
                    player.vehicle?.removePassenger(player)
                    return@handler
                }

                if (steerPacket.flags and 0x01 == 0x01.toByte() && vehicle.isOnGround) {
                    vehicle.velocity = vehicle.velocity.withY(mount.jumpHeight)
                }

                vehicle.setView(player.position.yaw(), 0f)
                vehicle.velocity = vehicle.velocity
                    .add(yawToVec(player.position.yaw()).mul(steerPacket.forward.toDouble()).mul(mount.speed))
                    .add(yawToVec(player.position.yaw() + (if (steerPacket.sideways > 0) -90 else 90)).mul(abs(steerPacket.sideways.toDouble())).mul(mount.speed))

            }
        }
    }
}