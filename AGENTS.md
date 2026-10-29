# Guide de contribution

Ce dépôt contient Pop TV, un lanceur Android TV pour Freebox Pop. Avant une modification, lire le README et examiner le composant concerné.

## Principes

- Garder une navigation fiable à la télécommande et un premier affichage rapide.
- N’utiliser que des données réelles ; garder les clés dans `local.properties`.
- Limiter les animations permanentes, les requêtes réseau et la mémoire image.
- Préserver le retour vers le lanceur Free et les chemins de récupération.
- Respecter les licences et avis d’attribution des composants utilisés.

## Vérification

Compiler la variante de production avec `./gradlew :app:assembleRelease`. Pour les changements de logique, exécuter les tests unitaires concernés avec `./gradlew :app:testReleaseUnitTest`.

## Organisation du code

L’application est dans `app/`. Les sources Kotlin utilisent `com.poptv.launcher` et sont classées par noyau (`core`), fonctionnalités (`features`), données (`data`) et intégration Android (`platform`). Les règles de navigation et de disposition sont séparées de l’interface ; la configuration privée reste locale et ignorée par Git.
