# AlarmScheduler

![](https://img.shields.io/maven-central/v/com.carterchen247/alarm-scheduler) 
![](https://img.shields.io/github/languages/top/CarterChen247/AlarmScheduler)
![](https://img.shields.io/github/actions/workflow/status/CarterChen247/AlarmScheduler/.github/workflows/android.yml?branch=master)








**AlarmScheduler is a tool to schedule time exact tasks, even in very short period of time.**

**AlarmScheduler provides various supports:**

- Easy-to-use API
- Alarms survive after device reboot
- debuggable with built-in logger

**AlarmScheduler** is built on top of **AlarmManager** + **Kotlin Coroutines** + **Room** and ❤️ !!

## Add AlarmScheduler **to your project**

### Gradle ![](https://img.shields.io/maven-central/v/com.carterchen247/alarm-scheduler) 

Add the dependency to your **module**'s `build.gradle` file:

```groovy
dependencies {
    implementation 'com.carterchen247:alarm-scheduler:x.x.x'
}
```

## How to Use

To see the complete usage of AlarmScheduler, please build and refer to the [demo app](https://github.com/CarterChen247/AlarmScheduler/tree/develop/app).

### Schedule an Alarm

#### 1️⃣ Define a AlarmTask callback

```kotlin
AlarmScheduler.setAlarmTaskFactory(object : AlarmTaskFactory {
    override fun createAlarmTask(alarmType: Int): AlarmTask {
        return MyAlarmTask()
    }
})
```

```kotlin
class MyAlarmTask : AlarmTask {
    override fun onAlarmFires(alarmId: Int, dataPayload: DataPayload) {
        // do something with dataPayload you set
    }

    companion object {
        const val TYPE = 1
    }
}
```

#### 2️⃣ And then just schedule it!

```kotlin
val config = AlarmConfig(
    Date().time + 10000L, // trigger time
    MyAlarmTask.TYPE
) {
    dataPayload("reminder" to "have a meeting")
}

AlarmScheduler.schedule(config){ ... }
```

#### 3️⃣ Handle the result on your own

```kotlin
AlarmScheduler.schedule(config) { result: ScheduleResult ->
    when (result) {
        is ScheduleResult.Success -> {
            // alarm scheduling success!
        }
        is ScheduleResult.Failure -> {
            when (result) {
                ScheduleResult.Failure.CannotScheduleExactAlarm -> {
                    // handle scenarios like user disables exact alarm permission
                }
                is ScheduleResult.Failure.Error -> {
                    // handle error
                }
            }
        }
    }
}
```

### For Android 12+

When receiving a `CannotScheduleExactAlarm` failure result, it means the user disable the exact alarm permission. Try to use `openExactAlarmSettingPage()`  extension function with prompts to guide your user.

```kotlin
fun Activity.openExactAlarmSettingPage() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        startActivity(Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM))
    }
}
```

get notified once user grants the exact alarm permission.

```kotlin
AlarmSchedulerEventObserver { event ->
    if (event is ScheduleExactAlarmPermissionGrantedEvent) {
	// get notified
    }
}
```

### Debugging

If something is not working, enable the built-in logger to see what’s going on.

```kotlin
AlarmScheduler.setLogger(AlarmSchedulerLogger.DEBUG)
```

### Other Features

```kotlin
AlarmScheduler.getScheduledAlarmsAsync { scheduledAlarms: List<AlarmInfo> ->
    // list the scheduled alarms 
}
```

There are more! refer to [AlarmSchedulerContract](https://github.com/CarterChen247/AlarmScheduler/blob/develop/alarmscheduler/src/main/java/com/carterchen247/alarmscheduler/AlarmSchedulerContract.kt) see full APIs.

```kotlin
internal interface AlarmSchedulerContract {
    fun setAlarmTaskFactory(alarmTaskFactory: AlarmTaskFactory)
    fun setLogger(loggerImpl: AlarmSchedulerLogger)
    fun setErrorHandler(errorHandler: AlarmSchedulerErrorHandler)
    fun isAlarmTaskScheduled(alarmId: Int): Boolean
    fun cancelAlarmTask(alarmId: Int)
    fun cancelAllAlarmTasks()
    fun getScheduledAlarmsAsync(callback: ScheduledAlarmsCallback)
    fun addEventObserver(observer: AlarmSchedulerEventObserver)
    fun removeEventObserver(observer: AlarmSchedulerEventObserver)
    fun schedule(config: AlarmConfig, callback: ScheduleResultCallback?)
    fun canScheduleExactAlarms(): Boolean
}
```

## Dealing with behavior changes
### [Android 15 - Changes to package stopped state](https://developer.android.com/about/versions/15/behavior-changes-all#enhanced-stop-states)

>**✅ Tested**

By using AlarmScheduler, when the app recovers from the stopped state, it will automatically detect the `ACTION_BOOT_COMPLETED` action, and alarms will be automatically rescheduled.

### [Android 14 - Schedule exact alarms are denied by default](https://developer.android.com/about/versions/14/behavior-changes-all#schedule-exact-alarms)

> **✅ Handled**

To handle the behavior change in Android 14, use `ScheduleResultCallback` when calling `AlarmScheduler.schedule()`. This callback will return one of the following results:

- `Success`: The alarm was successfully scheduled.
- `Failure.CannotScheduleExactAlarm`: The app doesn't have permission to schedule exact alarms.
- `Failure.Error`: An error occurred during scheduling.

Here's how to use it:

```kotlin
AlarmScheduler.schedule(config) { result: ScheduleResult ->
    when (result) {
        is ScheduleResult.Success -> {
            // Alarm scheduling success!
        }
        is ScheduleResult.Failure -> {
            when (result) {
                ScheduleResult.Failure.CannotScheduleExactAlarm -> {
                    // Handle scenarios like user disables exact alarm permission
                    // Consider prompting the user to grant permission
                }
                is ScheduleResult.Failure.Error -> {
                    // Handle other errors
                }
            }
        }
    }
}
```

For Android 12 and above, you can use the `openExactAlarmSettingPage()` extension function to guide users to the exact alarm permission settings:

```kotlin
fun Activity.openExactAlarmSettingPage() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        startActivity(Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM))
    }
}
```

You can also get notified when the user grants the exact alarm permission:

```kotlin
AlarmSchedulerEventObserver { event ->
    if (event is ScheduleExactAlarmPermissionGrantedEvent) {
        // Permission granted, you can try scheduling the alarm again
    }
}
```

## Apps using AlarmScheduler

- [吉時倒垃圾(GarbageTruckAlarm)](https://play.google.com/store/apps/details?id=com.carterchen247.garbagetruckalarm&hl=zh_TW)

## License

```
MIT License

Copyright (c) 2020 Carter Chen

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```
