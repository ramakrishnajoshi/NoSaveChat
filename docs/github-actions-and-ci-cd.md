# Understanding GitHub Actions in this Project

This document provides an overview of how GitHub Actions is used for automation, Continuous Integration (CI), and potentially Continuous Deployment (CD) within this project.

## CI/CD Pipeline

A CI/CD Pipeline is the automated sequence of steps (the "assembly line") that takes code from a developer's commit all the way to being built, tested, and potentially deployed.

## General Uses of GitHub Actions

GitHub Actions is a powerful automation platform built into GitHub. It allows developers to automate workflows directly within their repository based on events like code pushes, pull requests, issue creation, or scheduled times. Common uses across different companies include:

* **Continuous Integration (CI):** Automatically building, testing, and analyzing code on every change to ensure quality and catch errors early. (More details below)
* **Continuous Deployment/Delivery (CD):** Automatically deploying applications or publishing packages after successful CI checks. (More details below)
* **Automation:** Handling repetitive tasks like labeling issues, assigning reviewers, updating dependencies, generating release notes, or sending notifications.
* **Infrastructure as Code (IaC):** Managing cloud infrastructure using code (e.g., Terraform, Pulumi) triggered by repository events.
* **Security Scanning:** Running security analysis tools (like GitHub's CodeQL) to find vulnerabilities.

**Benefits:** Using GitHub Actions helps teams maintain **consistency**, reduce **manual effort**, get **faster feedback** on changes, and **integrate** development processes smoothly within the GitHub ecosystem.

### CI/CD In depth
* **Continuous Integration (CI):** This is the most common use case.
    * **Building:** Automatically compiling code whenever changes are pushed or pull requests are created.
    * **Testing:** Running unit tests, integration tests, and end-to-end tests automatically to catch regressions.   
  * **Static Analysis:** Running linters (like Android Lint), style checkers (like Ktlint), and static analysis tools (like Detekt, SonarQube, CodeQL) to enforce code quality, style consistency, and find potential bugs or security vulnerabilities.

* **Continuous Deployment/Delivery (CD):**
    * **Deploying:** Automatically deploying applications to various environments (staging, production) after successful builds and tests. This could involve deploying web apps, mobile app builds to testing platforms (like Firebase App Distribution), or publishing packages.   
  * **Publishing:** Automatically publishing libraries/packages to registries (like Maven Central, npm, Docker Hub).


## Continuous Integration (CI) in this Project

CI is the practice of frequently merging code changes into a central repository, after which automated builds and tests are run.

**How we use CI:**

* **Trigger:** Workflows are typically triggered automatically on `push` events to main branches (like `main` or `develop`) and on `pull_request` events targeting these branches.
* **Tasks:** The CI workflow(s) defined in the `.github/workflows/` directory perform several key tasks using Gradle (`./gradlew`):
    * **Building:** Compiling the Android application (`./gradlew assembleDebug` or `./gradlew assembleRelease`) to ensure the code builds successfully. The APK file is generated on the GitHub Actions runner's file system. The APK file exists only within the workspace for that specific job run. If you need to use this APK later (e.g., upload it somewhere for testing, attach it to the workflow run results), you would typically add another step after the build step using an action like actions/upload-artifact@v4. Without such a step, the APK is discarded when the runner finishes its job.
    * **Testing:** Running unit tests (`./gradlew testDebugUnitTest`) to verify code logic. (Instrumented tests like `./gradlew connectedCheck` might be run separately or on a different schedule as they require emulators/devices).
    * **Static Analysis:**
        * Running Android Lint (`./gradlew lintDebug`) for Android-specific issues.
        * Checking Kotlin code style with Ktlint (`./gradlew ktlintCheck`).
        * Analyzing code quality and potential bugs with Detekt (`./gradlew detekt`).
* **Goal:** To ensure that any code merged into main branches is buildable, passes core tests, and adheres to our quality and style standards. Failed checks will typically block pull request merges.
* **Workflow File:** You can find the primary CI configuration here: `.github/workflows/android-ci.yml`.

**Note on Local Checks:**
* We also use Git pre-commit hooks (configured via `config/git-hooks`) to run Ktlint and Detekt locally *before* you commit. This provides faster feedback but CI checks on GitHub Actions are the ultimate source of truth, running in a clean, consistent environment.
* Local checks using pre commit hooks can be bypassed by using the `--no-verify` flag with `git commit`.

## Continuous Deployment / Delivery (CD)

CD automates the release process, deploying the application to users or testing environments after CI checks pass.

**Potential CD Scenarios for this Android Project (Illustrative - check current setup):**

* **Internal Testing:** Automatically building an AAB/APK and uploading it to Google Play Internal Testing or Firebase App Distribution when changes are merged to a `develop` or `main` branch.
* **Production Release:** Automatically drafting a release, building a signed AAB/APK, and uploading it to a production track on Google Play when a new Git tag is pushed or code is merged to a specific `release` branch.
* **Library Publishing:** If this project contained Android libraries (`.aar`), CD could automate publishing them to repositories like Maven Central.


---

By automating CI and potentially CD with GitHub Actions, we aim to improve code quality, reduce manual release overhead, and deliver value more reliably. Please familiarize yourself with the workflows in `.github/workflows/` and the project's documentation (like `docs/static-code-analysis.md`).


## Continuous Deployment vs Continuous Delivery
### Continuous Delivery (CD):
Goal: To automate the release process so that new changes, after passing automated tests in CI, can be rapidly and reliably released to users.   
Process: Takes the build artifact from CI, runs further tests (integration, end-to-end), and prepares it for release. The deployment to production might still be a manual button push, but the preparation is automated.
Benefit: Reduces the risk and manual effort involved in releases, allowing for more frequent updates.   

### Continuous Deployment (CD - alternative meaning):
Goal: To automatically deploy every change that passes all stages of the CI/CD pipeline to production without manual intervention.   
Process: **Extends Continuous Delivery by automatically triggering the final deployment step**.   
Benefit: Achieves the fastest possible release cycle. Requires a high degree of confidence in the automated testing and release process.