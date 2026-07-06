#!/bin/bash

# Movie Explorer APK Installation Script for Termux
# This script automates APK installation and setup on Android via Termux

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Constants
APK_NAME="MovieApp-release.apk"
PACKAGE_NAME="com.movieapp.explorer"
INSTALL_DIR="$HOME/Downloads"

# Functions
print_header() {
    echo -e "${BLUE}========================================${NC}"
    echo -e "${BLUE}$1${NC}"
    echo -e "${BLUE}========================================${NC}"
}

print_success() {
    echo -e "${GREEN}✓ $1${NC}"
}

print_error() {
    echo -e "${RED}✗ $1${NC}"
}

print_warning() {
    echo -e "${YELLOW}⚠ $1${NC}"
}

print_info() {
    echo -e "${BLUE}ℹ $1${NC}"
}

# Check prerequisites
check_prerequisites() {
    print_header "Checking Prerequisites"
    
    # Check if adb is available
    if ! command -v adb &> /dev/null; then
        print_error "adb not found! Installing android-tools..."
        pkg update
        pkg install -y android-tools
    fi
    print_success "adb is available"
    
    # Check if adb devices can be detected
    print_info "Checking connected devices..."
    adb_count=$(adb devices | grep -v "List" | grep -v "^$" | wc -l)
    
    if [ $adb_count -lt 1 ]; then
        print_warning "No devices detected. Continuing with installation..."
        print_info "Make sure device is properly connected"
    else
        print_success "Device(s) detected"
        adb devices
    fi
}

# Download APK from GitHub
download_apk() {
    print_header "Downloading APK"
    
    if [ ! -f "$INSTALL_DIR/$APK_NAME" ]; then
        print_info "APK not found. Please provide one of the following:"
        echo ""
        echo "Option 1: Download from GitHub Release"
        echo "  - Visit: https://github.com/your-repo/releases/latest"
        echo "  - Download: $APK_NAME"
        echo "  - Save to: $INSTALL_DIR/"
        echo ""
        echo "Option 2: Provide via URL (if available)"
        echo "  ./termux-install.sh --url <apk-url>"
        echo ""
        echo "Option 3: Place APK file and run again"
        print_error "APK file not found at $INSTALL_DIR/$APK_NAME"
        exit 1
    fi
    
    print_success "APK found at $INSTALL_DIR/$APK_NAME"
    
    # Verify APK file size
    apk_size=$(du -h "$INSTALL_DIR/$APK_NAME" | cut -f1)
    print_info "APK size: $apk_size"
    
    # Calculate MD5
    print_info "Verifying APK integrity..."
    md5=$(md5sum "$INSTALL_DIR/$APK_NAME" | awk '{print $1}')
    print_info "MD5: $md5"
}

# Install APK
install_apk() {
    print_header "Installing APK"
    
    print_info "Preparing installation..."
    
    # Check if app is already installed
    if adb shell pm list packages | grep -q "$PACKAGE_NAME"; then
        print_warning "App already installed. Will upgrade..."
        print_info "Uninstalling previous version..."
        adb uninstall "$PACKAGE_NAME" || true
    fi
    
    # Install the APK
    print_info "Installing $APK_NAME..."
    if adb install -r "$INSTALL_DIR/$APK_NAME"; then
        print_success "APK installed successfully!"
    else
        print_error "Failed to install APK"
        print_info "Troubleshooting:"
        echo "  1. Ensure device has sufficient storage"
        echo "  2. Try: adb uninstall $PACKAGE_NAME"
        echo "  3. Then retry: ./termux-install.sh"
        exit 1
    fi
}

# Grant permissions
grant_permissions() {
    print_header "Granting Permissions"
    
    print_info "Granting required permissions..."
    
    # Required permissions
    permissions=(
        "android.permission.INTERNET"
        "android.permission.ACCESS_NETWORK_STATE"
    )
    
    for perm in "${permissions[@]}"; do
        adb shell pm grant "$PACKAGE_NAME" "$perm" || true
        print_success "Granted: $perm"
    done
}

# Launch app
launch_app() {
    print_header "Launching App"
    
    print_info "Launching $PACKAGE_NAME..."
    
    adb shell am start -n "$PACKAGE_NAME/.ui.MainActivity"
    
    sleep 2
    
    print_success "App launched!"
    print_info "If the app doesn't appear, check device notification bar"
}

# Verify installation
verify_installation() {
    print_header "Verifying Installation"
    
    # Check if package is installed
    if adb shell pm list packages | grep -q "$PACKAGE_NAME"; then
        print_success "Package installed and verified"
    else
        print_error "Package verification failed"
        exit 1
    fi
    
    # Get app version
    version=$(adb shell dumpsys package "$PACKAGE_NAME" | grep "versionName=" | cut -d'=' -f2)
    print_info "Installed version: $version"
    
    # Check app size
    size=$(adb shell pm dump "$PACKAGE_NAME" | grep "size=" | cut -d'=' -f2)
    print_info "App size: $size bytes"
}

# View app logs
view_logs() {
    print_header "Live App Logs"
    print_info "Press Ctrl+C to stop"
    sleep 1
    adb logcat | grep --color=auto "MovieApp\|${PACKAGE_NAME}"
}

# Uninstall app
uninstall_app() {
    print_header "Uninstalling App"
    
    print_warning "Removing $PACKAGE_NAME..."
    
    if adb uninstall "$PACKAGE_NAME"; then
        print_success "App uninstalled successfully"
    else
        print_error "Failed to uninstall app"
        exit 1
    fi
}

# Show help
show_help() {
    cat << EOF
Usage: $0 [OPTION]

Movie Explorer - Termux Installation Script

OPTIONS:
    install         Install APK (default)
    uninstall       Remove app from device
    logs            View app logs in real-time
    launch          Launch installed app
    verify          Verify installation status
    help            Show this help message

EXAMPLES:
    $0 install         # Install APK with all steps
    $0 logs             # View live app logs
    $0 uninstall        # Remove app from device
    $0 verify           # Check installation status

PREPARATION:
    1. Download APK from GitHub Release
    2. Save to: ~/Downloads/MovieApp-release.apk
    3. Run: $0

REQUIREMENTS:
    - Termux app installed
    - android-tools package (auto-installed if missing)
    - Device with USB debugging enabled
    - APK file available

TROUBLESHOOTING:
    - If adb not found: pkg install android-tools
    - If no devices: Check USB debugging on device
    - If install fails: Try 'adb kill-server' then retry
    - If permission denied: Grant permissions manually

For more info: https://github.com/your-repo/blob/main/README.md

EOF
}

# Main script
main() {
    print_header "Movie Explorer - Termux Installer"
    print_info "Version 1.0"
    
    # Parse command line arguments
    case "${1:-install}" in
        install)
            check_prerequisites
            download_apk
            install_apk
            grant_permissions
            verify_installation
            launch_app
            print_header "Installation Complete!"
            print_success "Movie Explorer is ready to use"
            print_info "Open the app from your app drawer or run: termux-install.sh launch"
            ;;
        uninstall)
            uninstall_app
            ;;
        logs)
            view_logs
            ;;
        launch)
            launch_app
            ;;
        verify)
            check_prerequisites
            verify_installation
            ;;
        help)
            show_help
            ;;
        *)
            print_error "Unknown option: $1"
            show_help
            exit 1
            ;;
    esac
}

# Run main script
main "$@"
