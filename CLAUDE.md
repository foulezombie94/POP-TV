# Pop TV engineering notes

The app is a Kotlin Android TV launcher targeting the Freebox Pop. Start with `README.md` and `AGENTS.md` before making changes.

Keep remote navigation dependable, show the app grid promptly, use real data, and bound image/network work. Preserve Free launcher recovery. Keep API credentials in ignored `local.properties`. Retain third-party notices and license terms.

Build with `./gradlew :app:assembleRelease`. Run relevant unit tests with `./gradlew :app:testReleaseUnitTest` when changing pure logic.

