# AGENTS.md — play-clj libGDX 1.14.2 Update

## Background

This project is a **fork/update of the `play-clj` Clojure game library** (originally by Zach Oakes). The original library was abandoned in 2016 and targets libGDX 1.9.3. The goal of this project is to bring it up to date so it works cleanly with the latest stable libGDX (currently **1.14.2**, released June 2026).

The library is essentially a thin, idiomatic-Clojure wrapper around [libGDX](https://libgdx.com/), providing macros and helper functions for 2D/3D graphics, physics (Box2D, Bullet), UI (Scene2D), tiled maps, input handling, and asset management.

## Current Status

- **libGDX version**: 1.14.2 (was 1.9.3)
- **Clojure version**: 1.12.0 (was 1.7.0)
- **Java target**: 1.8 (was 1.6)
- **Desktop backend**: LWJGL3 (was LWJGL2)
- **Build tool**: Leiningen (`project.clj`) + Clojure CLI (`deps.edn`)
- **Test suite**: 25 tests, all passing via headless libGDX backend

## Project Structure

```
play-clj/
├── project.clj          # Leiningen build config
├── deps.edn             # Clojure CLI build config (includes :test alias)
├── src/
│   └── play_clj/
│       ├── core.clj                     # Main screen/game macros, interop helpers
│       ├── core_basics.clj              # Colors, input, sound, music, asset-manager
│       ├── core_cameras.clj             # Orthographic/Perspective camera helpers
│       ├── core_graphics.clj            # Pixmap, shape, tiled-map, renderers, Stage
│       ├── core_listeners.clj           # InputProcessor, GestureDetector, UI listeners
│       ├── core_utils.clj               # Timers, screenshots, preferences, bundles
│       ├── g2d.clj                      # 2D graphics: texture, sprite, animation, nine-patch
│       ├── g3d.clj                      # 3D graphics: model, model-batch, environment, material
│       ├── g2d_physics.clj              # Box2D physics wrappers
│       ├── g3d_physics.clj             # Bullet physics wrappers
│       ├── math.clj                     # Vector, matrix, geometry, spline, intersection helpers
│       ├── ui.clj                       # Scene2D UI widgets (buttons, labels, tables, etc.)
│       ├── utils.clj                    # String key conversion, GDX class/field helpers, data structures
│       ├── entities.clj                 # Entity records and draw! protocol
│       ├── physics.clj                  # Multimethod dispatch for 2D/3D physics
│       └── repl.clj                     # REPL helper functions
├── src-java/
│   └── play_clj/g3d_physics/
│       └── ContactListener3D.java       # Java class used by Bullet contact listener
├── test/
│   └── play_clj/
│       ├── headless_fixture.clj         # HeadlessApplication test fixture
│       ├── utils_test.clj               # Tests for key conversion, GDX symbols, data structures
│       ├── math_test.clj                # Tests for vectors, matrices, geometry, math utils
│       ├── core_test.clj                # Tests for colors, cameras, shapes, key/button codes
│       ├── g2d_test.clj                 # Tests for texture/sprite/animation entities
│       └── entities_test.clj            # Tests for entity records and draw! protocol
├── template/                            # Leiningen template for `lein new play-clj`
│   ├── project.clj
│   ├── resources/                       # Android native libs (.so files)
│   └── src/leiningen/new/play_clj/     # Template source files
├── classes/                             # Compiled Java output (ContactListener3D.class)
└── doclet/                              # Javadoc/doc generation helpers
```

## Build & Test

### Clojure CLI (deps.edn)

```bash
# Compile and download dependencies
clojure -P

# Run all tests
clojure -M:test run-tests.clj

# Start a REPL
clojure -M
```

### Leiningen

```bash
# Compile Java sources and build
lein javac
lein compile

# Start a REPL
lein repl
```

**Note**: The `gdx-platform$natives-desktop` dependency is only needed in the `:test` alias so that the headless backend can load native libraries during testing.

## Key Breaking Changes Fixed (libGDX 1.9.3 → 1.14.2)

### 1. `Animation<T>` is now generic
- **File**: `src/play_clj/g2d.clj`
- **Fix**: `animation->texture` now type-hints `.getKeyFrame` return value with `^TextureRegion`
- **Rationale**: Since libGDX 1.9.5, `Animation` is generic. Without the type hint, Clojure sees the return as `Object`, which breaks downstream code expecting a `TextureRegion`.

### 2. `InputProcessor.scrolled` signature changed
- **File**: `src/play_clj/core_listeners.clj`
- **Fix**: Changed `scrolled(int amount)` → `scrolled(float amountX, float amountY)`
- **Rationale**: libGDX 1.9.12 added 2D scroll support. The screen map now exposes `:amount-x` and `:amount-y` instead of `:amount`.

### 3. `Stage` constructor requires `Viewport` (1.9+)
- **Fix**: The headless backend initializes `Stage` with a default viewport, so existing `(Stage.)` usage still works in practice. No source change needed for the library itself, but template projects were updated to use `Lwjgl3ApplicationConfiguration` + `StretchViewport`.

### 4. LWJGL2 → LWJGL3
- **File**: `template/src/leiningen/new/play_clj/desktop-launcher.clj`
- **Fix**: Replaced `LwjglApplication` with `Lwjgl3Application` and `Lwjgl3ApplicationConfiguration`
- **Rationale**: libGDX deprecated LWJGL2. LWJGL3 is the default desktop backend since 1.11.0.

### 5. Android `armeabi` native libraries removed
- **File**: `template/src/leiningen/new/play_clj.clj`
- **Fix**: Removed all `armeabi/` (32-bit ARM) `.so` references from the template generator
- **Rationale**: Android armeabi support was dropped in libGDX 1.10.0.

### 6. `ScreenUtils.getFrameBufferPixels` deprecated
- **File**: `src/play_clj/core_utils.clj` (in `screenshot!`)
- **Note**: The old `ScreenUtils/getFrameBufferPixels` call still exists. In future, this should be migrated to `Pixmap/createFromFrameBuffer` as recommended by libGDX 1.9.14+.

## Coding Conventions

- **Pure-Clojure helper functions** (no libGDX dependency): live in `utils.clj`, `math.clj`
- **Graphics/physics wrappers**: use the `*` suffix for constructor fns (e.g., `texture*`, `sprite*`, `vector-2*`), and plain names for macros that chain method calls (e.g., `texture`, `sprite`)
- **Entity pattern**: Most drawable things are wrapped in a record implementing the `Entity` protocol, with an `:object` key holding the underlying Java object
- **Macros**: Heavy use of macros to chain Java method calls via `calls!` / `call!` helpers in `utils.clj`. Method names are converted from kebab-case keywords to camelCase Java method names
- **Namespace loading**: `core.clj` uses `(load "core_basics")` etc. to split a single namespace across multiple files

## How to Add Tests

1. Create a file in `test/play_clj/<name>_test.clj`
2. Require `[play-clj.headless-fixture]` and call `(use-fixtures :once play-clj.headless-fixture/headless-setup)` if the test touches libGDX classes
3. Tests that only exercise pure Clojure utilities (e.g. `utils_test.clj`) do **not** need the headless fixture
4. Avoid creating `Texture` from scratch in headless mode; use `(TextureRegion.)` or pass existing objects
5. Run with `clojure -M:test run-tests.clj`

## Template Notes

The `lein new play-clj` template is still Leiningen-based. If you edit template files, remember:
- `template/src/leiningen/new/play_clj/desktop-project.clj` — desktop project deps
- `template/src/leiningen/new/play_clj/android-project.clj` — android project deps
- `template/src/leiningen/new/play_clj/desktop-launcher.clj` — LWJGL3 launcher code
- `template/src/leiningen/new/play_clj/core.clj` — default game code template
- `template/src/leiningen/new/play_clj.clj` — template generator (renders files for new projects)

## Known Issues / TODOs

- `screenshot!` still uses deprecated `ScreenUtils/getFrameBufferPixels`. Should migrate to `Pixmap/createFromFrameBuffer`
- The `template` still generates Leiningen projects. A `deps.edn` template for Clojure CLI could be added
- `SelectBox`, `Tree`, `Slider` became generic in newer libGDX versions but Clojure reflection handles them without explicit type hints in most cases. If runtime `ClassCastException` appears in UI code, add explicit `^SelectBox<String>` style type hints
- Android template still references `org.clojure-android/clojure` and `neko`. These are old; modern Android builds would need a full rewrite using current Clojure and Android tooling
- The library does not yet expose newer libGDX features such as `JsonSkimmer`, `PoolManager`, `Vector4`, `Justify` text alignment, etc.

## License

Public Domain (UNLICENSE). All files that originate from this project are dedicated to the public domain.
