package com.carterchen247.alarmscheduler.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.carterchen247.alarmscheduler.db.converter.DataPayloadTypeConverter
import com.carterchen247.alarmscheduler.model.AlarmInfo
import com.carterchen247.alarmscheduler.model.DataPayload

@Entity
@TypeConverters(DataPayloadTypeConverter::class)
data class AlarmTaskEntity(
    val type: Int = -1,
    val triggerTime: Long = -1,
    val dataPayload: DataPayload? = null,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
) {
    companion object {
        fun create(alarmInfo: AlarmInfo): AlarmTaskEntity {
            return alarmInfo.run {
                AlarmTaskEntity(
                    alarmType,
                    triggerTime,
                    dataPayload,
                    if (autoGenerateId()) 0 else alarmId
                )
            }
        }
    }
}