# EcoTracker (Android, Kotlin)

Kotlin + XML Android app with Firebase auth and Firestore profile/onboarding storage.

## Run

```powershell
.\gradlew.bat assembleDebug
.\gradlew.bat installDebug
```

APK:

`app/build/outputs/apk/debug/app-debug.apk`

## Firebase

- Package: `com.example.ecotracker`
- Put `google-services.json` in `app/`
- Enable Email/Password in Firebase Authentication
- Create Firestore database

## Main modules

- Auth: Sign In / Sign Up / Forgot password
- Onboarding: 5 steps + validation + Firestore save
- Home + sidebar navigation
- My Progress, Learning, Personal Tips, Achievements
- Settings (profile edit + Firestore save)
