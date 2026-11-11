package net.sprocketgames.universalmultiblockviewer;

import com.mojang.logging.LogUtils;
import net.minecraftforge.fml.common.Mod;
import net.sprocketgames.universalmultiblockviewer.dev.DevInstantBuildNetworking;
import org.slf4j.Logger;

/** Common entry point. Viewer-host adapters are discovered only when their host mod is installed. */
@Mod(UniversalMultiblockViewer.MOD_ID)
public final class UniversalMultiblockViewer {
    public static final String MOD_ID = "universal_multiblock_viewer";
    public static final Logger LOGGER = LogUtils.getLogger();

    public UniversalMultiblockViewer() {
        DevInstantBuildNetworking.register();
        LOGGER.info("Universal Multiblock Viewer is loading");
    }
}
