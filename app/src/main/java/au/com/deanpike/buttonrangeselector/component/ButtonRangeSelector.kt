package au.com.deanpike.buttonrangeselector.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import au.com.deanpike.buttonrangeselector.component.ButtonRangeConstants.DEFAULT_SELECTED_VALUE

/**
 *
 * @param rangeValues the list of values to display on the buttons
 * @param minSelectedValue the minimum selected value
 * @param maxSelectedValue the maximum selected value
 * @param selectedBorder the border colour of the selected button
 * @param includedBackground the background colour of the buttons between the minimum and maximum buttons
 * @param backgroundColour the components background colour
 * @param selectedBackground the selected buttons background colour
 * @param selectedFontColour the colour of the font
 * [LazyListState.firstVisibleItemScrollOffset]
 */

@Composable
fun ButtonRangeSelector(
    rangeValues: List<String>,
    minSelectedValue: Int = DEFAULT_SELECTED_VALUE,
    maxSelectedValue: Int = DEFAULT_SELECTED_VALUE,
    backgroundColour: Color,
    selectedBorder: Color,
    selectedBackground: Color,
    includedBackground: Color,
    selectedFontColour: Color,
    onRangeChanged: (Int, Int) -> Unit = { _, _ -> }
) {
    val lazyState = rememberLazyListState()
    val buttonValues by remember { mutableStateOf(rangeValues) }
    var minValue by remember {
        mutableIntStateOf(minSelectedValue)
    }
    var maxValue by remember {
        mutableIntStateOf(maxSelectedValue)
    }
    var clickCount by remember {
        mutableIntStateOf(0)
    }

    LaunchedEffect(minSelectedValue, maxSelectedValue) {
        minValue = minSelectedValue
        maxValue = maxSelectedValue
    }

    LazyRow(
        modifier = Modifier
            .wrapContentWidth()
            .padding(8.dp), // This is so that the button border is visible when the end buttons are selected
        state = lazyState
    ) {
        buttonValues.forEachIndexed { index, i ->
            item {
                OutlinedButton(
                    modifier = Modifier
                        .defaultMinSize(minWidth = 48.dp),
                    onClick = {
                        determineMinMAx(
                            index = index,
                            min = minValue,
                            max = maxValue,
                            clickCount = clickCount
                        ).let {
                            minValue = it.first
                            maxValue = it.second
                            onRangeChanged(minValue, maxValue)
                            clickCount++
                        }
                    },
                    shape = if (index == minValue && minValue == maxValue) {
                        // Only one value selected
                        RoundedCornerShape(16.dp)
                    } else if (index == minValue) {
                        // Start of range
                        RoundedCornerShape(
                            topStart = 16.dp,
                            topEnd = 0.dp,
                            bottomStart = 16.dp,
                            bottomEnd = 0.dp
                        )
                    } else if (index == maxValue) {
                        // End of range
                        RoundedCornerShape(
                            topStart = 0.dp,
                            topEnd = 16.dp,
                            bottomStart = 0.dp,
                            bottomEnd = 16.dp
                        )
                    } else {
                        RoundedCornerShape(0.dp)
                    },
                    border = BorderStroke(
                        width = 1.dp,
                        color = if (clickCount == 0) {
                            backgroundColour
                        } else if (index < minValue || index > maxValue) {
                            backgroundColour
                        } else if (index == minValue || index == maxValue) {
                            selectedBorder
                        } else {
                            includedBackground
                        }
                    ),
                    colors = ButtonDefaults.outlinedButtonColors().copy(
                        containerColor = if (clickCount == 0) {
                            backgroundColour
                        } else if (index < minValue || index > maxValue) {
                            backgroundColour
                        } else if (index == minValue || index == maxValue) {
                            selectedBackground
                        } else {
                            includedBackground
                        }
                    ),
                    contentPadding = PaddingValues(
                        vertical = 8.dp,
                        horizontal = 8.dp
                    )
                ) {
                    Text(
                        text = i,
                        color = if (index == minValue || index == maxValue) {
                            selectedFontColour
                        } else {
                            Color.Black
                        },
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

private fun determineMinMAx(
    index: Int = DEFAULT_SELECTED_VALUE,
    min: Int = DEFAULT_SELECTED_VALUE,
    max: Int = DEFAULT_SELECTED_VALUE,
    clickCount: Int
): Pair<Int, Int> {
    if (min == DEFAULT_SELECTED_VALUE && max == DEFAULT_SELECTED_VALUE) {
        return Pair(index, index)
    } else if (min == max && min != index) {
        // Both values are the same, so the user has previously selected one option
        return if (index < min) {
            Pair(index, max)
        } else {
            Pair(min, index)
        }
    } else if (min != max) {
        // The user has selected 2 options, so we need to set them to the same value
        // If the user has only clicked once, then determine the users selected min and max as the first click automatically selected the max value in the list
        return if (clickCount == 1) {
            if (index < min) {
                Pair(index, min)
            } else {
                Pair(min, index)
            }
        } else {
            Pair(index, index)
        }
    } else if (min == max) {
        // The user selected the same option again
        return Pair(index, index)
    }
    return Pair(DEFAULT_SELECTED_VALUE, DEFAULT_SELECTED_VALUE)
}

object ButtonRangeConstants {
    const val DEFAULT_SELECTED_VALUE = -1
}