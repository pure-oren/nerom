# Android App Setup Guide

Complete setup instructions for building and deploying the native Android Movie Explorer app.

## Table of Contents

1. [Prerequisites](#prerequisites)
2. [TMDB API Key](#tmdb-api-key)
3. [Generate Keystore](#generate-keystore)
4. [GitHub Secrets Configuration](#github-secrets-configuration)
5. [Local Build & Testing](#local-build--testing)
6. [GitHub Actions Workflow](#github-actions-workflow)
7. [Termux Installation](#termux-installation)

---

## Prerequisites

### System Requirements

- **macOS/Linux/Windows** with terminal access
- **Java 11+**: `java -version`
- **Android SDK**: Installed via Android Studio or CLI
- **Git**: For version control
- **Base64 utility**: Built-in on macOS/Linux

### Check Environment

```bash
# Check Java version (must be 11+)
java -version

# If not installed, install JDK 11:
# macOS: brew install openjdk@11
# Linux: sudo apt-get install openjdk-11-jdk
# Windows: Download from adoptopenjdk.net
```

---

## TMDB API Key

### Get Free API Key

1. **Register Account**
   - Visit: https://www.themoviedb.org/signup
   - Create account with email

2. **Request API Key**
   - Go to: https://www.themoviedb.org/settings/api
   - Click "Create" or "Request an API Key"
   - Select "Developer" option
   - Accept terms and fill questionnaire
   - Submit request

3. **Verify Email**
   - Check email for confirmation
   - API key will be shown after approval

4. **Copy API Key**
   - Go to: https://www.themoviedb.org/settings/api
   - Copy your API key (looks like: `a1b2c3d4e5f6g7h8i9j0k1l2m3n4`)

⚠️ **Keep this key secret!** Do not commit to GitHub.

---

## Generate Keystore

### Step 1: Create Keystore

```bash
# Generate keystore file (valid for 10 years)
keytool -genkey -v -keystore release.jks \
  -keyalg RSA \
  -keysize 2048 \
  -validity 10000 \
  -alias movie_app
```

**Follow the prompts:**

```
Enter keystore password: [Enter secure password]
Re-enter new password: [Confirm password]
First and last name: [Your Name or Company]
Organizational unit: [Your Org]
Organization: [Your Company]
City or Locality: [City]
State or Province: [State]
Two-letter country code: [e.g., US]
```

**Remember these credentials:**
- **Keystore Password**: Password for the keystore file
- **Key Alias**: `movie_app`
- **Key Password**: Same as keystore password (if you pressed Enter when prompted)

### Step 2: Store Keystore Safely

```bash
# Move keystore to a safe location
mv release.jks ~/secure/location/release.jks

# Set restrictive permissions
chmod 600 ~/secure/location/release.jks

# Backup keystore
cp release.jks release.jks.backup
```

⚠️ **CRITICAL**: Keep the keystore file safe. Losing it means you can't update the app on Play Store!

---

## GitHub Secrets Configuration

### Step 1: Navigate to Repository Settings

1. Go to your GitHub repository
2. Click **Settings** (top right)
3. Select **Secrets and variables** → **Actions**
4. Click **New repository secret**

### Step 2: Add TMDB_API_KEY

1. **Name**: `TMDB_API_KEY`
2. **Value**: Paste your TMDB API key from Step 1
3. Click **Add secret**

### Step 3: Add KEYSTORE_BASE64

Convert keystore to Base64:

**macOS/Linux:**
```bash
# Create base64 encoded keystore
base64 release.jks > keystore_base64.txt

# Copy to clipboard
cat keystore_base64.txt | pbcopy  # macOS
# or
cat keystore_base64.txt | xclip   # Linux
```

**Windows (PowerShell):**
```powershell
# Create base64 encoded keystore
[Convert]::ToBase64String([IO.File]::ReadAllBytes("release.jks")) | Out-Clipboard
```

**Add to GitHub:**

1. **Name**: `KEYSTORE_BASE64`
2. **Value**: Paste entire base64 content (long string)
3. Click **Add secret**

### Step 4: Add KEYSTORE_PASSWORD

1. **Name**: `KEYSTORE_PASSWORD`
2. **Value**: The password you set when creating the keystore
3. Click **Add secret**

### Step 5: Add KEY_ALIAS

1. **Name**: `KEY_ALIAS`
2. **Value**: `movie_app`
3. Click **Add secret**

### Step 6: Add KEY_PASSWORD

1. **Name**: `KEY_PASSWORD`
2. **Value**: The key password (usually same as keystore password)
3. Click **Add secret**

### Verify Secrets

```bash
# List GitHub secrets (requires gh CLI)
gh secret list -R username/repo
```

---

## Local Build & Testing

### Clone Repository

```bash
git clone https://github.com/your-username/your-repo.git
cd your-repo/android
```

### Set Local Environment Variables

**macOS/Linux:**
```bash
# Add to ~/.bash_profile or ~/.zshrc
export TMDB_API_KEY="your_api_key_here"
```

**Windows (PowerShell):**
```powershell
$env:TMDB_API_KEY = "your_api_key_here"
```

### Build Debug APK

```bash
# Make Gradle executable
chmod +x gradlew

# Build debug APK (no signing required)
./gradlew assembleDebug

# APK location: app/build/outputs/apk/debug/app-debug.apk
```

### Install on Emulator

```bash
# List available devices
./gradlew connectedDevices

# Install APK
./gradlew installDebug
```

### Build Release APK (Local)

```bash
# Create local build config
export TMDB_API_KEY="your_api_key"
export KEYSTORE_FILE="$(pwd)/release.jks"
export KEYSTORE_PASSWORD="your_keystore_password"
export KEY_ALIAS="movie_app"
export KEY_PASSWORD="your_key_password"

# Build release APK
./gradlew assembleRelease

# APK location: app/build/outputs/apk/release/app-release.apk
```

### Test APK Installation

```bash
# On Android device/emulator
adb install -r app/build/outputs/apk/debug/app-debug.apk

# Launch app
adb shell am start -n com.movieapp.explorer/.ui.MainActivity

# View logs
adb logcat | grep "MovieApp"
```

---

## GitHub Actions Workflow

### Trigger Builds

**Automatic Trigger:**
- Push to `main` or `develop` branch
- Changes in `android/` directory

**Manual Trigger:**
1. Go to repository
2. Click **Actions** tab
3. Select **Build Android APK** workflow
4. Click **Run workflow**
5. Enter optional release notes
6. Click **Run workflow**

### Monitor Build

1. **Go to Actions tab** → **Build Android APK**
2. **Click the running workflow**
3. **Expand job steps** to see progress
4. **View logs** for any errors

### Download Build Artifacts

1. **Build completes** ✓
2. **Go to workflow run** page
3. **Scroll to "Artifacts"** section
4. **Download** `MovieApp-APK-{number}` ZIP

### Access Release Assets

1. **Go to Releases** tab
2. **Latest release** at top
3. **Download Assets:**
   - `MovieApp-release-{number}.apk`
   - `MovieApp-v1.0.0-build-{number}.zip`

---

## Termux Installation

### Prerequisites

```bash
# Install on Android device from Play Store/F-Droid
# Open Termux app

# Update packages
pkg update && pkg upgrade

# Install Android tools
pkg install android-tools

# Verify installation
adb version
```

### Installation Methods

#### Method 1: From GitHub Release

```bash
# Download APK
cd ~/storage/downloads
wget https://github.com/your-repo/releases/download/latest/MovieApp-release.apk

# Install
adb install -r MovieApp-release.apk

# Verify installation
adb shell pm list packages | grep movieapp
```

#### Method 2: From ZIP Package

```bash
# Extract ZIP
unzip MovieApp-v1.0.0.zip -d ~/MovieApp
cd ~/MovieApp

# View installation guide
cat INSTALL.md

# Install APK
adb install -r MovieApp-release.apk
```

#### Method 3: Direct Installation

```bash
# Via file manager (easiest)
# 1. Download APK to device
# 2. Open file manager
# 3. Navigate to Downloads
# 4. Tap MovieApp-release.apk
# 5. Follow installation prompts
```

### Launch Application

```bash
# After installation, launch from:
# - App drawer (Movie Explorer icon)
# - Or via adb:
adb shell am start -n com.movieapp.explorer/.ui.MainActivity
```

### Verify Installation

```bash
# Check app is installed
adb shell pm list packages | grep explorer

# View app info
adb shell dumpsys package com.movieapp.explorer

# View app logs
adb logcat | grep MovieApp
```

### Uninstall

```bash
adb uninstall com.movieapp.explorer
```

---

## Troubleshooting

### Build Fails - "API key not found"

**Solution:**
```bash
# Verify environment variable
echo $TMDB_API_KEY  # Should print your key

# Or add to build.gradle.kts:
buildConfigField("String", "TMDB_API_KEY", "\"your_key_here\"")
```

### Build Fails - "Keystore not found"

**Solution:**
```bash
# Verify keystore exists
ls -la release.jks

# For CI/CD, ensure KEYSTORE_BASE64 secret is set correctly
# Verify base64 encoding:
echo $KEYSTORE_BASE64 | base64 -d > test.jks
```

### APK Installation Fails

**Solution:**
```bash
# Uninstall existing version
adb uninstall com.movieapp.explorer

# Reinstall
adb install -r app-release.apk

# Check device storage
adb shell df /data

# If storage full, clear cache
adb shell pm clear com.movieapp.explorer
```

### "No API response" in App

**Solutions:**
1. Check internet connection: `adb shell ping 8.8.8.8`
2. Verify API key: Go to https://www.themoviedb.org/settings/api
3. Check API rate limits: TMDB allows 40 requests/10 seconds
4. Clear app data: `adb shell pm clear com.movieapp.explorer`

### Gradle Sync Errors

**Solution:**
```bash
# Clean gradle cache
rm -rf ~/.gradle/caches

# Update gradle wrapper
./gradlew wrapper --gradle-version=8.2

# Sync again
./gradlew --refresh-dependencies build
```

---

## Security Best Practices

### ✓ DO:
- Keep keystore file secure (encrypted backup)
- Use strong keystore password (12+ characters)
- Rotate API keys if compromised
- Never commit secrets to Git
- Use GitHub Secrets, not environment files

### ✗ DON'T:
- Commit keystore to Git
- Share API key in messages/code
- Use same password for keystore and key
- Expose secrets in build logs
- Hardcode credentials in source files

---

## Next Steps

1. **Build APK**: Run workflow and download APK
2. **Test Locally**: Install on device/emulator
3. **Publish**: Share APK or publish to Play Store
4. **Monitor**: Track usage and errors

---

## Useful Resources

- **TMDB API**: https://www.themoviedb.org/settings/api
- **Android Docs**: https://developer.android.com
- **Gradle Docs**: https://gradle.org/documentation
- **GitHub Actions**: https://docs.github.com/en/actions
- **Termux Wiki**: https://wiki.termux.com

---

## Support

If you encounter issues:

1. **Check logs**: `adb logcat | grep -E "ERROR|Exception"`
2. **Read error message** carefully
3. **Search GitHub Issues** for similar problems
4. **Check build configuration** in `app/build.gradle.kts`
5. **Review secrets** in GitHub Settings

---

**Last Updated:** 2024
**Status:** Ready for Production
