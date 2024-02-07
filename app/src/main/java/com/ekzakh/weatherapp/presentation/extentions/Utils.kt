package com.ekzakh.weatherapp.presentation.extentions

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.doOnDestroy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

fun ComponentContext.componentScope() = CoroutineScope(Dispatchers.Main.immediate + SupervisorJob())
    .apply {
        doOnDestroy { cancel() }
    }

fun Float.tempToFormatted(): String = "${roundToInt()}°C"

fun Calendar.formattedDateFull(): String {
    val formatter = SimpleDateFormat("EEEE | d yyy", Locale.getDefault())
    return formatter.format(this.time)
}

fun Calendar.formattedDateShort(): String {
    val formatter = SimpleDateFormat("EEE", Locale.getDefault())
    return formatter.format(this.time)
}
