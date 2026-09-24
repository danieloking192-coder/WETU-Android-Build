# WETU — Procédure reproductible : de l'écriture GitHub à l'APK

Date de référence : 24 septembre 2026

## Objectif
Cette procédure est le modèle à réutiliser lorsqu'un nouveau projet Android doit être rendu effectivement écrivable depuis ChatGPT/GitHub puis compilé en APK avec GitHub Actions.

## 1. Rendre l'accès GitHub réellement effectif
1. Utiliser le bon compte GitHub et le bon dépôt.
2. Ouvrir les paramètres d'installation du ChatGPT Codex Connector.
3. Vérifier les permissions utiles : lecture/écriture du code, Actions, issues, pull requests et workflows.
4. Dans l'accès aux dépôts, sélectionner **All repositories** lorsque le nouveau dépôt doit être accessible.
5. Enregistrer la modification.
6. Vérifier ensuite que le dépôt apparaît réellement avec des permissions de push/admin/maintain/pull/triage. Ne pas considérer l'écran de configuration comme preuve suffisante.

## 2. Initialiser un dépôt vide avant toute écriture automatisée
Un dépôt totalement vide peut refuser une écriture Contents API avec une erreur du type `sha wasn't supplied`, car aucune branche/référence initiale n'existe encore.
1. Créer manuellement un premier fichier, typiquement `README.md`.
2. Commiter ce fichier sur `main`.
3. Vérifier que le dépôt contient maintenant une branche/référence exploitable.
4. Seulement après cela, effectuer les écritures automatisées de fichiers.

## 3. Écrire la fondation Android
Créer progressivement, avec un commit vérifiable à chaque étape importante :
- `settings.gradle.kts`
- `build.gradle.kts`
- `gradle.properties`
- `app/build.gradle.kts`
- `AndroidManifest.xml`
- ressources de thème
- `MainActivity.kt`
- `.gitignore`
- workflow dans `.github/workflows/`

Base validée pour WETU :
- Android namespace/applicationId : `com.wetu.creator`
- compileSdk/targetSdk : 35
- minSdk : 26
- Java 17
- Kotlin JVM target 17
- Jetpack Compose + Material 3
- versionCode 21, versionName 1.0.0

## 4. Corriger les incompatibilités de dépendances/API
Après le premier build, lire l'erreur exacte avant de modifier.
Exemple rencontré : `SmallTopAppBar` non résolu avec la version Material 3 utilisée.
Correction appliquée : remplacer `SmallTopAppBar` par `TopAppBar` et conserver l'annotation `ExperimentalMaterial3Api`.
Ne jamais déclarer un build vert sans vérifier le résultat réel de GitHub Actions.

## 5. Construire automatiquement l'APK
Workflow de référence :
- checkout du dépôt
- JDK 17
- environnement Gradle 8.9
- validation des fichiers Android essentiels
- `:app:assembleDebug`
- vérification de la présence de `app/build/outputs/apk/debug/app-debug.apk`
- calcul du SHA-256
- upload de l'APK comme artefact `wetu-apk-debug`
- conservation de l'artefact pendant 14 jours

Cette chaîne correspond au fonctionnement recommandé par GitHub : configurer JDK/Gradle, exécuter le build, puis conserver le résultat comme artefact. Voir la documentation GitHub sur les builds Gradle et les artefacts.

## 6. Éliminer les workflows concurrents
S'il existe plusieurs workflows qui construisent le même APK :
1. identifier les doublons ;
2. conserver un workflow de référence ;
3. supprimer le doublon ;
4. pousser la modification ;
5. vérifier qu'un nouveau run démarre et qu'il correspond au bon commit.

## 7. Validation finale
La validation doit suivre cette chaîne :
**écriture effective → commit → workflow déclenché → build terminé → conclusion SUCCESS → APK vérifié → artefact vérifié → test réel sur téléphone.**

Pour WETU, la correction finale de `MainActivity.kt` a produit le commit :
`3ac52fa183daf977a55142182f83e48270f23a8f`

Le run GitHub Actions vert de référence est :
- workflow : WETU Android Build
- run : #5
- run ID : `35953371558`
- conclusion : SUCCESS
- commit testé : `3ac52fa183daf977a55142182f83e48270f23a8f`

## 8. Règles à réutiliser dans les prochains projets
- Toujours vérifier le compte GitHub et le dépôt cible avant d'écrire.
- Toujours rendre l'installation Codex Connector réellement accessible au dépôt.
- Toujours initialiser un dépôt vide avant les écritures automatisées.
- Toujours vérifier le SHA/commit après une écriture.
- Toujours faire un build automatisé avant de parler d'APK fonctionnel.
- Toujours distinguer « code écrit », « build vert » et « APK récupéré/testé ».
- Toujours conserver un seul workflow de build de référence.
- Toujours vérifier l'artefact avant de fournir un lien de téléchargement.
- Ne jamais inventer un lien d'APK, un artefact, un commit ou un résultat de build.
- Après le build vert, passer au test réel sur appareil.

## 9. Principe de mémoire de projet
Quand un prochain projet atteint le même niveau — dépôt GitHub accessible, projet Android initialisé et besoin de produire un APK — reprendre cette procédure comme **pipeline standard d'initialisation → écriture → CI → APK → validation** et adapter uniquement les noms, chemins, package/applicationId et exigences du nouveau projet.
