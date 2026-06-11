# IPTV Player (Android TV) — white-label starter

A clean, **from-scratch** Android TV / Fire TV IPTV player written in Kotlin.
It connects to an [Xtream Codes](https://en.wikipedia.org/wiki/Xtream_Codes)
compatible portal, shows live channels grouped by category, and plays them with
ExoPlayer (Media3). The server address is fixed in the app, so your users only
ever enter a **username** and **password**.

This project contains **only original code** — no decompiled or third-party app
binaries — so it is yours to brand and ship.

> ⚠️ **Use responsibly.** Only configure this app against IPTV portals you are
> authorized to use (your own subscription or your own reseller panel), and only
> distribute content you have the rights to distribute.

---

## What's a "portal URL"?

When you buy an IPTV subscription, the provider gives you three things:

| Thing            | Example                              | Where it goes        |
|------------------|--------------------------------------|----------------------|
| Server / portal  | `http://example-server.com:8080`     | `BrandConfig.kt` (you set this once) |
| Username         | `john123`                            | typed by the user    |
| Password         | `s3cret`                             | typed by the user    |

The app uses the standard Xtream Codes API (`player_api.php`) to log in, fetch
the channel list, and build playback URLs.

---

## 1. Point the app at your server

Open **`app/src/main/java/com/example/iptvplayer/BrandConfig.kt`** and set:

```kotlin
const val PORTAL_BASE_URL: String = "http://your-portal-domain.example:8080/"
```

Keep the trailing slash. That's the only change required to make the app
functional.

## 2. Re-brand it

Everything brand-related is centralized:

| What            | File                                                        |
|-----------------|-------------------------------------------------------------|
| App name        | `res/values/strings.xml` → `app_name`                       |
| Colors / theme  | `res/values/colors.xml` (the `brand_*` colors)              |
| Login logo      | `res/drawable/logo.xml` (replace with your PNG/vector)      |
| Launcher icon   | `res/drawable/ic_launcher.xml`                              |
| TV banner       | `res/drawable/app_banner.xml` (320×180)                     |
| Package / app id| `app/build.gradle.kts` → `applicationId` (and `namespace`)  |

The placeholder icons/logo/banner are simple vector drawables so the project
builds out of the box — swap them for your real artwork.

## 3. Build & run

This needs **Android Studio** (it ships the Android SDK and a Gradle wrapper
binary, which isn't committed here).

1. Open this folder in Android Studio and let it sync Gradle.
2. Plug in / start an **Android TV** emulator or device.
3. Press **Run**.

(From the command line you can run `gradle wrapper` once to generate
`gradlew`, then `./gradlew assembleDebug`.)

---

## How it's organized

```
app/src/main/java/com/example/iptvplayer/
├── BrandConfig.kt            ← portal URL + brand helpers (edit this)
├── IptvApp.kt                ← Application; initializes the ServiceLocator
├── data/
│   ├── model/XtreamModels.kt ← API response models
│   ├── remote/XtreamApi.kt   ← Retrofit definition of player_api.php
│   ├── remote/XtreamClient.kt← Retrofit/OkHttp setup
│   ├── SessionStore.kt       ← stores the logged-in credentials
│   └── XtreamRepository.kt   ← the app's data entry point
├── di/ServiceLocator.kt      ← tiny manual DI container
└── ui/
    ├── login/LoginActivity.kt    ← username + password only
    ├── browse/MainActivity.kt    ← hosts the Leanback browse fragment
    ├── browse/MainFragment.kt    ← channels grouped by category
    ├── browse/CardPresenter.kt   ← a single channel card
    └── player/PlaybackActivity.kt← ExoPlayer fullscreen playback
```

## Scope & next steps

This starter implements the full **Live TV** vertical slice: login → category
rows → channel grid → playback. Natural extensions:

- **VOD & Series** — the Xtream API exposes `get_vod_categories`,
  `get_vod_streams`, `get_series`, etc. Add them to `XtreamApi` and new browse rows.
- **EPG** (now/next program info) via `get_short_epg`.
- **Search & favorites.**
- **Encrypted credential storage** (`androidx.security:security-crypto`).
- **Real launcher icons** as PNG mipmaps for best results across devices.
