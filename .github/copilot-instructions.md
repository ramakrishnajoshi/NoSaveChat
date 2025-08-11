# Copilot Instructions for NoSaveChat

## About This Project

NoSaveChat is an Android application that displays call logs without storing sensitive call information. It's built using Jetpack Compose and follows modern Android development practices.

## Architecture

- **UI Framework**: Jetpack Compose with Material 3 design
- **Architecture Pattern**: MVVM with ViewModels
- **Lifecycle Management**: Android Architecture Components with ProcessLifecycleOwner
- **Language**: Kotlin 2.0.21
- **Build System**: Gradle with Kotlin DSL

## Key Components

### MainActivity.kt
- Main entry point using ComponentActivity
- Implements ProcessLifecycleOwner for app lifecycle detection
- Uses lifecycle observer to refresh call logs when app returns from background

### CallLogViewModel.kt
- AndroidViewModel managing call log data state
- Handles READ_CALL_LOG permission checking
- Provides reactive data flow using StateFlow
- Implements safe refresh mechanism to prevent concurrent loading

### CallLogScreen.kt (Compose UI)
- Jetpack Compose screen displaying call logs
- Uses LaunchedEffect for initial data loading
- Implements permission request handling
- Responsive UI with proper loading and error states

## Development Guidelines

### Code Style
- Follow Kotlin coding conventions
- Use ktlint for code formatting (configured in build.gradle.kts)
- Apply detekt for static code analysis
- Maintain consistent naming conventions

### Architecture Patterns
- Use ViewModels for business logic and state management
- Implement proper separation of concerns
- Follow reactive programming patterns with StateFlow/Flow
- Use Compose for declarative UI development

### Lifecycle Management
- Always register/unregister lifecycle observers properly
- Use ProcessLifecycleOwner for app-level lifecycle events
- Handle configuration changes gracefully
- Prevent memory leaks by cleaning up observers

### Permission Handling
- Check permissions before accessing sensitive data
- Implement proper permission request flows
- Handle permission denied scenarios gracefully
- Follow Android best practices for runtime permissions

### Testing
- Write unit tests for ViewModels and business logic
- Use Compose testing utilities for UI tests
- Mock external dependencies appropriately
- Test permission scenarios and edge cases

## Build Configuration

- **Target SDK**: Latest stable Android API level
- **Min SDK**: 24 (Android 7.0)
- **AGP Version**: 8.7.0 or latest stable
- **Kotlin Version**: 2.0.21
- **Compose BOM**: Latest stable version

## Common Implementation Patterns

### Option 6: Compose Lifecycle Approach (Alternative Implementation)

The Compose Lifecycle approach would use Compose's built-in lifecycle awareness instead of ProcessLifecycleOwner:

```kotlin
@Composable
fun CallLogScreen(viewModel: CallLogViewModel) {
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val lifecycleEvent by lifecycle.observeAsState()
    
    LaunchedEffect(lifecycleEvent) {
        if (lifecycleEvent == Lifecycle.Event.ON_START) {
            viewModel.refreshCallLogs()
        }
    }
    
    // Rest of UI implementation
}
```

**Detailed Analysis of Option 6:**

**Pros:**
- **Compose-Native**: Integrates seamlessly with Compose's reactive paradigm
- **Declarative**: Follows Compose's declarative approach to UI and state management
- **Automatic Cleanup**: Compose handles cleanup automatically when the composable leaves the composition
- **Reactive**: Uses Compose's state observation mechanisms naturally
- **Scoped to UI**: Only triggers when the specific UI component is active

**Cons:**
- **Compose-Only Solution**: Limited to Compose UI framework, not usable in View-based UIs
- **UI-Coupled**: Business logic becomes coupled to UI lifecycle rather than app lifecycle
- **Multiple Triggers**: May trigger on UI recomposition events beyond just app foreground/background
- **Less Precise**: Compose lifecycle events may not perfectly align with app visibility states
- **Learning Curve**: Requires understanding Compose lifecycle patterns and state management

**Implementation Details:**
1. **LocalLifecycleOwner**: Access the current lifecycle owner within the Compose hierarchy
2. **observeAsState()**: Convert lifecycle events into Compose state that triggers recomposition
3. **LaunchedEffect**: Execute side effects (API calls) in response to lifecycle changes
4. **Event Filtering**: Filter for specific lifecycle events (ON_START, ON_RESUME) that indicate visibility

**When to Use Option 6:**
- When building a pure Compose application
- When you want UI-specific refresh behavior
- When implementing feature-specific refresh logic
- When the refresh scope should be limited to specific screens

**Why ProcessLifecycleOwner (Current Implementation) is Better:**
- **App-Level Scope**: Refreshes data when the entire app comes to foreground, not just UI components
- **Framework Agnostic**: Works with both Compose and View-based UIs
- **Precise Control**: Specifically designed for app-level lifecycle events
- **Performance**: Fewer false triggers compared to UI-level lifecycle events
- **Separation of Concerns**: Keeps app lifecycle logic separate from UI implementation

The current ProcessLifecycleOwner implementation is the optimal choice for this use case because it provides precise app-level lifecycle detection with minimal overhead and clean architecture separation.

## Dependencies

Key dependencies to be aware of:
- `androidx.lifecycle:lifecycle-process` for ProcessLifecycleOwner
- `androidx.activity:activity-compose` for Compose integration
- `androidx.lifecycle:lifecycle-viewmodel-compose` for ViewModel integration
- Compose BOM for consistent Compose library versions

## Error Handling

- Always handle permission denied scenarios
- Implement proper error states in UI
- Log errors appropriately for debugging
- Provide user-friendly error messages

## Performance Considerations

- Avoid unnecessary recompositions in Compose
- Use proper caching and state management
- Implement efficient data loading strategies
- Monitor memory usage and prevent leaks