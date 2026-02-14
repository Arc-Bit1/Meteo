# Meteo (Android / Kotlin / Compose)

Application météo Android native pensée pour une publication Google Play, avec une base **Clean Architecture + MVVM**.

## Stack technique
- **UI**: Jetpack Compose + Material 3 (mode sombre automatique, design pro type apps météo premium)
- **Architecture**: Clean (presentation/data/core/domain), MVVM
- **DI**: Hilt
- **Réseau**: Retrofit + Moshi + Coroutines
- **Localisation**: FusedLocationProviderClient
- **API météo**: **Meteoblue Free Weather API** (`basic-day` + `basic-1h`)
- **Cache offline**: Room
- **Background refresh**: WorkManager (chaînage toutes les 5 min, soumis aux contraintes système)
- **Widget écran d’accueil**: AppWidgetProvider
- **Notifications**: canal dédié alertes météo
- **Tests**: JUnit + MockK + Coroutines test

## Structure du projet

```text
app/src/main/java/com/example/meteo
├── core
│   ├── di/AppModule.kt
│   ├── location/LocationProvider.kt
│   ├── notification/WeatherNotifier.kt
│   └── worker/WeatherRefreshWorker.kt
├── data
│   ├── local/{WeatherDatabase, WeatherDao, WeatherCacheEntity}
│   ├── remote/{MeteoblueApi, WeatherDtos}
│   └── repository/WeatherRepository.kt
├── domain/model/WeatherModels.kt
├── presentation
│   ├── components/WeatherAnimation.kt
│   ├── main/{MainScreen, MainViewModel, WorkScheduler}
│   └── theme/Theme.kt
├── widget/WeatherWidgetReceiver.kt
├── MainActivity.kt
└── MeteoApp.kt
```

## Fonctionnalités couvertes
- Température actuelle, ressenti, humidité, vent, pression.
- Prévision **24h** (endpoint `basic-1h`) et **7 jours** (endpoint `basic-day`).
- Gestion erreurs: localisation refusée / indisponible, absence internet.
- Cache local Room pour affichage offline.
- Animation météo dynamique (soleil/pluie) côté Compose.
- Planification automatique avec contraintes batterie/réseau.
- Orientation verrouillée en mode portrait pour une UX homogène.

## Dépendances Gradle principales
Voir `app/build.gradle.kts` (Compose, Hilt, Retrofit, Room, WorkManager, Glance/AppWidget, tests).

## Configuration API Meteoblue
1. Créer une clé API sur Meteoblue (Free Weather API).
2. Remplacer `REPLACE_ME` dans:
   - `app/build.gradle.kts` via `buildConfigField("METEOBLUE_API_KEY", ...)`

## Lancer le projet
1. Ouvrir le dossier dans Android Studio (Hedgehog+ recommandé).
2. Laisser la synchro Gradle se terminer.
3. Vérifier la clé API Meteoblue.
4. Lancer sur appareil/émulateur Android 8+
5. Accepter les permissions de localisation (+ notifications Android 13+).

## Améliorations recommandées pour version Play Store
- Chiffrer / masquer la clé API (Remote Config, backend proxy, NDK obfuscation partielle).
- Ajouter un mapping complet des `pictocode` Meteoblue avec assets animés dédiés.
- Ajouter tests UI Compose + tests instrumentés WorkManager/Room.
- Ajouter onboarding + écran paramètres (unités, ville favorite, langue).
- Ajouter stratégie d’alertes météo sévères avec seuils configurables.
- Accessibilité: contrastes, taille police, content descriptions.
- Analytics + crash reporting + monitoring perf (Firebase Performance).
- Politique confidentialité + Data safety form + conformité Play.
