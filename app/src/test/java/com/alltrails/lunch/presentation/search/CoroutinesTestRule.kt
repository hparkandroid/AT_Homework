package com.alltrails.lunch.presentation.search

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestWatcher
import org.junit.runner.Description

class CoroutinesTestRule : TestWatcher() {
    val testDispatchers = TestDispatchers()

    override fun starting(description: Description) {
        super.starting(description)
        Dispatchers.setMain(testDispatchers.main)
    }

    override fun finished(description: Description) {
        super.finished(description)
        Dispatchers.resetMain()
    }
}

interface Dispatchers {
    val default: CoroutineDispatcher
    val main: CoroutineDispatcher
    val unconfined: CoroutineDispatcher
    val io: CoroutineDispatcher
}

@OptIn(ExperimentalCoroutinesApi::class)
class TestDispatchers : com.alltrails.lunch.presentation.search.Dispatchers {
    val testCoroutineScheduler = TestCoroutineScheduler()

    override val default: CoroutineDispatcher
        get() = UnconfinedTestDispatcher(
            name = "default",
            scheduler = testCoroutineScheduler,
        )
    override val main: CoroutineDispatcher
        get() = UnconfinedTestDispatcher(
            name = "main",
            scheduler = testCoroutineScheduler,
        )
    override val unconfined: CoroutineDispatcher
        get() = UnconfinedTestDispatcher(
            name = "unconfined",
            scheduler = testCoroutineScheduler,
        )
    override val io: CoroutineDispatcher
        get() = UnconfinedTestDispatcher(
            name = "io",
            scheduler = testCoroutineScheduler,
        )
}
