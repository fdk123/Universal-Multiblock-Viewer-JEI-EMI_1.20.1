package net.sprocketgames.universalmultiblockviewer.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.resources.ResourceLocation;
import org.junit.jupiter.api.Test;

class MultiblockMaterialsTest {
    @Test
    void alternativesContributeOnlyTheirAuthoredPresentationDefault() {
        BlockRequirement alternatives = new BlockRequirement(List.of(
            new BlockOption(BlockOption.Kind.BLOCK, new ResourceLocation("minecraft:stone")),
            new BlockOption(BlockOption.Kind.BLOCK, new ResourceLocation("minecraft:granite"))), 0, "Frame");
        Map<GridPos, BlockRequirement> cells = new LinkedHashMap<>();
        cells.put(new GridPos(0, 0, 0), alternatives);
        cells.put(new GridPos(1, 0, 0), alternatives);
        cells.put(new GridPos(2, 0, 0), alternatives);
        StructureVariant variant = new StructureVariant("base", "Base", 3, 1, 1, cells);

        List<MaterialEntry> materials = MultiblockMaterials.forVariant(variant);

        assertEquals(1, materials.size());
        assertEquals(new ResourceLocation("minecraft:stone"), materials.get(0).requirement().defaultBlock().id());
        assertEquals(3, materials.get(0).count());
    }

    @Test
    void combinesDifferentPositionRequirementsWhenTheirDisplayedBlockMatches() {
        BlockOption rune = new BlockOption(BlockOption.Kind.BLOCK, new ResourceLocation("minecraft:gold_block"));
        BlockRequirement upgradeable = new BlockRequirement(List.of(rune,
            new BlockOption(BlockOption.Kind.BLOCK, new ResourceLocation("minecraft:diamond_block"))), 0, "Upgradeable rune");
        BlockRequirement fixed = new BlockRequirement(List.of(rune), 0, "Fixed rune");
        Map<GridPos, BlockRequirement> cells = new LinkedHashMap<>();
        for (int index = 0; index < 4; index++) cells.put(new GridPos(index, 0, 0), upgradeable);
        for (int index = 0; index < 4; index++) cells.put(new GridPos(index, 1, 0), fixed);

        List<MaterialEntry> materials = MultiblockMaterials.forVariant(new StructureVariant("tier_1", "Tier 1", 4, 2, 1, cells));

        assertEquals(1, materials.size());
        assertEquals(rune, materials.get(0).requirement().defaultBlock());
        assertEquals(8, materials.get(0).count());
    }

    @Test
    void reusableToolsCountOnceWhileRetainingAllPlacedPositions() {
        BlockOption chalkMark = new BlockOption(BlockOption.Kind.BLOCK, new ResourceLocation("minecraft:stone"), Map.of("axis", "x"),
            new MaterialPresentation(new ResourceLocation("minecraft:flint_and_steel"), MaterialPresentation.Kind.REUSABLE_TOOL));
        BlockOption anotherOrientation = new BlockOption(BlockOption.Kind.BLOCK, new ResourceLocation("minecraft:stone"), Map.of("axis", "z"),
            new MaterialPresentation(new ResourceLocation("minecraft:flint_and_steel"), MaterialPresentation.Kind.REUSABLE_TOOL));
        Map<GridPos, BlockRequirement> cells = new LinkedHashMap<>();
        cells.put(new GridPos(0, 0, 0), new BlockRequirement(List.of(chalkMark), 0, "Mark"));
        cells.put(new GridPos(1, 0, 0), new BlockRequirement(List.of(anotherOrientation), 0, "Mark"));
        cells.put(new GridPos(2, 0, 0), new BlockRequirement(List.of(chalkMark), 0, "Mark"));

        List<MaterialEntry> materials = MultiblockMaterials.forVariant(new StructureVariant("marks", "Marks", 3, 1, 1, cells));

        assertEquals(1, materials.size());
        assertEquals(1, materials.get(0).count());
        assertEquals(3, materials.get(0).placedCount());
        assertEquals(MaterialPresentation.Kind.REUSABLE_TOOL, materials.get(0).kind());
    }
}
