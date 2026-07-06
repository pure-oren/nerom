# Android Movie Explorer - Deployment Checklist

Complete this checklist to successfully build, sign, and deploy your native Android app.

## Pre-Deployment (Complete Once)

### 1. Setup Development Environment

- [ ] Install JDK 11 or higher
  ```bash
  java -version  # Verify Java 11+
  ```

- [ ] Install Android Studio or Android SDK
  ```bash
  which adb  # Verify ADB installed
  ```

- [ ] Clone repository
  ```bash
  git clone <repo-url>
  cd your-repo
  ```

- [ ] Verify Gradle setup
  ```bash
  cd android && chmod +x gradlew
  ./gradlew --version
  ```

### 2. Get API Credentials

- [ ] Create TMDB account: https://www.themoviedb.org/signup
- [ ] Request API key: https://www.themoviedb.org/settings/api
- [ ] Wait for approval
- [ ] Copy API key to secure location
- [ ] Note: This is **different for each developer**

### 3. Generate Signing Keystore

- [ ] Generate keystore file:
  ```bash
  keytool -genkey -v -keystore release.jks \
    -keyalg RSA -keysize 2048 -validity 10000 \
    -alias movie_app
  ```

- [ ] Save keystore password securely (you'll need it multiple times)
- [ ] Verify keystore created:
  ```bash
  ls -la release.jks
  ```

- [ ] **IMPORTANT**: Backup keystore to secure location
  ```bash
  cp release.jks ~/backup/release.jks
  chmod 600 ~/backup/release.jks
  ```

- [ ] Never commit keystore to Git
- [ ] Add to .gitignore:
  ```
  release.jks
  *.jks
  local.properties
  ```

### 4. Setup GitHub Repository

- [ ] Create or configure GitHub repository
- [ ] Enable GitHub Actions (usually default)
- [ ] Go to Settings → Secrets and variables → Actions

### 5. Add GitHub Secrets

- [ ] Add `TMDB_API_KEY`
  - Value: Your TMDB API key

- [ ] Encode keystore to Base64:
  ```bash
  # macOS
  base64 release.jks | pbcopy
  
  # Linux
  base64 release.jks | xclip
  
  # Windows PowerShell
  [Convert]::ToBase64String([IO.File]::ReadAllBytes("release.jks")) | Out-Clipboard
  ```

- [ ] Add `KEYSTORE_BASE64`
  - Value: Entire base64 string (very long!)

- [ ] Add `KEYSTORE_PASSWORD`
  - Value: Your keystore password

- [ ] Add `KEY_ALIAS`
  - Value: `movie_app`

- [ ] Add `KEY_PASSWORD`
  - Value: Your key password (usually same as keystore)

- [ ] Verify secrets (command line):
  ```bash
  gh secret list -R username/repo
  ```

---

## Local Build & Testing

### 1. Build Debug APK

- [ ] Navigate to android directory:
  ```bash
  cd android
  ```

- [ ] Build debug APK:
  ```bash
  ./gradlew assembleDebug
  ```

- [ ] Verify APK created:
  ```bash
  ls -la app/build/outputs/apk/debug/app-debug.apk
  ```

- [ ] Note file size: ~50-60 MB (debug includes extra data)

### 2. Test on Emulator/Device

- [ ] Connect device with USB debugging enabled
- [ ] Verify connection:
  ```bash
  adb devices
  ```

- [ ] Install APK:
  ```bash
  ./gradlew installDebug
  ```

- [ ] Or via direct install:
  ```bash
  adb install -r app/build/outputs/apk/debug/app-debug.apk
  ```

- [ ] Launch app:
  ```bash
  adb shell am start -n com.movieapp.explorer/.ui.MainActivity
  ```

- [ ] Test features:
  - [ ] Movies tab loads content
  - [ ] TV shows tab loads content
  - [ ] Scroll through content
  - [ ] View is responsive
  - [ ] No crashes in logs

- [ ] Check logs for errors:
  ```bash
  adb logcat | grep -E "MovieApp|Error|Exception"
  ```

- [ ] Uninstall for clean test:
  ```bash
  adb uninstall com.movieapp.explorer
  ```

---

## CI/CD Pipeline Deployment

### 1. Prepare Repository

- [ ] Commit all changes locally
- [ ] Push to feature branch
- [ ] Verify branch is clean:
  ```bash
  git status
  ```

- [ ] Create pull request (optional but recommended)
- [ ] Get code review if required
- [ ] Merge to `main` branch

### 2. Trigger Build

**Automatic:**
- [ ] Push to `main` branch with changes in `android/`
- [ ] Workflow triggers automatically
- [ ] Monitor on GitHub → Actions

**Manual:**
- [ ] Go to GitHub repo → Actions tab
- [ ] Select "Build Android APK" workflow
- [ ] Click "Run workflow"
- [ ] Select `main` branch
- [ ] Optionally add release notes
- [ ] Click "Run workflow"

### 3. Monitor Build

- [ ] Go to Actions tab
- [ ] Click running workflow
- [ ] Watch status in real-time
- [ ] Check for errors in logs:
  - [ ] Java compilation errors
  - [ ] Gradle build errors
  - [ ] Signing errors
  - [ ] Upload errors

- [ ] If build fails:
  - [ ] Click on failed step
  - [ ] Read error message carefully
  - [ ] Fix issue locally
  - [ ] Push fix and retry

- [ ] Build should complete in 5-10 minutes
- [ ] Check for success checkmark ✓

### 4. Access Build Artifacts

- [ ] Go to workflow run page
- [ ] Scroll down to "Artifacts" section
- [ ] Download `MovieApp-APK-{number}` ZIP
- [ ] Extract ZIP:
  ```bash
  unzip MovieApp-APK-*.zip
  ```

- [ ] Verify contents:
  - [ ] `MovieApp-release.apk`
  - [ ] `INSTALL.md`
  - [ ] `BUILD_INFO.txt`

### 5. Access Release

- [ ] Go to GitHub Releases tab
- [ ] Find latest release (top of list)
- [ ] Verify release has artifacts:
  - [ ] `MovieApp-release-*.apk`
  - [ ] `MovieApp-v1.0.0-*.zip`

- [ ] Download and test both

---

## Installation Verification

### 1. Install from Artifacts

- [ ] Download APK from workflow artifacts
- [ ] Transfer to device:
  ```bash
  adb push MovieApp-release.apk /sdcard/Download/
  ```

- [ ] Install via ADB:
  ```bash
  adb install -r MovieApp-release.apk
  ```

- [ ] Or install directly on device:
  - [ ] Open file manager
  - [ ] Navigate to Downloads
  - [ ] Tap APK file
  - [ ] Grant permissions
  - [ ] Install

### 2. Test Production APK

- [ ] App launches successfully
- [ ] No crashes on startup
- [ ] API loads data:
  - [ ] Popular movies visible
  - [ ] Popular TV shows visible
  - [ ] Ratings display correctly
  - [ ] Images load from TMDB

- [ ] Offline functionality:
  - [ ] Load content while online
  - [ ] Enable airplane mode
  - [ ] App still shows cached content

- [ ] Performance:
  - [ ] Smooth scrolling
  - [ ] Fast image loading
  - [ ] No freezing

- [ ] Uninstall successfully:
  ```bash
  adb uninstall com.movieapp.explorer
  ```

### 3. Test on Multiple Devices

- [ ] Test on different Android versions:
  - [ ] Android 8 (API 26)
  - [ ] Android 10 (API 29)
  - [ ] Android 12 (API 31)
  - [ ] Android 14 (API 34)

- [ ] Test on different screen sizes:
  - [ ] Phone (small)
  - [ ] Phone (large)
  - [ ] Tablet
  - [ ] Landscape orientation

---

## Release Distribution

### 1. GitHub Releases

- [ ] Latest release available on GitHub
- [ ] Download links working:
  - [ ] APK download
  - [ ] ZIP download

- [ ] Release notes visible
- [ ] Build info accessible

### 2. Termux Installation

- [ ] Download APK to device
- [ ] Open Termux
- [ ] Install android-tools:
  ```bash
  pkg install android-tools
  ```

- [ ] Install APK:
  ```bash
  adb install -r ~/Downloads/MovieApp-release.apk
  ```

- [ ] App launches in Termux

### 3. Documentation

- [ ] QUICK_START.md updated with latest version
- [ ] ANDROID_SETUP_GUIDE.md is current
- [ ] ANDROID_PROJECT_SUMMARY.md reflects current state
- [ ] README.md in android/ folder is accurate

---

## Post-Deployment Monitoring

### 1. Track Installation

- [ ] GitHub release shows download count
- [ ] Artifacts storage quota monitored
- [ ] Release notes saved for history

### 2. Collect Feedback

- [ ] Monitor issues on GitHub
- [ ] Check for crash reports
- [ ] Track user feedback

### 3. Monitor Crashes

- [ ] Setup crash reporting (optional):
  - [ ] Sentry integration
  - [ ] Firebase Crashlytics
  - [ ] Or manual logs

- [ ] Monitor logs for errors:
  ```bash
  adb logcat -c  # Clear logs
  # Use app...
  adb logcat > logcat_output.txt  # Save logs
  ```

### 4. Performance Monitoring

- [ ] Track app size:
  - [ ] Current: ~45-50 MB
  - [ ] Monitor growth

- [ ] Test on slow networks:
  - [ ] 3G simulation
  - [ ] API timeout scenarios

---

## Maintenance

### 1. Regular Updates

- [ ] Update dependencies monthly:
  ```bash
  # Check for updates
  ./gradlew dependencyUpdates
  ```

- [ ] Update Android SDK
- [ ] Update Build tools

### 2. Security

- [ ] Rotate API keys if compromised
- [ ] Update ProGuard rules for new libraries
- [ ] Audit GitHub Secrets access

### 3. Version Management

- [ ] Increment version code:
  ```gradle
  versionCode = 2  // Increment for each release
  versionName = "1.0.1"
  ```

- [ ] Tag releases in Git:
  ```bash
  git tag -a v1.0.1 -m "Release 1.0.1"
  git push origin v1.0.1
  ```

### 4. Backup

- [ ] Backup keystore regularly:
  ```bash
  cp release.jks ~/encrypted-backup/
  ```

- [ ] Backup GitHub Secrets list (encrypted)
- [ ] Document all API keys location (secure)

---

## Troubleshooting During Deployment

### Build Fails in CI/CD

**Check logs:**
- [ ] Go to failed workflow step
- [ ] Read full error message
- [ ] Search error on Stack Overflow

**Common issues:**
- [ ] TMDB_API_KEY not set → Add to Secrets
- [ ] Keystore not found → Verify KEYSTORE_BASE64
- [ ] Signing error → Verify KEY_ALIAS and passwords
- [ ] Gradle error → Check gradle version

### Installation Fails

```bash
# Try these steps in order:
adb kill-server
adb start-server
adb devices
adb uninstall com.movieapp.explorer
adb install -r MovieApp-release.apk
```

### App Crashes After Install

- [ ] Check API key is valid
- [ ] Verify internet connection
- [ ] Clear app cache: `adb shell pm clear com.movieapp.explorer`
- [ ] Check logs: `adb logcat | grep -i error`

### No Data Shows in App

- [ ] Verify API key works:
  ```bash
  curl "https://api.themoviedb.org/3/movie/popular?api_key=YOUR_KEY"
  ```

- [ ] Check network connection
- [ ] Verify API response status (not rate limited)
- [ ] Clear app data and retry

---

## Sign-Off Checklist

Before marking deployment complete:

- [ ] Local build and test passed
- [ ] GitHub CI/CD pipeline configured
- [ ] All secrets added to GitHub
- [ ] Build artifact successfully generated
- [ ] APK installed and tested
- [ ] Features working as expected
- [ ] Documentation updated
- [ ] Release published
- [ ] Backup verified

---

## Final Notes

- **Keep keystore safe** - Losing it breaks Play Store updates
- **Rotate secrets** - If ever compromised
- **Test thoroughly** - Especially after updates
- **Document changes** - For future maintenance
- **Monitor users** - Track crashes and feedback

---

**Status**: Ready to Deploy
**Last Checklist**: 2024
**Next Review**: After first release

Good luck with your deployment! 🎉
