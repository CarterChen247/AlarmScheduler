

AlarmScheduler
===

This is an utils library to help to schedule time exact tasks(Calendar reminders, TODO reminders, etc.)

## Setup

```
implementation 'com.carterchen247:alarm-scheduler:1.0.0'
```

##  Usage

Please see `app` module for more information and demo code.

### Initialization
```kotlin
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        // init AlarmScheduler
        AlarmScheduler.init(this)
        
        // set  AlarmTaskFactory
        AlarmScheduler.setAlarmTaskFactory(object : AlarmTaskFactory {
            override fun createAlarmTask(alarmType: Int): AlarmTask {
                return DemoAlarmTask()
            }
        })
        
        // if you need logs
        AlarmScheduler.setLogger(AlarmSchedulerLoggerImpl())
    }
}
```

### Schedule a task

```kotlin
// schedule an alarm
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // schedule an alarm fires after 10 seconds
        AlarmConfig(DemoAlarmTask.TYPE, System.currentTimeMillis() + 10000L) {
            dataPayload(DataPayload().apply { putString("reminder", "have a meeting") })
        }.schedule()
    }
}
```
