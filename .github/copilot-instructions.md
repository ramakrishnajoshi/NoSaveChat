# NoSaveChat Android Application

NoSaveChat is an Android app built with Kotlin and Jetpack Compose that displays call log history and enables quick WhatsApp chat initiation. The app follows MVVM architecture and uses modern Android development practices.

**Always reference these instructions first and fallback to search or bash commands only when you encounter unexpected information that does not match the info here.**

## Working Effectively

### Prerequisites and Environment Setup
- **JDK 17 Required**: Verify with `java -version` - should show OpenJDK 17
- **Android SDK Required**: Environment variable `ANDROID_HOME` must point to valid Android SDK
- **Network Access**: Google Maven repository (`dl.google.com`) access required for Android dependencies
- **Git**: For version control and pre-commit hooks

### Bootstrap and Build Process
**CRITICAL**: All build commands require network access to Google Maven repository. If builds fail with plugin resolution errors like "Plugin [id: 'com.android.application'] was not found", this indicates network restrictions blocking `dl.google.com`.

**IMPORTANT LIMITATION**: In environments with restricted internet access (like some CI systems), Android builds may fail due to inability to download Android Gradle Plugin and dependencies from Google's Maven repository. If you encounter this issue, document the limitation and note that builds work in environments with full internet access.

#### Clean and Build Commands:
```bash
# Clean build directory (takes ~30 seconds)
./gradlew clean

# Build debug APK - NEVER CANCEL: takes 3-8 minutes for first build, 1-3 minutes incremental
# Set timeout to 15+ minutes for safety
./gradlew assembleDebug

# Comprehensive build with all checks - NEVER CANCEL: takes 10-15 minutes
# Set timeout to 30+ minutes
./gradlew build
```

#### Static Analysis and Quality Checks:
```bash
# Android Lint - checks Android-specific issues (takes 1-2 minutes)
./gradlew lintDebug

# Ktlint code style check (takes 1-2 minutes)
./gradlew ktlintCheck

# Auto-fix Kotlin code style issues
./gradlew ktlintFormat

# Detekt static analysis - checks code smells and complexity (takes 1-2 minutes)
./gradlew detekt
```

#### Testing:
```bash
# Unit tests - NEVER CANCEL: takes 1-3 minutes
./gradlew testDebugUnitTest

# All tests (unit + instrumented, requires connected device/emulator)
# Takes 5-10 minutes - NEVER CANCEL: Set timeout to 20+ minutes
./gradlew test
```

### Git Hooks Setup
**Always set up pre-commit hooks after cloning:**
```bash
git config core.hooksPath config/git-hooks
chmod +x config/git-hooks/pre-commit
```

## Validation Requirements

### Manual Testing Scenarios
**CRITICAL**: After making any changes, always run through complete validation:

1. **Build Validation**:
   - Run `./gradlew clean build` successfully
   - Verify APK is generated in `app/build/outputs/apk/debug/`

2. **Code Quality Validation**:
   - Run `./gradlew ktlintCheck` - must pass with no errors
   - Run `./gradlew detekt` - must pass with no errors
   - Run `./gradlew lintDebug` - must pass with no errors

3. **Pre-commit Validation**:
   - Test git hooks work: `git add . && git commit -m "test"` (then reset)
   - Hooks run ktlint and detekt automatically

### Required Before Committing
**Always run these commands before pushing changes:**
```bash
./gradlew ktlintFormat  # Auto-fix style issues
./gradlew ktlintCheck   # Verify style compliance
./gradlew detekt        # Check code quality
./gradlew lintDebug     # Check Android issues
```

## Common Tasks and Troubleshooting

### Network Issues
- **Plugin resolution failures**: Error "Plugin [id: 'com.android.application'] was not found" indicates blocked access to `dl.google.com`
- **Root cause**: Google Maven repository access required for Android Gradle Plugin dependencies
- **Workaround**: If in restricted environment, document commands but note network requirement
- **Alternative**: Use Android Studio with pre-cached dependencies, or work in environment with full internet access
- **Validation**: Test with `curl -I "https://dl.google.com/dl/android/maven2/"` - should return HTTP 200

### Build Failures
- **Gradle daemon issues**: Run `./gradlew --stop` then retry
- **Dependency conflicts**: Run `./gradlew clean` then rebuild
- **Memory issues**: Increase heap size in `gradle.properties`

### Code Quality Issues
- **Ktlint failures**: Run `./gradlew ktlintFormat` to auto-fix
- **Detekt warnings**: Review `build/reports/detekt/detekt.html` for details
- **Android Lint**: Review `app/build/reports/lint-results-debug.html`

## Project Structure Navigation

### Key Directories
```
app/src/main/java/com/example/nosavechat/
├── MainActivity.kt                    # App entry point
├── viewmodel/CallLogViewModel.kt      # MVVM ViewModel
├── repository/CallLogRepository.kt    # Data access layer
├── model/CallLogItem.kt              # Domain model
├── ui/
│   ├── screens/CallLogScreen.kt      # Main screen composable
│   ├── components/CallLogItem.kt     # Call log item UI
│   └── theme/                        # Material 3 theming
└── util/WhatsAppUtil.kt              # WhatsApp integration
```

### Important Files
- `build.gradle.kts` (root): Project-level build configuration, static analysis setup
- `app/build.gradle.kts`: App module configuration, dependencies
- `gradle/libs.versions.toml`: Centralized dependency versions
- `config/detekt/detekt.yml`: Detekt rules configuration
- `.github/workflows/android-ci.yml`: CI/CD pipeline
- `docs/`: Comprehensive project documentation

### Common File Patterns
- **After changing ViewModels**: Always check corresponding Repository and UI files
- **After changing Models**: Check Repository, ViewModel, and UI components
- **After adding dependencies**: Update `gradle/libs.versions.toml` first

## CI/CD Integration

### GitHub Actions Workflow
The CI pipeline (`.github/workflows/android-ci.yml`) runs:
1. Android Lint (`./gradlew lintDebug`)
2. Ktlint Check (`./gradlew ktlintCheck`) 
3. Detekt Analysis (`./gradlew detekt`)
4. Build Debug APK (`./gradlew assembleDebug`)

**Always ensure local validation passes before pushing to prevent CI failures.**

### Expected Timing
- **NEVER CANCEL builds or tests** - they may take significant time
- Clean: 30 seconds
- Incremental build: 1-3 minutes
- Full build: 3-8 minutes
- Complete CI pipeline: 10-15 minutes
- **Always set timeouts to 2x expected time**

## Frequently Used Commands Reference

### Quick Development Workflow
```bash
# Standard development cycle
./gradlew clean                    # Clean previous builds
./gradlew assembleDebug           # Build debug APK
./gradlew ktlintFormat            # Fix style issues
./gradlew ktlintCheck detekt      # Verify code quality
```

### Repository Status
```bash
# Check what's available
./gradlew tasks                   # List available tasks
./gradlew dependencies           # Show dependency tree
git status                       # Check uncommitted changes
```

### Installation and Deployment
```bash
# Install debug APK (requires connected Android device/emulator)
./gradlew installDebug

# Uninstall app
./gradlew uninstallDebug
```

Remember: This is an Android project requiring specific environment setup. Always verify prerequisites before attempting builds, and expect longer build times on first run while Gradle downloads dependencies.