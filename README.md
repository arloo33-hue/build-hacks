# Advanced Building Tools

**Minecraft 1.20.1 • Fabric**

A powerful set of creative building tools inspired by Effortless Building and similar mods.

## Features

### Building Wand
- Hold the **Building Wand**
- Press **F** → opens a searchable list of **every registered block** (vanilla + all modded blocks)
- Left-click a block in the list to select it
- Press **G** (or use the wand) → open the Shape menu
- **20 shapes**: Cube, Hollow Cube, Sphere, Hollow Sphere, Cylinder, Pyramid, Line, Wall, Floor, Circle, Dome, Cone, Frame, Cross, Stairs, Arch, Tunnel, and more
- Modes: **Solid**, **Hollow**, **Air** (clear)
- Click two corners to define the area → blocks are placed instantly
- **Very long reach** (64 blocks) while holding the wand

### Ghost Tool
- Right-click any block → turns it into a ghost (currently uses Barrier)
- Right-click a barrier again to remove it

### Other Tools
- **Measuring Tape** – click two corners to see exact size and volume
- **Copy & Paste Tool** – placeholder ready for full schematic support
- **Trap Launcher** – quickly places simple traps

## Requirements

- Minecraft **1.20.1**
- Fabric Loader ≥ 0.15
- Fabric API for 1.20.1
- Java **17**

## Building

```bash
# Generate the Gradle wrapper (first time only)
gradle wrapper --gradle-version 8.8

# Build
./gradlew build
```

The finished jar will be in `build/libs/`.

## Installation

1. Install Fabric Loader for 1.20.1
2. Put the mod jar + Fabric API into your `mods` folder
3. Launch the game

## Development Notes

After running `./gradlew genSources`, open the mixin classes and verify the exact method name used for reach distance in `ClientPlayerInteractionManager` (it can be `getReachDistance` or similar). Adjust the `@Inject` target if needed.

## Expanding the Mod

- Improve Ghost Tool with a real custom translucent block + renderer
- Full copy/paste with rotation & mirroring
- More trap presets
- Undo / Redo system
- Better visual selection outline while choosing corners

## License

MIT – see [LICENSE](LICENSE)

---

Made for singleplayer / creative building. Enjoy!
