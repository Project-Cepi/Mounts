package world.cepi.mounts

import net.minestom.server.extensions.Extension;
import world.cepi.kstom.event.listenOnly
import world.cepi.kstom.util.log
import world.cepi.kstom.util.node

class MountsExtension : Extension() {

    override fun initialize(): LoadStatus {
        node.listenOnly(MountHook::hookUseOnBlock)
        node.addChild(MountHook.playerVehicleNode)
        MountCommand.register()

        log.info("[Mounts] has been enabled!")

        return LoadStatus.SUCCESS
    }

    override fun terminate() {
        MountCommand.unregister()

        log.info("[Mounts] has been disabled!")
    }

}
