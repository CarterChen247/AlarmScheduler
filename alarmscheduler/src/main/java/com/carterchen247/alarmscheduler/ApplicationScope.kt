package com.carterchen247.alarmscheduler

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

val applicationScope = CoroutineScope(SupervisorJob())