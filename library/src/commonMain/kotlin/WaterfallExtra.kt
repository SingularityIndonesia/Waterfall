import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun <T> Waterfall(
    modifier: Modifier = Modifier,
    items: List<T> = emptyList(),
    scrollState: ScrollState = rememberScrollState(),
    contentPadding: (panelWidth: Dp, rowCount: Int) -> PaddingValues = { _, _ -> PaddingValues(0.dp) },
    verticalGap: (panelWidth: Dp, rowCount: Int) -> Dp = { _, _ -> 0.dp },
    horizontalGap: (panelWidth: Dp, rowCount: Int) -> Dp = { _, _ -> 0.dp },
    minWidth: (panelWidth: Dp) -> Dp = { 200.dp },
    content: @Composable (T) -> Unit = {}
) {
    val density = LocalDensity.current
    val panelWidth = remember { mutableStateOf(10.dp) }
    val rowCount = rememberUpdatedState((panelWidth.value.value / minWidth.invoke(panelWidth.value).value).toInt().takeIf { it > 0 } ?: 1)
    val verticalGap = rememberUpdatedState(verticalGap.invoke(panelWidth.value, rowCount.value))
    val horizontalGap = rememberUpdatedState(horizontalGap.invoke(panelWidth.value, rowCount.value))
    val contentPadding = rememberUpdatedState(contentPadding.invoke(panelWidth.value, rowCount.value))

    Waterfall(
        modifier = modifier
            .onSizeChanged {
                panelWidth.value = (it.width / density.density).dp
            },
        items = items,
        scrollState = scrollState,
        contentPadding = contentPadding.value,
        verticalGap = verticalGap.value,
        horizontalGap = horizontalGap.value,
        rowCount = rowCount.value,
        content = content,
    )
}