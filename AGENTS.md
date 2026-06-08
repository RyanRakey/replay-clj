# AGENTS.md — play-clj libGDX 1.14.2 Update

## Background

This project is a **fork/update of the `play-clj` Clojure game library** (originally by Zach Oakes). The original library was abandoned in 2016 and targets libGDX 1.9.3. The goal of this project is to bring it up to date so it works cleanly with the latest stable libGDX (currently **1.14.2**, released June 2026).

The library is essentially a thin, idiomatic-Clojure wrapper around [libGDX](https://libgdx.com/), providing macros and helper functions for 2D/3D graphics, physics (Box2D, Bullet), UI (Scene2D), tiled maps, input handling, and asset management.

## Current Status

- **libGDX version**: 1.14.2 (was 1.9.3)
- **Clojure version**: 1.12.0 (was 1.7.0)
- **Java target**: 1.8 (was 1.6)
- **Desktop backend**: LWJGL3 (was LWJGL2)
- **Build tool**: Clojure CLI (`deps.edn`)
- **Test suite**: 351 tests (329 headless + 22 GL), all passing

## Project Structure

```
play-clj/
├── deps.edn             # Clojure CLI build config
├── build.clj            # Java compilation and JAR packaging (tools.build)
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
│   └── play_clj/
│       ├── g3d_physics/
│       │   └── ContactListener3D.java   # Java class used by Bullet contact listener
│       └── test/
│           └── MockGL20.java            # Mock GL20 implementation for headless GL tests
├── classes/                             # Compiled Java output (ContactListener3D.class)
└── test/
    └── play_clj/
        ├── headless_fixture.clj         # HeadlessApplication test fixture (no GL)
        ├── gl_fixture.clj               # GL-aware fixture using MockGL20
        ├── core_test.clj                # Tests for colors, cameras, shapes, key/button codes
        ├── core_listeners_test.clj      # Tests for input/listener machinery
        ├── core_utils_test.clj          # Tests for timers, assets, preferences
        ├── entities_test.clj            # Tests for entity records and draw! protocol
        ├── g2d_test.clj                 # Tests for texture/sprite/animation entities
        ├── g3d_test.clj                 # Tests for 3D graphics (model, environment, material)
        ├── g3d_physics_test.clj         # Tests for Bullet physics wrappers
        ├── gl_core_test.clj             # GL tests: stage*, shape*, clear!
        ├── gl_entities_test.clj         # GL tests: entity draw! with real SpriteBatch
        ├── gl_g2d_test.clj              # GL tests: Texture, Sprite, BitmapFont, NinePatch
        ├── math_test.clj                # Tests for vectors, matrices, geometry, math utils
        ├── physics_test.clj             # Tests for physics multimethod dispatch
        ├── physics_shapes_test.clj      # Tests for Box2D shape macros
        ├── repl_test.clj                # Tests for REPL helper functions
        ├── ui_test.clj                  # Tests for Scene2D UI widgets
        └── utils_test.clj               # Tests for key conversion, GDX symbols, data structures
```

## Build & Test

### Clojure CLI (deps.edn)

```bash
# Download dependencies
clojure -P

# Compile Java sources (only needed after modifying .java files)
clojure -T:build javac

# Run all tests
clojure -M:test run-tests.clj

# Run GL-dependent tests (uses MockGL20 for headless GL context)
clojure -M:test run-gl-tests.clj

# Generate coverage report (HTML at target/coverage/index.html)
clojure -M:test:coverage

# Start a REPL
clojure -M

# Build a JAR
clojure -T:build jar
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
2. For tests that don't need GL: require `[play-clj.headless-fixture]` and call `(use-fixtures :once play-clj.headless-fixture/headless-setup)`
3. For tests that need GL (SpriteBatch, ShapeRenderer, Stage, Texture, BitmapFont): require `[play-clj.gl-fixture]` and call `(use-fixtures :once play-clj.gl-fixture/gl-setup)`. This injects a `MockGL20` into `Gdx.gl`/`Gdx.gl20` that returns valid handles for shader/buffer/texture creation.
4. When creating `TextureRegion` or `Sprite` in GL tests, use a real `Texture` from a `Pixmap` — `(TextureRegion.)` has a null texture and will crash on draw.
5. Run headless tests with `clojure -M:test run-tests.clj`
6. Run GL tests with `clojure -M:test run-gl-tests.clj`

## Known Issues / TODOs

- `screenshot!` still uses deprecated `ScreenUtils/getFrameBufferPixels`. Should migrate to `Pixmap/createFromFrameBuffer`
- `SelectBox`, `Tree`, `Slider` became generic in newer libGDX versions but Clojure reflection handles them without explicit type hints in most cases. If runtime `ClassCastException` appears in UI code, add explicit `^SelectBox<String>` style type hints
- The library does not yet expose newer libGDX features such as `JsonSkimmer`, `PoolManager`, `Vector4`, `Justify` text alignment, etc.

## License

Public Domain (UNLICENSE). All files that originate from this project are dedicated to the public domain.
