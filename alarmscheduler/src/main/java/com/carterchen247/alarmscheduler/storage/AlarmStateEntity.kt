package com.carterchen247.alarmscheduler.storage

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.carterchen247.alarmscheduler.model.AlarmInfo
import com.carterchen247.alarmscheduler.model.DataPayload
import com.carterchen247.alarmscheduler.storage.converter.DataPayloadTypeConverter

@Entity
@TypeConverters(DataPayloadTypeConverter::class)
data class AlarmStateEntity(
    val type: Int = -1,
    val triggerTime: Long = -1,
    val dataPayload: DataPayload? = null,
    @PrimaryKey val id: Int = 0
) {
    companion object {
        fun create(alarmInfo: AlarmInfo): AlarmStateEntity {
            return alarmInfo.run {
                AlarmStateEntity(
                    alarmType,
                    triggerTime,
                    dataPayload,
                    alarmId
                )
            }
        }
    }
}