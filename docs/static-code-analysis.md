# NoSaveChat

This document provides information on how to use the code quality tools integrated in this project: Ktlint and Detekt.

## Code Quality Tools

This project uses two complementary code quality tools to ensure consistent code style and identify potential issues:

### Ktlint

Ktlint is a linter and formatter for Kotlin that ensures your code follows the official Kotlin style guide.

#### Features

- No configuration required (uses the official Kotlin style guide)
- Formats code automatically
- Integrates with Gradle
- Provides Git hooks for pre-commit checks

#### Usage

**Check code style:**
```bash
./gradlew ktlintCheck
```

**Format code automatically:**
```bash
./gradlew ktlintFormat
```

**Check a specific module:**
```bash
./gradlew :app:ktlintCheck
```

### Detekt

Detekt is a static code analysis tool for Kotlin that identifies code smells, complexity issues, and potential bugs.

#### Features

- Customizable rule sets
- Detailed HTML reports
- Gradle integration
- Baseline support to exclude existing issues

#### Usage

**Run analysis:**
```bash
./gradlew detekt
```

**Generate a baseline file:**
```bash
./gradlew detektBaseline
```

**Run analysis on a specific module:**
```bash
./gradlew :app:detekt
```

## Configuration

### Ktlint Configuration

Ktlint is configured in the root `build.gradle.kts` file with the following settings:

```kotlin
configure<org.jlleitschuh.gradle.ktlint.KtlintExtension> {
    debug.set(true)
    android.set(true)
    outputToConsole.set(true)
    ignoreFailures.set(false)
    enableExperimentalRules.set(true)
    filter {
        exclude("**/generated/**")
        include("**/kotlin/**")
    }
}
```

### Detekt Configuration

Detekt is configured in the root `build.gradle.kts` file and uses a configuration file located at `config/detekt/detekt.yml`:

```kotlin
detekt {
    buildUponDefaultConfig = true
    allRules = false
    config = files("${project.rootDir}/config/detekt/detekt.yml")
    baseline = file("${project.rootDir}/config/detekt/baseline.xml")

    reports {
        html.enabled = true
        xml.enabled = true
        txt.enabled = false
        sarif.enabled = true
    }
}
```

You can customize the rules by editing the `config/detekt/detekt.yml` file.

## Git Hooks

This project includes a pre-commit Git hook that runs both Ktlint and Detekt before allowing commits. This ensures that all committed code meets the project's quality standards.

### Setup Git Hooks

To set up the Git hooks, run the following command from the project root:

```bash
git config core.hooksPath config/git-hooks
chmod +x config/git-hooks/pre-commit
```

### Bypassing Pre-commit Hook (Not recommended)
The standard and most straightforward way to bypass the pre-commit hook (and also the commit-msg hook, if one existed) for a specific commit is to use the --no-verify flag with the git commit command.

```bash
git commit --no-verify -m "Your commit message"
```

### Pre-commit Hook

The pre-commit hook runs the following checks:

1. Ktlint check to ensure code style compliance
2. Detekt analysis to catch potential issues

If either check fails, the commit will be blocked until the issues are resolved.

## Best Practices

1. Run `./gradlew ktlintFormat` before committing to automatically fix style issues
2. Address Detekt warnings even if they don't block your commit
3. Keep the baseline file updated when you intentionally ignore specific issues
4. Review HTML reports for detailed information about detected issues

## Troubleshooting

### Common Ktlint Issues

- **Wildcard imports**: Replace with specific imports
- **Missing final newline**: Ensure files end with a newline
- **Indentation**: Use 4 spaces for indentation

### Common Detekt Issues

- **Complex functions**: Break down functions with high complexity
- **Large classes**: Split large classes into smaller, focused ones
- **Magic numbers**: Replace hardcoded values with named constants
