# Quick Start Guide - Android Movie Explorer

Get your native Android app up and running in 15 minutes!

## What You're Building

A fully native Android application (not WebView) that fetches movies and TV shows from TMDB API with offline caching and Material Design UI.

## Prerequisites (5 min)

### 1. Get TMDB API Key
- Go to: https://www.themoviedb.org/signup
- Create account → Request API key → Get approved
- Copy your API key

### 2. Install Java 11+
```bash
java -version  # Should be 11 or higher
```

### 3. Clone Repository
```bash
git clone <your-repo-url>
cd your-repo
```

---

## Option 1: Build Locally (5 min)

### Build Debug APK (No signing needed)

```bash
cd android

# Make gradle executable
chmod +x gradlew

# Build debug APK
./gradlew assembleDebug

# APK location: app/build/outputs/apk/debug/app-debug.apk
```

### Install on Device/Emulator

```bash
# Install
./gradlew installDebug

# Or via adb
adb install -r app/build/outputs/apk/debug/app-debug.apk
```

---

## Option 2: Build via GitHub (CI/CD) (10 min)

### Setup GitHub Secrets

1. Go to repository → Settings → Secrets and Variables → Actions

2. Add `TMDB_API_KEY`
   - Name: `TMDB_API_KEY`
   - Value: Your API key from step 1

3. Generate and add keystore (for signing):
   ```bash
   # Generate keystore
   keytool -genkey -v -keystore release.jks \
     -keyalg RSA -keysize 2048 -validity 10000 \
     -alias movie_app
   
   # Convert to base64 (macOS/Linux)
   base64 release.jks | pbcopy
   
   # Or on Linux:
   cat release.jks | base64 | xclip
   ```

4. Add keystore secrets:
   - `KEYSTORE_BASE64`: Paste the base64 content
   - `KEYSTORE_PASSWORD`: Your keystore password
   - `KEY_ALIAS`: `movie_app`
   - `KEY_PASSWORD`: Your key password

### Trigger Build

1. Push to `main` branch or manually:
   - Go to Actions → Build Android APK → Run workflow

2. Monitor build:
   - Actions tab → Click running workflow
   - Watch status

3. Download APK:
   - Build completes → Scroll to "Artifacts"
   - Download `MovieApp-APK-{number}`

---

## Installation on Device

### Method 1: Direct APK Installation

```bash
# Copy APK to phone via file manager
# Open file manager → Downloads
# Tap MovieApp-release.apk
# Follow prompts
```

### Method 2: Via ADB

```bash
# Ensure device is connected
adb devices

# Install APK
adb install -r MovieApp-release.apk

# Launch app
adb shell am start -n com.movieapp.explorer/.ui.MainActivity
```

### Method 3: Termux

```bash
# In Termux:
pkg install android-tools

# Navigate to APK location
cd ~/Downloads

# Install
adb install -r MovieApp-release.apk
```

---

## Project Structure Overview

```
android/
├── app/src/main/
│   ├── java/com/movieapp/explorer/
│   │   ├── data/          # API, Database, Models
│   │   ├── ui/            # Activities, Fragments, Adapters
│   │   └── viewmodel/     # ViewModels (MVVM)
│   └── res/
│       ├── layout/        # XML layouts
│       └── values/        # Colors, strings, dimens
└── build.gradle.kts       # Dependencies & config
```

---

## Key Files to Understand

| File | Purpose |
|------|---------|
| `MainActivity.java` | Entry point, tab navigation |
| `TmdbApiService.java` | API endpoints |
| `MovieRepository.java` | Movie business logic |
| `MovieViewModel.java` | UI state management |
| `MovieFragment.java` | Movie UI |
| `activity_main.xml` | Main layout |

---

## Testing the App

### Features to Try

1. **View Movies**
   - Tap Movies tab → See popular movies
   - Swipe through categories (Popular, Top Rated, Upcoming)

2. **View TV Shows**
   - Tap TV Shows tab → See popular shows
   - Swipe through categories

3. **Search** (Feature ready for implementation)
   - Use search bar at top

4. **Offline Caching**
   - Load content online
   - Go offline (airplane mode)
   - Browse cached content

---

## Customization

### Change App Name
Edit `android/app/src/main/res/values/strings.xml`:
```xml
<string name="app_name">Your App Name</string>
```

### Change Color Scheme
Edit `android/app/src/main/res/values/colors.xml`:
```xml
<color name="primary">#your_color</color>
<color name="secondary">#your_color</color>
```

### Add More Features
See: `ANDROID_PROJECT_SUMMARY.md` → Development Guide

---

## Troubleshooting

### Build Fails: "API key not found"
```bash
# Set API key environment variable
export TMDB_API_KEY="your_key_here"

# Or rebuild
cd android && ./gradlew clean assembleDebug
```

### APK Won't Install
```bash
# Uninstall first
adb uninstall com.movieapp.explorer

# Reinstall
adb install -r MovieApp-release.apk
```

### No Data Shows in App
1. Check internet connection
2. Verify TMDB API key is correct
3. Check API status: https://www.themoviedb.org

### Gradle Error
```bash
# Clean and retry
./gradlew clean build

# Update wrapper
./gradlew wrapper --gradle-version=8.2
```

---

## Next Steps

### For Development
1. Read `ANDROID_PROJECT_SUMMARY.md` for architecture details
2. Read `android/README.md` for API documentation
3. Explore `MovieViewModel.java` to understand data flow

### For Deployment
1. Follow `ANDROID_SETUP_GUIDE.md` for production build
2. Setup all GitHub Secrets
3. Push to `main` to trigger CI/CD
4. Download signed APK from releases

### For Production
1. Update app version in `build.gradle.kts`
2. Add release notes
3. Test on multiple devices
4. Publish to Play Store (convert APK to AAB)

---

## Documentation Map

| Document | When to Read |
|----------|--------------|
| **QUICK_START.md** | Getting started (you are here) |
| **ANDROID_SETUP_GUIDE.md** | Setup keystore & GitHub secrets |
| **ANDROID_PROJECT_SUMMARY.md** | Understand architecture |
| **android/README.md** | API docs & detailed setup |
| **termux-install.sh --help** | Installation help |

---

## Key Commands Reference

```bash
# Build
./gradlew assembleDebug              # Debug APK
./gradlew assembleRelease            # Release APK (with signing)

# Install
./gradlew installDebug               # Install debug APK
adb install -r app-release.apk       # Install any APK

# Test
./gradlew test                       # Unit tests
adb logcat                           # View logs

# Clean
./gradlew clean                      # Clean build
rm -rf ~/.gradle/caches              # Clear gradle cache

# Info
./gradlew tasks                      # List available tasks
adb devices                          # List connected devices
```

---

## Support

- **Issues?** Check Troubleshooting section above
- **API Help?** https://www.themoviedb.org/settings/api
- **Android Help?** https://developer.android.com
- **GitHub Issues?** Report in repository

---

## What's Included

✓ Full MVVM architecture
✓ TMDB API integration
✓ Room database caching
✓ Material Design UI
✓ GitHub Actions CI/CD
✓ APK signing automation
✓ Termux installation script
✓ Complete documentation

---

## Performance

- **APK Size**: ~45-50 MB
- **Min Android**: 8.0 (API 26)
- **Target Android**: 14 (API 34)
- **Languages**: Java 11

---

## Time Estimates

- **Setup**: 5-10 minutes
- **First Build**: 2-5 minutes
- **Installation**: 1-2 minutes
- **First Run**: Instant

---

**Ready to go!** Start with Option 1 or Option 2 above to get your APK built.

Good luck! 🚀
