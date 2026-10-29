# Pop TV project overview

Pop TV is a Kotlin Android TV launcher for Freebox Pop. The application includes a six-column home grid, a cached TMDB carousel, local launcher profiles, settings and a recovery path to Free TV.

## Build

Install JDK 21 and Android SDK API 36. Configure `local.properties` from `local.properties.example`, then run `./gradlew :app:assembleRelease`.

## Main components

See `README.md` for the source tree and build instructions. See `AGENTS.md` for contribution requirements. The interface follows selected tvOS interaction patterns; Apple’s Human Interface Guidelines are the design reference, not a source of application code.
