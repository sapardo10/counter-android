package com.cornershop.counterstest.features.main.list

import com.cornershop.data.models.Counter

class CounterViewModel(
    val counter: Counter,
    val isSelected: () -> Boolean,
    val onCheckTapped: () -> Unit,
    val onLongTap: () -> Unit,
    val onMinusTapped: () -> Unit,
    val onPlusTapped: () -> Unit
)