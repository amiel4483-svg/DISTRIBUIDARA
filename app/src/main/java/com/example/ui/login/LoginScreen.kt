package com.example.ui.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.DaniisaBlue
import com.example.ui.theme.DaniisaGreen
import com.example.ui.theme.DaniisaRed
import com.example.ui.theme.DaniisaTextSecondary
import com.example.ui.viewmodel.UiState

@Composable
fun LoginScreen(
    state: UiState,
    onUsuarioChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onBtnEntrarClick: () -> Unit,
    onUrlBaseChange: (String) -> Unit,
    onDismissNotification: () -> Unit,
    modifier: Modifier = Modifier
) {
    var passwordVisible by remember { mutableStateOf(false) }
    var showUrlDialog by remember { mutableStateOf(false) }
    var tempUrl by remember(state.urlBase) { mutableStateOf(state.urlBase) }

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(state.notifAlerta) {
        state.notifAlerta?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            onDismissNotification()
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF4F6F9))
    ) {
        // Main Container matching LayoutLogin (Centro : 3 Horizontal, Centro : 2 Vertical)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Card wrapper for clean high-contrast presentation on mobile and tablet
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.92f)
                    .widthIn(max = 440.dp)
                    .shadow(elevation = 8.dp, shape = RoundedCornerShape(24.dp)),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // 1. imgLogo (Imagen, 100x100 dp)
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color(0xFFE3F2FD))
                            .testTag("imgLogo"),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.img_daniisa_logo),
                            contentDescription = "Logo Distribuidora Daniisa",
                            modifier = Modifier.size(100.dp),
                            contentScale = ContentScale.Crop
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // 2. lblTitulo (Etiqueta, "DISTRIBUIDORA DANIISA", 22sp, Bold, #1565C0)
                    Text(
                        text = "DISTRIBUIDORA DANIISA",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = DaniisaBlue,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.testTag("lblTitulo")
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    // 3. lblSubtitulo (Etiqueta, "Iniciar Sesión", 14sp, #666666)
                    Text(
                        text = "Iniciar Sesión",
                        fontSize = 14.sp,
                        color = DaniisaTextSecondary,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.testTag("lblSubtitulo")
                    )

                    // 4. esp1 (Espaciador)
                    Spacer(modifier = Modifier.height(24.dp))

                    // 5. lblUsuario (Etiqueta, "Usuario:", 14sp, Bold)
                    Column(
                        modifier = Modifier.fillMaxWidth(0.90f)
                    ) {
                        Text(
                            text = "Usuario:",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF333333),
                            modifier = Modifier.testTag("lblUsuario")
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        // 6. txtUsuario (CuadroDeTexto, "Escribe tu usuario", 80%-90%, 16sp, Mayusculas: Falso)
                        OutlinedTextField(
                            value = state.txtUsuario,
                            onValueChange = onUsuarioChange,
                            placeholder = { Text("Escribe tu usuario", fontSize = 15.sp, color = Color(0xFF9E9E9E)) },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = null,
                                    tint = DaniisaBlue
                                )
                            },
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp),
                            keyboardOptions = KeyboardOptions(
                                capitalization = KeyboardCapitalization.None,
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Next
                            ),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = DaniisaBlue,
                                unfocusedBorderColor = Color(0xFFD0D7DE),
                                focusedContainerColor = Color(0xFFFAFCFF),
                                unfocusedContainerColor = Color(0xFFFAFAFA)
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("txtUsuario")
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // 7. lblPassword (Etiqueta, "Contraseña:", 14sp, Bold)
                    Column(
                        modifier = Modifier.fillMaxWidth(0.90f)
                    ) {
                        Text(
                            text = "Contraseña:",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF333333),
                            modifier = Modifier.testTag("lblPassword")
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        // 8. txtPassword (CuadroDeTextoDeContraseña, "Escribe tu contraseña", 16sp)
                        OutlinedTextField(
                            value = state.txtPassword,
                            onValueChange = onPasswordChange,
                            placeholder = { Text("Escribe tu contraseña", fontSize = 15.sp, color = Color(0xFF9E9E9E)) },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Lock,
                                    contentDescription = null,
                                    tint = DaniisaBlue
                                )
                            },
                            trailingIcon = {
                                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                    Icon(
                                        imageVector = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                        contentDescription = if (passwordVisible) "Ocultar contraseña" else "Mostrar contraseña",
                                        tint = Color(0xFF757575)
                                    )
                                }
                            },
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp),
                            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Password,
                                imeAction = ImeAction.Done
                            ),
                            keyboardActions = KeyboardActions(
                                onDone = { onBtnEntrarClick() }
                            ),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = DaniisaBlue,
                                unfocusedBorderColor = Color(0xFFD0D7DE),
                                focusedContainerColor = Color(0xFFFAFCFF),
                                unfocusedContainerColor = Color(0xFFFAFAFA)
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("txtPassword")
                        )
                    }

                    // 9. esp2 (Espaciador)
                    Spacer(modifier = Modifier.height(24.dp))

                    // 10. btnEntrar (Botón, "ENTRAR" / "Conectando...", 16sp, Bold, Fondo: #27AE60, Texto: Blanco, Redondeado)
                    Button(
                        onClick = onBtnEntrarClick,
                        enabled = !state.isConnecting && !state.isInitialChecking,
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = DaniisaGreen,
                            contentColor = Color.White,
                            disabledContainerColor = Color(0xFFA9DFBF),
                            disabledContentColor = Color.White
                        ),
                        modifier = Modifier
                            .fillMaxWidth(0.90f)
                            .height(52.dp)
                            .testTag("btnEntrar")
                    ) {
                        if (state.isConnecting || state.isInitialChecking) {
                            CircularProgressIndicator(
                                color = Color.White,
                                strokeWidth = 2.5.dp,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "Conectando...",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        } else {
                            Text(
                                text = "ENTRAR",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    // 11. lblMensaje (Etiqueta, 12sp, #E74C3C, Visible si hay error)
                    if (state.lblMensajeVisible && state.lblMensajeTexto.isNotBlank()) {
                        Spacer(modifier = Modifier.height(14.dp))
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFFFDEDEC),
                            modifier = Modifier.fillMaxWidth(0.90f)
                        ) {
                            Text(
                                text = state.lblMensajeTexto,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium,
                                color = DaniisaRed,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .padding(horizontal = 12.dp, vertical = 8.dp)
                                    .testTag("lblMensaje")
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Quick access demo accounts for easy distributor testing
                    Text(
                        text = "Cuentas de prueba rápidas:",
                        fontSize = 12.sp,
                        color = DaniisaTextSecondary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.padding(horizontal = 4.dp)
                    ) {
                        OutlinedButton(
                            onClick = {
                                onUsuarioChange("admin")
                                onPasswordChange("1234")
                            },
                            shape = RoundedCornerShape(20.dp),
                            contentPadding = ButtonDefaults.ContentPadding
                        ) {
                            Text("Admin", fontSize = 11.sp, color = DaniisaBlue)
                        }
                        OutlinedButton(
                            onClick = {
                                onUsuarioChange("vendedor")
                                onPasswordChange("1234")
                            },
                            shape = RoundedCornerShape(20.dp)
                        ) {
                            Text("Ventas", fontSize = 11.sp, color = DaniisaBlue)
                        }
                        OutlinedButton(
                            onClick = {
                                onUsuarioChange("reparto")
                                onPasswordChange("1234")
                            },
                            shape = RoundedCornerShape(20.dp)
                        ) {
                            Text("Reparto", fontSize = 11.sp, color = DaniisaBlue)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // URL Endpoint Server Config Button
            TextButton(
                onClick = { showUrlDialog = true }
            ) {
                Icon(
                    imageVector = Icons.Default.Public,
                    contentDescription = null,
                    tint = DaniisaBlue,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Configurar Servidor (URL Apps Script)",
                    fontSize = 12.sp,
                    color = DaniisaBlue
                )
            }
        }

        // Notification Snackbar (Notif1.MostrarAlerta)
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        )
    }

    // Dialog for setting Google Apps Script /exec URL
    if (showUrlDialog) {
        AlertDialog(
            onDismissRequest = { showUrlDialog = false },
            title = { Text("Configuración URL_BASE", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    Text(
                        "Introduce la URL de tu Web App de Google Apps Script (/exec):",
                        fontSize = 13.sp,
                        color = Color.DarkGray
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = tempUrl,
                        onValueChange = { tempUrl = it },
                        placeholder = { Text("https://script.google.com/macros/s/.../exec") },
                        singleLine = false,
                        maxLines = 3,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onUrlBaseChange(tempUrl.trim())
                        showUrlDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = DaniisaBlue)
                ) {
                    Text("Guardar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showUrlDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}
