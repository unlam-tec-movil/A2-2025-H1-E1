package ar.edu.unlam.mobile.scaffolding.ui.screens.auth

import android.view.ViewTreeObserver
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import ar.edu.unlam.mobile.scaffolding.ui.theme.BlueGreen

@Composable
fun MyTextField(
    modifier: Modifier = Modifier,
    label: String,
    text: String,
    onValueChange: (String) -> Unit,
    keyboardOptions: KeyboardOptions,
    keyboardActions: KeyboardActions,
    readOnly: Boolean = false,
    height: Dp = 56.dp,
    trailingIcon: ImageVector? = null,
) {
    Column(modifier = modifier) {
        Spacer(modifier = Modifier.height(10.dp))
        OutlinedTextField(
            value = text,
            onValueChange = onValueChange,
            placeholder = { Text(text = label, color = BlueGreen) },
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            readOnly = readOnly,
            trailingIcon =
                trailingIcon?.let {
                    {
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(
                                imageVector = it,
                                contentDescription = null,
                                tint = Color(0xFF828282),
                            )
                        }
                    }
                },
            shape = RoundedCornerShape(30.dp),
            colors =
                OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF317971),
                    unfocusedBorderColor = Color(0xFFEFEEEE),
                    focusedTextColor = Color(0xFF317971),
                    unfocusedTextColor = Color(0xFF317971),
                    focusedContainerColor = Color(0xFFEFEEEE),
                    unfocusedContainerColor = Color(0xFFEFEEEE),
                ),
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(height),
        )
    }
}

@Composable
fun rememberImeState(): State<Boolean> {
    val imeState =
        remember {
            mutableStateOf(false)
        }
    val view = LocalView.current
    DisposableEffect(view) {
        val listener =
            ViewTreeObserver.OnGlobalLayoutListener {
                val isKeyboardOpen =
                    ViewCompat
                        .getRootWindowInsets(view)
                        ?.isVisible(WindowInsetsCompat.Type.ime()) != false
                imeState.value = isKeyboardOpen
            }

        view.viewTreeObserver.addOnGlobalLayoutListener(listener)
        onDispose {
            view.viewTreeObserver.removeOnGlobalLayoutListener(listener)
        }
    }
    return imeState
}
