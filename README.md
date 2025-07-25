# Waterfall Layout Compose

A Kotlin Multiplatform Compose library that provides a waterfall (staggered grid) layout component for Compose applications.

## What is it?

Waterfall Layout Compose is a [Kotlin Multiplatform](https://kotlinlang.org/docs/multiplatform.html) library that provides a `Waterfall` composable function for creating staggered grid layouts (also known as waterfall layouts or Pinterest-style layouts) in Compose applications. Very simple to use; This layout automatically detect content height and arranges items in columns.

The library supports multiple platforms including Android, iOS, and JVM, making it perfect for cross-platform applications built with Compose Multiplatform.

`Copyright 2025 Stefanus Ayudha and Contributors`

## Preview: (wait a sec it is loading)
![Preview](preview.gif)


## Features

- **Multiplatform Support**: Works on Android, iOS, and JVM
- **Automatic Layout**: Intelligently arranges items in columns for optimal space usage
- **Customizable**: Configure gaps, padding, and column count
- **Scrollable**: Built-in vertical scrolling support
- **Compose-first**: Built specifically for Jetpack Compose and Compose Multiplatform

## Installation

### Gradle (Kotlin DSL)

Add the dependency to your module's `build.gradle.kts`:

```kotlin
dependencies {
    implementation("io.github.stefanusayudha:waterfall:1.1.0")
}
```

### Gradle (Groovy DSL)

Add the dependency to your module's `build.gradle`:

```groovy
dependencies {
    implementation 'io.github.stefanusayudha:waterfall:1.1.0'
}
```

### Maven
Artifact: [Maven Central](https://central.sonatype.com/artifact/io.github.stefanusayudha/waterfall)

Add the dependency to your `pom.xml`:

```xml
<dependency>
    <groupId>io.github.stefanusayudha</groupId>
    <artifactId>waterfall</artifactId>
    <version>1.1.0</version>
</dependency>
```

## Usage

### Basic Usage
Layout will automatically detect content height and arrange them, no configuration needed.

```kotlin
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun BasicWaterfallExample() {
    val items = listOf("Item 1", "Item 2", "Item 3", "Item 4", "Item 5")
    
    Waterfall(
        modifier = Modifier.fillMaxSize(),
        items = items,
        rowCount = 2
    ) { item ->
        Card(
            modifier = Modifier.padding(4.dp)
        ) {
            Text(
                text = item,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}
```

## API Reference

### Waterfall Composable

```kotlin
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
)
```

#### Parameters

- **modifier**: `Modifier` - Modifier to be applied to the waterfall layout
- **items**: `List<T>` - List of items to display in the waterfall layout
- **scrollState**: `ScrollState` - State object for controlling and observing scroll behavior
- **contentPadding**: `PaddingValues` - Padding around the entire content
- **verticalGap**: `Dp` - Vertical spacing between items in the same column
- **horizontalGap**: `Dp` - Horizontal spacing between columns
- **rowCount**: `Int` - Number of columns in the waterfall layout
- **content**: `@Composable (T) -> Unit` - Composable function to render each item

### Since 1.1.0
New Api is added for more customize-ability.

```kotlin
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
)
```

## Platform Support

| Platform | Support Status |
|----------|----------------|
| Android  | ✅ Supported   |
| iOS      | ✅ Supported   |
| JVM      | ✅ Supported   |
| Linux    | 🚧 Planned     |
| WasmJS   | 🚧 Planned     |

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request. For major changes, please open an issue first to discuss what you would like to change.
