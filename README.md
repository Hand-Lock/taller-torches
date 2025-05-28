[![License: LGPL v3 or later](https://img.shields.io/badge/License-LGPL%20v3%2B-blue.svg)](https://github.com/Hand-Lock/studded-armor/blob/master/LICENSE)

# Taller Torches: Hitbox & Particle Fix

*A micro-mod that resizes torch collision/outline boxes **and** moves their flame particles so tall torch textures finally look right.*

---

## 🔥 Why This Exists

Resource-packs that restore the **Indev-era 13 px torch** (or any taller design) look gorgeous—until the flame spawns halfway down the stick and you end up smacking your mouse while trying to break the torch. **Taller Torches** fixes both issues for **torches, soul torches *and* redstone torches (floor & wall variants)**:

* **Particle origin** lifted to match the real top of the sprite.
* **Hit-box & outline** stretched so you can target the torch where it visibly is.

![Animation showing the problem of using a torch.png with a different height than vanilla, and the mod's fix](https://cdn.modrinth.com/data/cached_images/caaa1fa8dd6c4946171a9405f70ebc8109dbc7e0.gif)

No more flames stuck inside the stem or mis-aligned outlines—just pretty, tiki torches!

---

## 📏 How It Works

At load-time the mod reads a single JSON file:

```jsonc
// config/tallertorches.json
{
  "torch_height_px": 13,   // 10 = vanilla, 13 = Indev (default)
  "include_redstone": true // toggle: also adjust redstone torches
}
```

1. **torch\_height\_px** — height of your *torch.png* in pixels.
2. **include\_redstone** — when *true*, applies the same adjustments to redstone torches & wall redstone torches; set *false* to keep their vanilla shape.
3. Everything else (collision, outline, flame/smoke offsets, wall side-shift) is computed automatically.

---

## ⚙️ Installation & Dependencies

| Loader                        | Version            | Notes                          |
| ----------------------------- | ------------------ | ------------------------------ |
| **Fabric**                    | ≥ 0.16.14 (1.20.1) | **Fabric-API *not* required**  |
| **Forge (Sinytra Connector)** | (1.20.1)             | Works flawlessly               |
| **Quilt**                     | Untested           | Expected to behave like Fabric |

*Built against Yarn mappings; only **four tiny mixins** (`TorchBlock`, `WallTorchBlock`, `RedstoneTorchBlock`, `WallRedstoneTorchBlock`).*

---

## 🧰 Lightweight by Design

* ≈ 23 kB JAR
* No mixin GUIs, no runtime reflection
* Mixins run only when a torch’s shape or particle spawns

---

## 🖼️ Example Heights

| Torch sprite     | `torch_height_px` | Result                                              |
| ---------------- | ----------------- | --------------------------------------------------- |
| **Vanilla**      | 10                | Unchanged (particles & hit-box remain vanilla)      |
| **Indev**        | 13 *(default)*    | Flame sits neatly on the tip; hit-box ≈ 30 % taller |
| **Custom 16 px** | 16                | Ultra-tall torches? Just set 16 and reload!         |

*(Values beyond 16 px are supported but may clip ceilings in cramped builds.)*

*(Values below 10px are also supported, the minimum is 2px.)*

---

## 🖇️ Credits

* **Code & idea:** *HandLock\_*
* **Inspiration:** Mojang’s forgotten Indev textures – see the [Wiki archive](https://minecraft.wiki/w/Java_Edition_history_of_textures/Blocks#/media/File:Torch_%28texture%29_JE1.png).

> *“A taller torch deserves a taller flame.”*
