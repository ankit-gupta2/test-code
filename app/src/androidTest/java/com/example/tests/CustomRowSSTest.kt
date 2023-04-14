package com.example.tests

import androidx.compose.ui.test.junit4.createComposeRule
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.widgets.CustomRow
import com.karumi.shot.ScreenshotTest
import org.junit.Rule
import org.junit.Test

class CustomRowSSTest : ScreenshotTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun rendersButtonWithMaxWidth() {
        composeTestRule.setContent {
            MyApplicationTheme {
                CustomRow()
            }
        }
        compareScreenshot(composeTestRule)
    }
}