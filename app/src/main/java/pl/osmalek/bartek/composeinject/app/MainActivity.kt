package pl.osmalek.bartek.composeinject.app

import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import javax.inject.Inject
import javax.inject.Provider
import pl.osmalek.bartek.composeinject.ComposeInject
import pl.osmalek.bartek.composeinject.Injector
import pl.osmalek.bartek.composeinject.inject
import pl.osmalek.bartek.composeinject.provider
import pl.osmalek.bartek.composeinject.rememberInject

class MainActivity : AppCompatActivity() {
    companion object {
        val component: TestComponent = DaggerTestComponent.builder().build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeInject(component.injector()) {
                Column {
                    var isSecondVisible by remember { mutableStateOf(false) }
                    TestComposable(if (isSecondVisible) Color.Blue else Color.Black)
                    ProviderComposable(if (isSecondVisible) Color.Blue else Color.Black)

                    if (isSecondVisible) TestComposable(Color.Red)
                    Button(onClick = { isSecondVisible = !isSecondVisible }) {
                        Text(text = "CLick")
                    }
                }
            }
        }
    }

    @Composable
    private fun TestComposable(color: Color, injected: Injected = rememberInject()) {
        Log.wtf("COMPOSE", "Value $color: $injected")
        Text(text = injected.value, color = color)
    }

    @Composable
    private fun ProviderComposable(color: Color, injected: Provider<Injected> = provider()) {
        val get = injected.get()
        Log.wtf("COMPOSE", "Provider $color: $get")
        Text(text = get.value, color = color)
    }

    @ComposeInject
    class Injected @Inject constructor() {
        val value: String = "Hello!"
    }
}
