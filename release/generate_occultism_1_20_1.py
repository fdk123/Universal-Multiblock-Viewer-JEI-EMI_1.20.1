"""Generate UMV guides directly from Occultism 1.20.1 Modonomicon multiblocks.

Usage: python release/generate_occultism_1_20_1.py path/to/occultism-1.20.1-1.158.0.jar
"""

import json
import sys
import zipfile
from pathlib import Path


def main(jar_path: Path) -> None:
    with zipfile.ZipFile(jar_path) as jar:
        lang = json.loads(jar.read("assets/occultism/lang/en_us.json"))
        prefix = "data/occultism/modonomicon/multiblocks/"
        names = sorted(
            Path(name).stem
            for name in jar.namelist()
            if name.startswith(prefix) and name.endswith(".json") and Path(name).stem != "debug"
            and Path(name).stem != "storage_stabilizer_demo"
        )
        destination = Path(__file__).parent / "resource-pack-sources/umv-occultism-guides-1.20.1"
        guides = destination / "assets/umv_occultism/universal_multiblock_viewer/multiblocks"
        guides.mkdir(parents=True, exist_ok=True)
        for name in names:
            source = json.loads(jar.read(f"{prefix}{name}.json"))
            mapping = source["mapping"]
            palette = {}
            for symbol, entry in mapping.items():
                kind = entry["type"]
                if kind == "modonomicon:display":
                    continue
                if kind == "modonomicon:block":
                    block = entry["block"]
                    option = {"block": block}
                    if block.startswith("occultism:chalk_glyph_"):
                        color = block.rsplit("_", 1)[1]
                        option["material"] = {
                            "item": f"occultism:chalk_{color}", "kind": "reusable_tool"
                        }
                    palette[symbol] = {"any_of": [option], "default": block}
                elif kind == "modonomicon:tag":
                    tag = entry["tag"].removeprefix("#")
                    if tag != "minecraft:candles":
                        raise ValueError(f"Unhandled tag {tag} in {name}")
                    palette[symbol] = {
                        "any_of": [{"block": "minecraft:candle"}, {"tag": tag}],
                        "default": "minecraft:candle",
                    }
                else:
                    raise ValueError(f"Unhandled mapping {kind} in {name}")
            layers = [
                ["".join(" " if cell == "_" or cell not in palette else cell for cell in row)
                 for row in layer]
                for layer in source["pattern"]
            ]
            if all(set("".join(layer)) <= {" "} for layer in layers[1:]):
                layers = layers[:1]
            title_key = f"book.occultism.dictionary_of_spirits.pentacles.{name}.name"
            guide = {
                "format": 1,
                "id": f"umv_occultism:{name}",
                "title": lang.get(title_key, name.replace("_", " ").title()),
                "description": "Occultism ritual pentacle for Minecraft 1.20.1.",
                "provenance": {
                    "source_mod": "occultism", "tested_version": "1.158.0",
                    "notes": f"Generated from Occultism Modonomicon multiblock {name}."
                },
                "lookups": {"U": ["occultism:golden_sacrificial_bowl"]},
                "default_variant": "ritual_layout",
                "variants": [{"id": "ritual_layout", "title": "Ritual layout",
                              "palette": palette, "layers": layers}],
            }
            (guides / f"{name}.json").write_text(json.dumps(guide, indent=2) + "\n", encoding="utf-8")
        (destination / "pack.mcmeta").write_text(json.dumps({"pack": {
            "pack_format": 15, "description": "UMV Occultism guides for 1.20.1 (Occultism 1.158.0)"
        }}, indent=2) + "\n", encoding="utf-8")
        print(f"Generated {len(names)} guides in {destination}")


if __name__ == "__main__":
    main(Path(sys.argv[1]))
