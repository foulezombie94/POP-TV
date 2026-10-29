# Pop TV

Lanceur Android TV pour Freebox Pop, dérivé de tvhome et personnalisé avec accueil six colonnes, profils locaux, contrôle système, récupération Freebox et carrousel TMDB.

## Construire

1. Installer JDK 21 et Android SDK (API 36).
2. Copier `local.properties.example` vers `local.properties` et renseigner `sdk.dir` ainsi que `TMDB_READ_TOKEN`.
3. Lancer `./gradlew :app:assembleRelease`.

La clé TMDB n'est pas incluse. Le projet conserve les crédits et licences des dépendances amont.
