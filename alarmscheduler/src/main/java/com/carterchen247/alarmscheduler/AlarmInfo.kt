package com.carterchen247.alarmscheduler

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class AlarmInfo(
    val alarmType: Int,
    val triggerAtMillis: Long,
    val alarmId: Int
) : Parcelable