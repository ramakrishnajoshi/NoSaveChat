## NoSaveChat

NoSaveChat is a minimal Android app that shows your recent phone call history and lets you quickly open a WhatsApp chat with any number from the log. The app is built with Jetpack Compose and follows a simple MVVM architecture. It reads call logs on-device and does not upload or persist your data.

### Features
- **Recent call history**: Fetches and lists recent calls with contact name (if cached), number, type (Incoming/Outgoing/Missed/Rejected/Blocked), date, and duration.
- **WhatsApp quick chat**: Tap the Call icon on any call entry to start a chat with that number in WhatsApp.
- **Runtime permission handling**: Requests `READ_CALL_LOG` at runtime; shows appropriate loading, error, empty, and permission states.
- **Material 3 + Compose UI**: Modern UI implemented with Jetpack Compose and Material 3 components.

### Architecture
- **Presentation (Jetpack Compose)**
  - `MainActivity` hosts a `Scaffold` with a top app bar and renders `CallLogScreen`.
  - `CallLogScreen` observes `LiveData` from `CallLogViewModel`, handles permission flow, and renders list/loading/error/empty states.
  - `CallLogItemCard` displays an individual call with a WhatsApp action.
- **State/Logic (ViewModel)**
  - `CallLogViewModel` exposes `LiveData` for `callLogs`, `isLoading`, and `error`, and loads data via a repository on a background dispatcher.
- **Data (Repository + Model)**
  - `CallLogRepository` queries `CallLog.Calls` via `ContentResolver`, maps cursor rows to domain models, and formats dates.
  - `CallLogItem` is the domain model with a derived `callTypeString` for user-friendly labels.
- **Utilities**
  - `WhatsAppUtil` checks if WhatsApp is installed and opens a chat using `https://api.whatsapp.com/send?phone=<number>`.

### Data Flow
1. `CallLogScreen` checks `READ_CALL_LOG` and requests it if needed.
2. On grant, `CallLogViewModel.loadCallLogs()` calls `CallLogRepository.getRecentCalls()` on `Dispatchers.IO`.
3. The repository returns a list of `CallLogItem` (sorted newest first), which updates the `LiveData`.
4. The UI renders the list; tapping the WhatsApp icon invokes `WhatsAppUtil.openWhatsAppChat`.

### Permissions and App Visibility
- Manifest declares: `android.permission.READ_CALL_LOG`.
- Runtime: Permission is requested in `CallLogScreen` using `ActivityResultContracts.RequestPermission()`.
- Package visibility: `<queries><package android:name="com.whatsapp"/></queries>` is included so the app can target WhatsApp.

### Build and Run
- **Android Studio**: Latest stable (JDK 11).
- **SDK**: `compileSdk = 35`, `targetSdk = 35`, `minSdk = 24`.
- **Language/Toolkit**: Kotlin, Jetpack Compose, Material 3, LiveData.

Steps:
1. Clone the project and open it in Android Studio.
2. Sync Gradle and build the project.
3. Run on a device (recommended) with call history; grant the `READ_CALL_LOG` permission when prompted.
4. Install WhatsApp on the device to use the quick chat button.

### Project Structure (high level)
- `app/src/main/java/com/example/nosavechat/`
  - `MainActivity.kt`: App entry point, sets up Compose UI and top bar.
  - `viewmodel/CallLogViewModel.kt`: Loads call logs and exposes UI state via `LiveData`.
  - `repository/CallLogRepository.kt`: Reads call logs from `CallLog.Calls` and maps to models.
  - `model/CallLogItem.kt`: Domain model with derived call type label.
  - `ui/screens/CallLogScreen.kt`: Handles permission flow and renders the list and states.
  - `ui/components/CallLogItem.kt`: Card composable for a single call entry with a WhatsApp action.
  - `util/WhatsAppUtil.kt`: Opens WhatsApp chat and handles missing-app fallback.
- `app/src/main/AndroidManifest.xml`: Declares permission and WhatsApp package visibility.
- `app/build.gradle.kts`: Android/Compose configuration and dependencies.

### Dependencies (key)
- AndroidX Core, Lifecycle Runtime
- Jetpack Compose BOM, UI, Tooling, Material 3
- `androidx.compose.runtime:runtime-livedata`

### Usage Notes
- For WhatsApp chat to work reliably, the phone number should include the country code (E.164). The app uses the number as-is from the call log after removing spaces/dashes. If WhatsApp cannot resolve the number, ensure it’s in international format.
- The app reads call logs locally and does not store or transmit them.

### Limitations / Future Ideas
- Filtering, searching, and grouping call logs (e.g., by contact) could be added.
- Support for initiating SMS or phone call actions from the list.
- Optional formatting/normalization of numbers to E.164 with region inference.

### Contributing
Issues and pull requests are welcome. Please follow Kotlin coding conventions and keep the code easy to read and test.
