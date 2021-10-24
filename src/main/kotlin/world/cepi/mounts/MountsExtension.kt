package world.cepi.mounts

import net.minestom.server.extensions.Extension;
import world.cepi.kstom.event.listenOnly

class MountsExtension : Extension() {

    override fun initialize() {
        eventNode.listenOnly(MountHook::hookUseOnBlock)
        eventNode.addChild(MountHook.playerDisconnectNode)
        MountCommand.register()

        logger.info("[Mounts] has been enabled!")
    }

    override fun terminate() {
        MountCommand.unregister()

        logger.info("[Mounts] has been disabled!")
    }

}
