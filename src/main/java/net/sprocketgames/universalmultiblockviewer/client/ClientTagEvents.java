package net.sprocketgames.universalmultiblockviewer.client;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.event.TagsUpdatedEvent;
import net.sprocketgames.universalmultiblockviewer.UniversalMultiblockViewer;
import net.sprocketgames.universalmultiblockviewer.data.MultiblockDefinitionReloadListener;

/** Refreshes concrete tag alternatives after the client receives its live registry tags. */
@EventBusSubscriber(modid = UniversalMultiblockViewer.MOD_ID, value = Dist.CLIENT)
public final class ClientTagEvents {
    private ClientTagEvents() {
    }

    @SubscribeEvent
    public static void refreshDefinitionsAfterTags(TagsUpdatedEvent event) {
        MultiblockDefinitionReloadListener.INSTANCE.refreshResolvedDefinitions();
    }
}
