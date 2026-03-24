# EcoTracker Mobile App

EcoTracker is an Android application written in Kotlin with XML layouts and Material Components.

The project currently contains a mock onboarding and authentication flow based on custom design screens, including validation states, navigation between screens, and placeholder destinations for the next product steps.

## Tech Stack

- Kotlin
- Android Views (XML)
- Material Components
- Gradle Kotlin DSL
- Min SDK 24
- Target SDK 34

## Current Flow

The app currently includes these screens:

1. Loading
2. Onboarding screen 1
3. Onboarding screen 2
4. Sign up
5. Sign in
6. Forgot password
7. Greeting
8. Question
9. Home placeholder

Implemented behavior:

- onboarding navigation between screens
- sign in validation for email and password
- sign up validation for name, email, and password
- forgot password email validation
- mocked sign in, sign up, forgot password, Google, and Facebook actions
- skip onboarding behavior

## Project Structure

Main Android source files live in:

- `app/src/main/java/com/example/ecotracker`
- `app/src/main/res/layout`
- `app/src/main/res/values`
- `app/src/main/assets`

Design images used by the app are stored in:

- `app/src/main/assets/loading.png`
- `app/src/main/assets/screen_2.png`
- `app/src/main/assets/screen_3.png`
- `app/src/main/assets/screen_4.png`
- `app/src/main/assets/screen_5.png`
- `app/src/main/assets/forgot.png`
- `app/src/main/assets/greeting.png`
- `app/src/main/assets/question.png`

## Run In Android Studio

1. Open the folder in Android Studio.
2. Wait for Gradle Sync to finish.
3. Select the `app` run configuration.
4. Start an emulator or connect a device.
5. Press `Run`.

## Run From Terminal

Build debug APK:

```powershell
.\gradlew.bat assembleDebug
```

Install debug build on a connected device or emulator:

```powershell
.\gradlew.bat installDebug
```

## Output

Generated debug APK:

```text
app/build/outputs/apk/debug/app-debug.apk
```

## Notes

- Authentication is mocked for now.
- Social sign in buttons are placeholders.
- The home screen after onboarding is still a placeholder destination.
- The project was migrated from an older starter app into a native Android structure.

## Git

Repository:

`https://github.com/didukhroma/ecotracker-mobile-app.git`
