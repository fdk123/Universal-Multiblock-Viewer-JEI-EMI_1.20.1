package net.sprocketgames.universalmultiblockviewer.integration.emi;

import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.MinecraftForge;
import net.sprocketgames.universalmultiblockviewer.UniversalMultiblockViewer;
import net.sprocketgames.universalmultiblockviewer.data.MultiblockDefinitionRegistry;

/** Optional EMI entry point. EMI discovers this class only when EMI itself is installed. */
@EmiEntrypoint
public final class UniversalMultiblockViewerEmiPlugin implements EmiPlugin {
    public static final EmiRecipeCategory CATEGORY = new EmiRecipeCategory(
        new ResourceLocation(UniversalMultiblockViewer.MOD_ID, "multiblock"), EmiStack.of(Items.SPYGLASS));
    private static boolean inputHandlersRegistered;

    @Override
    public void register(EmiRegistry registry) {
        registerInputHandlers();
        registry.addCategory(CATEGORY);
        var definitions = MultiblockDefinitionRegistry.all();
        int useItems = definitions.stream().mapToInt(definition -> definition.useLookupItems().size()).sum();
        int recipeItems = definitions.stream().mapToInt(definition -> definition.recipeLookupItems().size()).sum();
        UniversalMultiblockViewer.LOGGER.info(
            "Universal Multiblock Viewer EMI registered {} guide(s), indexed {} U item(s) and {} R item(s)",
            definitions.size(), useItems, recipeItems
        );
        definitions.forEach(definition -> {
            registry.addRecipe(new EmiMultiblockRecipe(definition));
        });
    }

    private static void registerInputHandlers() {
        if (inputHandlersRegistered) return;
        inputHandlersRegistered = true;
        MinecraftForge.EVENT_BUS.addListener(EmiViewportInputHandler::onMouseScrolled);
        MinecraftForge.EVENT_BUS.addListener(EmiViewportInputHandler::onMousePressed);
        MinecraftForge.EVENT_BUS.addListener(EmiViewportInputHandler::onMouseDragged);
        MinecraftForge.EVENT_BUS.addListener(EmiViewportInputHandler::onMouseReleased);
        MinecraftForge.EVENT_BUS.addListener(EmiViewportInputHandler::tooltip);
        MinecraftForge.EVENT_BUS.addListener(EmiMaterialStripInputHandler::click);
        MinecraftForge.EVENT_BUS.addListener(EmiMaterialStripInputHandler::scroll);
        MinecraftForge.EVENT_BUS.addListener(EmiMaterialStripInputHandler::drag);
        MinecraftForge.EVENT_BUS.addListener(EmiMaterialStripInputHandler::tooltip);
        MinecraftForge.EVENT_BUS.addListener(EmiLayerInputHandler::click);
        MinecraftForge.EVENT_BUS.addListener(EmiLayerInputHandler::tooltip);
    }
}
