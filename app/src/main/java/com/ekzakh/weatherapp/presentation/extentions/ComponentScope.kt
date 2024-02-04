package com.ekzakh.weatherapp.presentation.extentions

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.doOnDestroy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel

fun ComponentContext.componentScope() = CoroutineScope(Dispatchers.Main.immediate + SupervisorJob())
    .apply {
        doOnDestroy { cancel() }
    }
