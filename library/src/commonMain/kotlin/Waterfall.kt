/*
 * Copyright 2025 Stefanus Ayudha
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.layout.boundsInParent
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

@Composable
fun <T> Waterfall(
    modifier: Modifier = Modifier,
    items: List<T> = emptyList(),
    scrollState: ScrollState = rememberScrollState(),
    contentPadding: PaddingValues = PaddingValues(0.dp),
    verticalGap: Dp = 0.dp,
    horizontalGap: Dp = 0.dp,
    rowCount: Int = 2,
    content: @Composable (T) -> Unit = {}
) {
    val density = LocalDensity.current
    val panelSize = remember { mutableStateOf(IntSize.Zero) }
    val ratios = remember { mutableStateMapOf<T, Float>() }
    val itemRect = remember { mutableStateMapOf<T, Rect>() }

    val contentTopPadding = rememberUpdatedState(contentPadding.calculateTopPadding())
    val contentBottomPadding = rememberUpdatedState(contentPadding.calculateBottomPadding())
    val contentPaddingStart = rememberUpdatedState(contentPadding.calculateStartPadding(LayoutDirection.Rtl))
    val contentPaddingEnd = rememberUpdatedState(contentPadding.calculateEndPadding(LayoutDirection.Ltr))
    val totalHorizontalPadding = rememberUpdatedState(contentPaddingStart.value + contentPaddingEnd.value)
    val availableWidth = rememberUpdatedState((panelSize.value.width / density.density).dp - totalHorizontalPadding.value - horizontalGap * (rowCount - 1))
    val itemWidth = rememberUpdatedState(availableWidth.value / rowCount)
    val itemWidthPx = rememberUpdatedState(itemWidth.value.value * density.density)
    val spacerHeight = rememberUpdatedState(((itemRect.values.maxOfOrNull { it.bottom } ?: 0f) / density.density).dp)

    Box(
        modifier = modifier
            .onSizeChanged { panelSize.value = it }
            .verticalScroll(scrollState)
    ) {
        // Trigger Scrollable by drawing spacer with height == total height
        Spacer(
            modifier = Modifier
                .padding(bottom = contentBottomPadding.value)
                .size(width = 0.dp, height = spacerHeight.value)
        )

        items.map { item ->
            val position = defineYPositionRelative(
                key = item,
                keys = items,
                ratios = ratios,
                maxWidth = itemWidthPx.value,
                verticalGap = verticalGap,
                rowCount = rowCount
            )
            val rowIndex = rememberUpdatedState(position.value.first)
            val offsetTop = rememberUpdatedState(position.value.second)
            val offsetStart = rememberUpdatedState(itemWidth.value * rowIndex.value)
            val horizontalGapOffsetExtra = rememberUpdatedState(horizontalGap * rowIndex.value)

            Box(
                modifier = Modifier
                    .width(itemWidth.value)
                    .wrapContentHeight()
                    .offset(y = offsetTop.value)
                    .offset(y = contentTopPadding.value)
                    .offset(x = offsetStart.value)
                    .offset(x = contentPaddingStart.value)
                    .offset(x = horizontalGapOffsetExtra.value)
                    .onSizeChanged {
                        ratios[item] = it.width.toFloat() / it.height.toFloat()
                    }
                    .onGloballyPositioned {
                        itemRect[item] = it.boundsInParent()
                    }
            ) {
                content.invoke(item)
            }
        }
    }
}

// result.first is the row index
// result.second is the ratio relative magnitude
@Composable
private fun <T> defineYPositionRelative(
    key: T,
    keys: List<T>,
    ratios: Map<T, Float>,
    maxWidth: Float,
    verticalGap: Dp,
    rowCount: Int
): State<Pair<Int, Dp>> {
    val density = LocalDensity.current
    val rowOccupation = mutableListOf(*(0..rowCount - 1).map { 0f }.toTypedArray())
    val verticalGapPx = verticalGap.value * density.density

    for (e: T in keys) {
        if (e == key) break

        val keyRatio = ratios[e] ?: 1f
        val minimumOccupiedRowIndex = rowOccupation
            .foldIndexed(0 to Float.MAX_VALUE) { i, acc, n ->
                if (acc.second > n)
                    i to n
                else acc
            }
            .first
        rowOccupation[minimumOccupiedRowIndex] += (maxWidth / keyRatio) + verticalGapPx
    }

    val row = rowOccupation
        .foldIndexed(0 to Float.MAX_VALUE) { i, acc, n ->
            if (acc.second > n)
                i to n
            else acc
        }
    val index = row.first
    val top = (row.second / density.density).dp

    return rememberUpdatedState(index to top)
}
