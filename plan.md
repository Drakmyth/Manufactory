# Minecraft 26.1.2 / NeoForge Migration Plan

## Summary

Perform a direct port from Minecraft 1.19.2/Forge to Minecraft 26.1.2/NeoForge using Java 25 and ModDevGradle. Consult each official migration primer sequentially, but do not create intermediate-version builds. Target the latest stable NeoForge release compatible with 26.1.2; stop if no stable release exists rather than silently selecting a beta.

Preserve working gameplay behavior, but replace superseded APIs with current NeoForge practices. Fresh worlds are required; no 1.19.2 save migration is promised. Keep generated resources ignored and produce them in CI.

## Step-by-Step Implementation

1. **Capture the baseline**
   - Record all registered IDs, recipes, blocks, items, fluids, menus, commands, configuration defaults, and reachable gameplay features.
   - Build and smoke-test the 1.19.2 version where possible.
   - Classify obvious stubs, including the battery that always returns zero, as deferred rather than migration regressions.
2. **Replace the build toolchain**
   - Base Gradle configuration and wrapper on the official 26.1.2 ModDevGradle MDK.
   - Configure Java 25, UTF-8, client/server/GameTest/datagen runs, publication, and existing artifact naming.
   - Remove ForgeGradle, Parchment, reobfuscation, and legacy token replacement.
3. **Update metadata and CI**
   - Replace `mods.toml` with `META-INF/neoforge.mods.toml` and current dependency ranges.
   - Update pack metadata, `update.json`, workflows, README, and AGENTS.md.
   - Make datagen a CI prerequisite artifact while keeping `src/generated/` ignored.
4. **Restore a compiling mod skeleton**
   - Port initialization, event subscribers, configuration, commands, client setup, and datagen.
   - Convert registries while retaining every established `manufactory:*` ID.
   - Compile after initialization, registries, configuration, and events.
5. **Port vanilla-facing content**
   - Update identifiers, holders, properties, creative tabs, tags, tiers, interactions, tickers, block entities, renderers, menus, and screens.
   - Port obtainable content and the mechanical dimension.
   - Reserve nonfunctional IDs but make them unobtainable pending redesign.
6. **Modernize storage and menus**
   - Replace legacy item capabilities with transactional item resource handlers.
   - Store Rock Drill upgrades in the vanilla container data component.
   - Use Value I/O/codecs for persistence and menu data slots for synchronization.
7. **Port recipes and networking**
   - Use validated `MapCodec` and `StreamCodec` recipe definitions.
   - Update lookup and datagen APIs.
   - Remove `SimpleChannel`, `DistExecutor`, and legacy packets; use typed payloads only if menu synchronization is insufficient.
8. **Implement the hybrid power port**
   - Preserve topology, solar behavior, connection rules, allocation, and machine progression.
   - Use fixed-point `long` energy (1 legacy unit = 1,000 internal units) and transactional transfers.
   - Use codec-based `SavedData`; reserve the battery ID but keep it unobtainable.
9. **Regenerate and validate resources**
   - Update generated and authored resources for current schemas and pack formats.
   - Validate clean datagen, client, dedicated server, namespaces, and packaged generated resources.
10. **Finish in functional milestones**
    - Gate bootstrap, registrations, static content, inventories, recipes, machines, power, UI/networking, fluids/worldgen, and final build.
    - Remove compatibility shims only after replacements pass.
    - Document deferred features separately from migration defects.

## Interface Changes

- Registry objects become NeoForge deferred holders/suppliers.
- Legacy item handlers become transactional resource handlers and capabilities.
- Recipes become codec-based.
- Legacy channel messages become menu synchronization or typed payloads.
- `float` power becomes fixed-point transactional `long` energy.
- Persistent data uses Value I/O/codecs.

## Test and Acceptance Plan

- Add unit tests for cable graph behavior, fair allocation, fixed-point conversion, and recipe codecs.
- Add GameTests for registrations, machines, upgrades, power, solar, cables, persistence, fluids, and commands.
- Verify menus/renderers in a client and loading on a dedicated server.
- Validate the mechanical dimension and generated resources in a fresh world.
- Acceptance requires `runData`, unit tests, GameTests, client, dedicated server, and `build` on Java 25 with no Forge dependencies/imports.

## Assumptions

- Only fresh 26.1.2 worlds are supported.
- Mod and registry IDs remain stable.
- Use ModDevGradle and the latest stable compatible NeoForge release.
- Generated resources remain ignored and are produced by CI.
- Working behavior is preserved; demonstrable stubs are disabled and deferred.
