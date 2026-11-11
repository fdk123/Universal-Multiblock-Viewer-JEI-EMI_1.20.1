package net.sprocketgames.universalmultiblockviewer.client;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.sprocketgames.universalmultiblockviewer.UniversalMultiblockViewer;
import net.sprocketgames.universalmultiblockviewer.data.MultiblockDefinitionRegistry;

/** Adds one concise discovery hint to items that open one or more loaded viewer guides. */
@EventBusSubscriber(modid = UniversalMultiblockViewer.MOD_ID, value = Dist.CLIENT)
public final class ClientTooltipEvents {
    private static final int GUIDE_AMBER = 0xFFD19545;

    private ClientTooltipEvents() {
    }

    @SubscribeEvent
    public static void addGuideHint(ItemTooltipEvent event) {
        ResourceLocation item = BuiltInRegistries.ITEM.getKey(event.getItemStack().getItem());
        boolean uses = MultiblockDefinitionRegistry.all().stream().anyMatch(definition -> definition.useLookupItems().contains(item));
        boolean recipes = MultiblockDefinitionRegistry.all().stream().anyMatch(definition -> definition.recipeLookupItems().contains(item));
        if (!uses && !recipes) return;
        String text = uses && recipes ? "Press R or U for Multiblock Guide"
            : recipes ? "Press R for Multiblock Guide" : "Press U for Multiblock Guide";
        event.getToolTip().add(Component.literal(text).withStyle(style -> style.withColor(GUIDE_AMBER)));
    }
}
