# EcoTracker Android

Native Android app built with Kotlin + XML.

## Stack

- Kotlin
- Android Views / XML layouts
- Firebase Authentication
- Cloud Firestore

## Requirements

- Android Studio
- JDK 17
- Android SDK 34

## App Id

- `com.example.ecotracker`

## Firebase setup

1. Create a Firebase project for package `com.example.ecotracker`
2. Enable `Email/Password` in Firebase Authentication
3. Create a Firestore database
4. Put `google-services.json` into `app/google-services.json`

If `google-services.json` is missing, Firebase flows will not work.

## Run

```powershell
.\gradlew.bat assembleDebug
.\gradlew.bat installDebug
```

## APK

- `app/build/outputs/apk/debug/app-debug.apk`
- `app/build/outputs/apk/debug/ecotracker.apk`

## Downloads

Test builds are published in GitHub Releases.

- Releases page: https://github.com/didukhroma/ecotracker-mobile-app/releases
- Latest APK: add the direct release asset link here after publishing

Notes:
- APK files are distributed through GitHub Releases, not stored in the repository.
- Use the latest release asset for Android testing.

## Main flows

- Sign up / Sign in / Forgot password
- 5-step onboarding with validation
- Home screen with sidebar
- Carbon emission tracker
- My progress
- Learning platform
- Personal tips
- Achievements
- Settings

## Data stored in Firestore

- `users/{uid}/profile/account`
- `users/{uid}/profile/onboarding`
- `users/{uid}/profile/learning`
- `users/{uid}/profile/personalTips`
- `users/{uid}/profile/achievements`
- `users/{uid}/profile/carbonTracker`

## Notes

- Carbon tracker is calculated on the client
- Before onboarding is completed, emission values stay at `0`
- After onboarding is completed, the first baseline estimate is calculated from onboarding answers
- `Personal tips` reset daily and sync cumulative progress to Firestore
- `My progress` uses the same tracker, learning progress, and personal tips state
- Logout clears local user state and signs out from Firebase
