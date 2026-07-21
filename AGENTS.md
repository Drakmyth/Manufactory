# Repository Guidelines

## Project Structure & Module Organization

Manufactory is a Java 25 Minecraft 26.1.2 NeoForge mod built with ModDevGradle. Production code lives under `src/main/java/com/drakmyth/minecraft/manufactory`, grouped by feature. Static resources are in `src/main/resources`; generated data belongs in `src/generated/resources` and must not be hand-edited or committed. Tests mirror production packages under `src/test/java`.

## Build, Test, and Development Commands

Use the checked-in wrapper. On Windows, replace `./gradlew` with `gradlew.bat`.

- `./gradlew runData` — regenerate ignored data into `src/generated/resources`; required before a local release build.
- `./gradlew build` — compile, test, process authored and generated resources, and produce the mod JAR.
- `./gradlew runClient` — launch a development client.
- `./gradlew runServer` — launch a dedicated development server.
- `./gradlew runGameTestServer` — run registered NeoForge GameTests and exit.
- `./gradlew clean` — remove Gradle build outputs.

## Coding Style & Naming Conventions

Use UTF-8, four-space indentation, and same-line braces. Packages are lowercase beneath `com.drakmyth.minecraft.manufactory`; classes use `PascalCase`, members use `camelCase`, and constants use `UPPER_SNAKE_CASE`. Resource and registry IDs use lowercase `snake_case`. Follow nearby NeoForge patterns and keep imports tidy.

## Testing Guidelines

Prefer NeoForge GameTests for behavior requiring Minecraft state. Name test classes `*Tests`. Run `./gradlew build` before submitting and `./gradlew runGameTestServer` for changed game behavior. Focus on regressions in machines, recipes, networking, power, fluids, commands, and persistence.

## Commit & Pull Request Guidelines

Use short imperative commit summaries and keep commits focused. Pull requests should explain changes and verification, link issues, and include screenshots or video for visible changes. Call out generated-resource, configuration, compatibility, and deferred-feature impacts.
