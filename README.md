# EcoTracker Mobile App

Native Android app built with Kotlin + XML (Material Components).

## Implemented Features

- 5-step onboarding questionnaire with validation and aggregated payload collection.
- Home screen with sidebar navigation and server-ready data model.
- My Progress screen (charts/sections mock, server-ready repository layer).
- Personal Tips screen (list of recommendations with selection state).
- Learning Platform:
  - category overview
  - category lessons list
  - lesson details
  - persistent lesson completion via `SharedPreferences`
- Achievements:
  - achievements grid (locked/unlocked/claimed)
  - achievement detail
  - achievement received screen with claim action
  - persistent claimed state via `SharedPreferences`
- Community profile placeholder screen with "In development" label.

## Tech Stack

- Kotlin
- Android Views (XML)
- Material Components
- RecyclerView
- DrawerLayout
- Gradle Kotlin DSL

## Android Config

- `minSdk = 24`
- `targetSdk = 34`
- `compileSdk = 34`

## Project Structure

- `app/src/main/java/com/example/ecotracker` — activities, repositories, local stores
- `app/src/main/res/layout` — screen and item XML layouts
- `app/src/main/res/values` — strings, arrays, colors, themes
- `app/src/main/assets` — design images (used by onboarding/home/learning)

## Build & Run

### Android Studio

1. Open project folder.
2. Wait for Gradle sync.
3. Run `app` configuration on emulator/device.

### Terminal (Windows)

```powershell
.\gradlew.bat assembleDebug
.\gradlew.bat installDebug
```

Debug APK output:

```text
app/build/outputs/apk/debug/app-debug.apk
```

## Data & Integration Notes

- Current API/data is mocked through repository objects:
  - `HomeRepository`
  - `MyProgressRepository`
  - `PersonalTipsRepository`
  - `LearningRepository`
  - `AchievementsRepository`
- Onboarding answers are collected into a single JSON payload and passed forward.
- Progress and achievement claim states are stored locally with `SharedPreferences`.

## Repository

- Remote: `https://github.com/didukhroma/ecotracker-mobile-app.git`
- Branch: `master`
