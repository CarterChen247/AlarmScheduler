# Changelog

All notable changes to this project will be documented in this file. See [standard-version](https://github.com/conventional-changelog/standard-version) for commit guidelines.

## [2.0.0](https://github.com/CarterChen247/AlarmScheduler/compare/v1.0.4...v2.0.0) (2023-01-31)


### ⚠ BREAKING CHANGES

* adjust AlarmSchedulerLogger api

### Features

* add Activity.openExactAlarmSettingPage extension function ([6e7ba13](https://github.com/CarterChen247/AlarmScheduler/commit/6e7ba136863103176a925eda66231c0b8923d727))
* add android:exported="true" flag to MainActivity of demo app ([69029bc](https://github.com/CarterChen247/AlarmScheduler/commit/69029bcd6efb8f838cacd1d2c8d6a51f8dd3af05))
* add canScheduleExactAlarms function ([e0c3965](https://github.com/CarterChen247/AlarmScheduler/commit/e0c396531a5fc8c3d1331340107430d386f0813e))
* add EventObserver. Add ScheduleExactAlarmPermissionGrantedEvent ([ac6e78f](https://github.com/CarterChen247/AlarmScheduler/commit/ac6e78f59dc3627c3e966af06f902c69c8958752))
* add PendingIntent.FLAG_IMMUTABLE flag when creating PendingIntent ([d838a71](https://github.com/CarterChen247/AlarmScheduler/commit/d838a71a5bfc3840d561ea7521466efb1e63f9d2))
* add SCHEDULE_EXACT_ALARM permission to AndroidManifest.xml ([649def8](https://github.com/CarterChen247/AlarmScheduler/commit/649def8504b0fc2f1b847e3cf61945cc688f205d))
* add scheduleAlarm util function ([ad6e906](https://github.com/CarterChen247/AlarmScheduler/commit/ad6e906d0d27ee448c7d560494317e9c6c6a952e))
* migrate compileSdkVersion and targetSdkVersion to Android 12 (Android S) ([bac22e4](https://github.com/CarterChen247/AlarmScheduler/commit/bac22e494dc851e9b82bf3996792920c043b6a3e))
* **sample app:** add time information ([6ca3cf1](https://github.com/CarterChen247/AlarmScheduler/commit/6ca3cf1221b4fdb9815c8c0f1c1a5a693d5bdf8e))
* **sonatype:** setup library module gradle ([be8aeb2](https://github.com/CarterChen247/AlarmScheduler/commit/be8aeb29d505b6acb17716e225f4d2577dd21370))
* **sonatype:** setup root project gradle ([5f8852e](https://github.com/CarterChen247/AlarmScheduler/commit/5f8852ee17aba4973fd13679f8a223ad60f78647))
* update gradle version. Fix init gradle issue ([f074f99](https://github.com/CarterChen247/AlarmScheduler/commit/f074f99f159a9c611498338c6ad208b0f00142fc))
* upgrade android.test.ext:junit version to 1.1.3 to fix gradle build failure ([a20d4a9](https://github.com/CarterChen247/AlarmScheduler/commit/a20d4a98e2a1fa54509d4e4ce6d9b2287d61d7d9))
* upgrade Kotlin version to 1.4.31 ([854e7c6](https://github.com/CarterChen247/AlarmScheduler/commit/854e7c67fc3263f9f422bb7d8d97640c09839155))
* upgrade Kotlin version to 1.5.32. Remove kotlin-android-extensions plugin ([bcef796](https://github.com/CarterChen247/AlarmScheduler/commit/bcef7963d663c4408fe863e1798e7f89f76ae8bb))
* use content provider to do initialization ([5c2542f](https://github.com/CarterChen247/AlarmScheduler/commit/5c2542f15dea785421c4449f6a82c5b5c1f85165))


### Bug Fixes

* fix Android S FLAG_IMMUTABLE issue ([024d8cb](https://github.com/CarterChen247/AlarmScheduler/commit/024d8cbe520f857734f9b4c6c24ab9181bc2287c))
* fix build failed on Apple M1 with JDK8-arm64 ([131a44f](https://github.com/CarterChen247/AlarmScheduler/commit/131a44ffc115f32a293bf4971cae6e9c0aa3dc62)), closes [/developer.android.com/jetpack/androidx/releases/room#2](https://github.com/CarterChen247//developer.android.com/jetpack/androidx/releases/room/issues/2)


* Merge pull request #13 from CarterChen247/refactor/logger-system ([c16146c](https://github.com/CarterChen247/AlarmScheduler/commit/c16146c0a609776a702eb5cc8bf1451917f0704d)), closes [#13](https://github.com/CarterChen247/AlarmScheduler/issues/13)

### [1.0.4](https://github.com/CarterChen247/AlarmScheduler/compare/v1.0.3...v1.0.4) (2020-10-17)


### Features

* add Global error handler for user ([08cdece](https://github.com/CarterChen247/AlarmScheduler/commit/08cdece25af8a85502c4863860bdc0047540bc15))

### [1.0.3](https://github.com/CarterChen247/AlarmScheduler/compare/v1.0.2...v1.0.3) (2020-08-16)


### Features

* refactor AlarmTaskCountCallback to ScheduledAlarmsCallback ([edf1343](https://github.com/CarterChen247/AlarmScheduler/commit/edf1343ab02a703c9b9bc8124bdcfb20c2f28ac3))

### [1.0.2](https://github.com/CarterChen247/AlarmScheduler/compare/v1.0.1...v1.0.2) (2020-08-04)


### Features

* change scheduleAlarm implementation from setExactAndAllowWhileIdle to setAlarmClock ([8047574](https://github.com/CarterChen247/AlarmScheduler/commit/80475744b424e0e31b0b5bfe8af08e05a826e19b))

### [1.0.1](https://github.com/CarterChen247/AlarmScheduler/compare/v1.0.0...v1.0.1) (2020-07-24)


### Features

* set allowBackup to false ([e2e055e](https://github.com/CarterChen247/AlarmScheduler/commit/e2e055e40d45dfcba1afd50b8c05f0abaf31e4fb))

## 1.0.0 (2020-05-31)


### Features

* add exception handling for suspend functions ([73ed819](https://github.com/CarterChen247/AlarmScheduler/commit/73ed819ad8bec9a638c4db1cd08ddd07036a0533))
* add getScheduledAlarmTaskCountAsync demo to demo app ([dcd17de](https://github.com/CarterChen247/AlarmScheduler/commit/dcd17de9922f1ad2169a8264973382b9bbbf7b71))
* add getScheduledAlarmTaskCountAsync method for user to get scheduled task count ([efa3c24](https://github.com/CarterChen247/AlarmScheduler/commit/efa3c24e790a7d1675137aeda808715bdf85bcd6))
