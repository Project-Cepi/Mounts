package world.cepi.mounts

import net.minestom.server.extensions.Extension;

class MountsExtension : Extension() {

    override fun initialize() {
        logger.info("[Mounts] has been enabled!")
    }

    override fun terminate() {
        logger.info("[Mounts] has been disabled!")
    }

}
