# Movie Explorer - Android Native App

A fully native Android Java application for browsing movies and TV shows from The Movie Database (TMDB) API.

## Features

- **Movie & TV Show Browsing**
  - Popular, Top Rated, Upcoming movies
  - Popular, Top Rated, On The Air TV shows
  - Real-time data from TMDB API
  
- **Search & Discovery**
  - Full-text search across movies and TV shows
  - Filter by genres and networks/studios
  - Advanced discovery with multiple sort options
  
- **Content Protection**
  - PIN protect sensitive content
  - Manage protected media library
  
- **Offline Support**
  - Local caching via Room database
  - Browse previously loaded content offline
  
- **Material Design**
  - Dark theme (Material Design 3)
  - Responsive UI for all screen sizes
  - Smooth animations and transitions

## Project Structure

```
android/
├── app/
│   ├── src/
│   │   └── main/
│   │       ├── java/com/movieapp/explorer/
│   │       │   ├── data/
│   │       │   │   ├── api/              # Retrofit API clients
│   │       │   │   ├── db/               # Room Database DAOs
│   │       │   │   ├── model/            # Data models
│   │       │   │   └── repository/       # Repository pattern
│   │       │   └── ui/
│   │       │       ├── MainActivity.java
│   │       │       ├── adapter/          # RecyclerView adapters
│   │       │       ├── fragment/         # UI Fragments
│   │       │       └── viewmodel/        # ViewModels (MVVM)
│   │       ├── res/
│   │       │   ├── layout/               # XML layouts
│   │       │   ├── values/               # Colors, strings, dimens
│   │       │   └── drawable/             # Drawable resources
│   │       └── AndroidManifest.xml
│   ├── build.gradle.kts
│   └── proguard-rules.pro
├── build.gradle.kts
└── settings.gradle.kts
```

## Architecture

This app uses **MVVM (Model-View-ViewModel)** architecture:

- **Model**: Data models + Repository layer
- **View**: Activities, Fragments, and Adapters
- **ViewModel**: Business logic and data management with LiveData

### Key Components

1. **API Layer** (Retrofit)
   - `TmdbApiService`: Interface for TMDB API endpoints
   - `RetrofitClient`: Singleton for HTTP client
   - Automatic API key injection via interceptor

2. **Database Layer** (Room)
   - `MovieAppDatabase`: Main database
   - `MovieDao`, `TvShowDao`: Data access objects
   - Supports offline caching

3. **Repository Pattern**
   - `MovieRepository`: Manages movie data
   - `TvShowRepository`: Manages TV show data
   - Handles API calls and local caching

4. **ViewModel Layer**
   - `MovieViewModel`: Movie business logic
   - `TvShowViewModel`: TV show business logic
   - Exposes LiveData for UI observation

5. **UI Layer**
   - `MainActivity`: Entry point with tab navigation
   - `MovieFragment`, `TvShowFragment`: Content fragments
   - `MovieAdapter`, `TvShowAdapter`: List adapters
   - Material Design components

## Setup & Build

### Prerequisites

- **Java 11** or higher
- **Android Studio** Flamingo or newer
- **Gradle 8.2+**
- **Android SDK 34** or newer

### Local Development

1. **Clone the repository**
   ```bash
   git clone <repo-url>
   cd android
   ```

2. **Set TMDB API Key** (for local testing)
   ```bash
   # Create local.properties or set environment variable
   export TMDB_API_KEY="your_api_key_here"
   ```

3. **Build Debug APK**
   ```bash
   ./gradlew assembleDebug
   ```

4. **Install on Device/Emulator**
   ```bash
   ./gradlew installDebug
   ```

5. **Run with Live Reload**
   ```bash
   ./gradlew run
   ```

## GitHub Actions CI/CD Setup

This project includes automated APK building and signing.

### Required Secrets

Add these secrets to your GitHub repository (Settings → Secrets → New repository secret):

1. **TMDB_API_KEY**
   - Get from: https://www.themoviedb.org/settings/api
   - Value: Your TMDB API key

2. **KEYSTORE_BASE64**
   - Generate keystore:
     ```bash
     keytool -genkey -v -keystore release.jks -keyalg RSA -keysize 2048 -validity 10000 -alias movie_app
     ```
   - Encode to Base64:
     ```bash
     base64 release.jks | tr -d '\n' | pbcopy  # macOS
     base64 release.jks > keystore_base64.txt  # Linux/Windows
     ```

3. **KEYSTORE_PASSWORD**
   - Password for the keystore file

4. **KEY_ALIAS**
   - Alias used when generating the keystore (e.g., `movie_app`)

5. **KEY_PASSWORD**
   - Password for the key alias

### Workflow Triggers

- **Automatic**: Commits/PRs to `main` or `develop` affecting `android/` directory
- **Manual**: Workflow dispatch with release notes input

### Output

The workflow generates:
- Signed APK: `MovieApp-release.apk`
- ZIP package with installation guide
- GitHub Release with downloadable assets

## Installation on Termux

### Via GitHub Release

1. **Download APK**
   ```bash
   cd ~/Downloads
   wget https://github.com/your-repo/releases/download/latest/MovieApp-release.apk
   ```

2. **Install with ADB**
   ```bash
   # In Termux
   pkg install android-tools
   adb install -r ~/Downloads/MovieApp-release.apk
   ```

### Via ZIP Package

1. **Extract ZIP**
   ```bash
   unzip MovieApp-v1.0.0.zip -d ~/Downloads/MovieApp
   cd ~/Downloads/MovieApp
   ```

2. **Follow INSTALL.md**
   ```bash
   cat INSTALL.md
   ```

## Dependencies

### Core Android
- `androidx.appcompat:appcompat:1.6.1`
- `androidx.core:core:1.12.0`
- `androidx.constraintlayout:constraintlayout:2.1.4`
- `com.google.android.material:material:1.11.0`

### Architecture
- `androidx.lifecycle:lifecycle-viewmodel:2.6.2`
- `androidx.lifecycle:lifecycle-livedata:2.6.2`
- `androidx.room:room-runtime:2.6.1`

### Networking
- `com.squareup.retrofit2:retrofit:2.10.0`
- `com.squareup.retrofit2:converter-gson:2.10.0`
- `com.squareup.okhttp3:okhttp:4.11.0`

### UI
- `com.github.bumptech.glide:glide:4.16.0` (Image loading)
- `androidx.paging:paging-runtime:3.2.1` (Pagination)

## ProGuard Configuration

Release builds are optimized with ProGuard:
- Code obfuscation enabled
- Resource shrinking enabled
- Keep rules for: Retrofit, Gson, Glide, Room, AndroidX

APK size: ~45-50 MB

## Troubleshooting

### Build Fails
```bash
# Clean and rebuild
./gradlew clean assembleDebug

# Check Gradle wrapper
./gradlew wrapper --gradle-version=8.2
```

### API Returns No Data
- Verify TMDB API key is correct
- Check internet connectivity
- Verify API rate limits not exceeded

### APK Installation Fails
- Uninstall existing app: `adb uninstall com.movieapp.explorer`
- Reinstall: `adb install -r app-release.apk`
- Check device storage space

### Gradle Sync Issues
```bash
# Update dependencies
./gradlew --refresh-dependencies build

# Clear Gradle cache
rm -rf ~/.gradle/caches
```

## Development

### Adding New Features

1. **Create database model** in `data/model/`
2. **Add API endpoints** to `TmdbApiService`
3. **Implement Repository** in `data/repository/`
4. **Create ViewModel** in `ui/viewmodel/`
5. **Build UI** (Fragment/Adapter) in `ui/`

### Code Style

- Follow Android official guidelines
- Use Java 11 features
- Null-safe code with annotations
- Proper naming conventions

### Testing

```bash
# Run unit tests
./gradlew test

# Run instrumented tests
./gradlew connectedAndroidTest
```

## Performance Optimization

- **Image Loading**: Glide with disk caching
- **Database**: Indexed queries, room migrations
- **Network**: OkHttp connection pooling, gzip compression
- **UI**: RecyclerView recycling, view binding

## Security

- API key injection via BuildConfig
- HTTPS enforced (no cleartext traffic)
- ProGuard obfuscation on release builds
- Database encryption-ready (Room)

## License

This project is licensed under the MIT License.

## Support

For issues or questions:
- Check GitHub Issues
- Refer to TMDB API docs: https://www.themoviedb.org/settings/api
- Android Docs: https://developer.android.com

## Building for Production

### Release APK

```bash
./gradlew assembleRelease
```

### Generate Signed APK

```bash
./gradlew bundleRelease  # For Play Store
./gradlew assembleRelease # For direct APK
```

### Upload to Releases

```bash
gh release create v1.0.0 ./app/build/outputs/apk/release/*.apk
```

---

**Created:** 2024
**Last Updated:** 2024
**Status:** Active Development
