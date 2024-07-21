package au.com.deanpike.buttonrangeselector

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import au.com.deanpike.buttonrangeselector.component.ButtonRangeConstants.DEFAULT_SELECTED_VALUE
import au.com.deanpike.buttonrangeselector.component.ButtonRangeSelector
import au.com.deanpike.buttonrangeselector.ui.theme.ButtonrangeselectorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ButtonrangeselectorTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Demo(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Demo(modifier: Modifier = Modifier) {
    var minValue by remember {
        mutableIntStateOf(DEFAULT_SELECTED_VALUE)
    }
    var maxValue by remember {
        mutableIntStateOf(DEFAULT_SELECTED_VALUE)
    }
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth(),
            text = "Button Range Selector",
            style = MaterialTheme.typography.headlineLarge,
            textAlign = TextAlign.Center
        )
        ButtonRangeSelector(
            rangeValues = listOf("Any", "1", "2", "3", "4", "5"),
            minSelectedValue = minValue,
            maxSelectedValue = maxValue,
            backgroundColour = Color.White,
            selectedBorder = Color.Blue,
            selectedBackground = Color.Blue,
            includedBackground = Color.Blue.copy(alpha = 0.1F),
            selectedFontColour = Color.White,
            onRangeChanged = { min, max ->
                minValue = min
                maxValue = max
            }
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(0.5F),
                text = "Minimum: $minValue"
            )
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "Maximum: $maxValue"
            )
        }

        Button(
            onClick = {
                minValue = DEFAULT_SELECTED_VALUE
                maxValue = DEFAULT_SELECTED_VALUE
            }
        ) {
            Text(text = "Reset")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ButtonrangeselectorTheme {
        Demo()
    }
}