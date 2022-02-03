# AlarmScheduler

![](https://img.shields.io/maven-central/v/com.carterchen247/alarm-scheduler) 
![](https://img.shields.io/github/languages/top/CarterChen247/AlarmScheduler)
![](https://img.shields.io/github/workflow/status/CarterChen247/AlarmScheduler/Test/master)


Since WorkManager handles the tasks which are guaranteed to be executed and it does not support time-exact tasks, we provide a library that helps to schedule tasks which is needed to be triggered at a specific time. (like calendar reminders, alarms, etc.)

**AlarmScheduler** is built on top of **AlarmManager** + **Kotlin Coroutines** + **Room**.

## Gradle

```
dependencies {
    implementation 'com.carterchen247:alarm-scheduler:x.x.x'
}
```

##  Usage

Please build `app` module for demo and more information.

### Initialization
```kotlin
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        // init AlarmScheduler
        AlarmScheduler.init(this)
        
        // bind AlarmTaskFactory to create different types of tasks when the alarm fires
        AlarmScheduler.setAlarmTaskFactory(object : AlarmTaskFactory {
            override fun createAlarmTask(alarmType: Int): AlarmTask {
                return DemoAlarmTask()
            }
        })
        
        // (optional) set logger to see detail logs
        AlarmScheduler.setLogger(AlarmSchedulerLoggerImpl())
    }
}
```

### Schedule a task

```kotlin
// schedule an alarm which fires after 10 seconds
AlarmConfig(System.currentTimeMillis() + 10000L, DemoAlarmTask.TYPE) {
    dataPayload(DataPayload().apply { putString("reminder", "have a meeting") })
}.schedule()
```

### Other features

```kotlin
internal interface AlarmSchedulerContract {
    fun setAlarmTaskFactory(alarmTaskFactory: AlarmTaskFactory)
    fun setLogger(logger: AlarmSchedulerLogger?)
    fun isAlarmTaskScheduled(alarmId: Int): Boolean
    fun cancelAlarmTask(alarmId: Int)
    fun cancelAllAlarmTasks()
    fun getScheduledAlarmTaskCountAsync(callback: AlarmTaskCountCallback)
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
