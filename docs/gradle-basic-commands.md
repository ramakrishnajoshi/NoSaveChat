## Common Gradle Commands

These commands should be run from the root directory of the project using your terminal. The `./gradlew` script ensures you use the correct Gradle version defined for this project.

### Building & Cleaning

* **Clean Build Directory:** Removes previous build outputs.
    ```bash
    ./gradlew clean
    ```

* **Assemble All Outputs:** Compiles code and creates packages (APK/AAB for apps, AAR for libraries) for *all* build variants (e.g., debug, release).
    ```bash
    ./gradlew assemble
    ```
    * Outputs are typically in `[module]/build/outputs/apk/` or `[module]/build/outputs/bundle/`.

* **Assemble Debug Build (Android App):** Creates the debug APK (or AAB).
    ```bash
    ./gradlew assembleDebug
    ```

* **Assemble Release Build (Android App):** Creates the release APK or, more commonly now, the release AAB (Android App Bundle).
    ```bash
    ./gradlew assembleRelease
    ```

* **Bundle Release Build (Android App):** Specifically creates the release AAB.
    ```bash
    ./gradlew bundleRelease
    ```

* **Build All & Run Checks:** Compiles, runs checks (like Lint), runs unit tests, and assembles all variants. Use this for a comprehensive verification.
    ```bash
    ./gradlew build
    ```

### Testing

* **Run Unit Tests:** Executes local JVM unit tests for all variants.
    ```bash
    ./gradlew test
    ```

* **Run Debug Unit Tests:** Executes local JVM unit tests specifically for the debug variant.
    ```bash
    ./gradlew testDebugUnitTest
    ```

* **Run Instrumented Tests:** Executes tests requiring a connected Android device or emulator for all variants.
    ```bash
    ./gradlew connectedCheck
    ```

* **Run Debug Instrumented Tests:** Executes instrumented tests specifically for the debug variant on a connected device/emulator.
    ```bash
    ./gradlew connectedDebugAndroidTest
    ```

### Installation (Android)

* **Install Debug App:** Builds and installs the debug variant onto a connected device/emulator.
    ```bash
    ./gradlew installDebug
    ```

* **Install Release App:** Builds and installs the release variant.
    ```bash
    ./gradlew installRelease
    ```

* **Uninstall Debug App:** Uninstalls the debug variant.
    ```bash
    ./gradlew uninstallDebug
    ```

* **Uninstall Release App:** Uninstalls the release variant.
    ```bash
    ./gradlew uninstallRelease
    ```

* **Uninstall All Variants:** Uninstalls all variants associated with the app's package ID.
    ```bash
    ./gradlew uninstallAll
    ```

### Information & Specific Tasks

* **List Available Tasks:** Shows the main tasks you can run. Use `--all` for a more exhaustive list.
    ```bash
    ./gradlew tasks
    # or
    ./gradlew tasks --all
    ```

* **Show Project Dependencies:** Displays the dependency tree. Specify the module (e.g., `:app`).
    ```bash
    ./gradlew :app:dependencies
    ```

* **Run Lint Checks (Android):** Analyzes code for potential issues in the debug variant.
    ```bash
    ./gradlew lintDebug
    ```

* **Run Ktlint Check (if configured):** Runs code style checks using Ktlint.
    ```bash
    ./gradlew ktlintCheck
    ```

* **Auto-format with Ktlint (if configured):** Applies Ktlint formatting rules to fix style issues.
    ```bash
    ./gradlew ktlintFormat
    ```

### Common Options & Chaining

* **Increase Log Verbosity:** Use `--info`, `--debug`, or `--stacktrace` for more detailed output when diagnosing issues.
    ```bash
    ./gradlew build --info
    ```

* **Generate Build Scan:** Creates a detailed web-based report of your build (requires agreement).
    ```bash
    ./gradlew build --scan
    ```

* **Stop Gradle Daemon:** Stops background Gradle processes.
    ```bash
    ./gradlew --stop
    ```

* **Run Tasks Sequentially ("Chaining"):** You can run multiple tasks in sequence by listing them. Gradle figures out the dependencies. A common chain is cleaning then building.
    ```bash
    ./gradlew clean build
    # or
    ./gradlew clean assembleDebug
    ```

---

Remember to explore available tasks using `./gradlew tasks` as projects often have custom tasks or tasks added by different plugins.
