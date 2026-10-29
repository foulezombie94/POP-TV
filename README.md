# Pop TV

Lanceur Android TV conçu pour la Freebox Pop. Il propose une grille d’applications à six colonnes, un carrousel de programmes TMDB, des profils locaux, des réglages et des commandes système.

## Prérequis

- JDK 21
- Android SDK avec la plateforme API 36
- Gradle Wrapper fourni dans le dépôt

## Configuration

Copier `local.properties.example` vers `local.properties`, régler `sdk.dir` et ajouter un jeton TMDB Read Access dans `TMDB_READ_TOKEN`. Le jeton ne doit jamais être ajouté au dépôt.

## Compilation

```sh
./gradlew :app:assembleRelease
```

L’APK est produit dans `app/build/outputs/apk/release/`.

## Structure

- `app/src/main/java/com/poptv/launcher/ui` : accueil, grille et carrousel
- `home` : navigation et règles de disposition
- `tmdb` : accès aux tendances et cache du carrousel
- `prefs` : préférences et profils locaux
- `settings` : réglages de l’appareil et du lanceur
- `system` : centre de contrôle et routage des touches
- `recovery` : retour au lanceur Free en cas de problème

Le projet contient des composants et dépendances provenant d’autres projets. Leurs avis de licence et attributions doivent être conservés. Consultez les licences des bibliothèques dans leurs distributions respectives.
