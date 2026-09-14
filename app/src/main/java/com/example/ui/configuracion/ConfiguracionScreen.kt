package com.example.ui.configuracion

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.BusinessConfig
import com.example.ui.viewmodel.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfiguracionScreen(
    state: UiState,
    onSaveConfig: (BusinessConfig) -> Unit,
    onSetRole: (String) -> Unit
) {
    var nombreNegocio by remember(state.businessConfig) { mutableStateOf(state.businessConfig.nombreNegocio) }
    var nombreContacto by remember(state.businessConfig) { mutableStateOf(state.businessConfig.nombreContacto) }
    var email by remember(state.businessConfig) { mutableStateOf(state.businessConfig.email) }
    var telefono by remember(state.businessConfig) { mutableStateOf(state.businessConfig.telefono) }
    var rfc by remember(state.businessConfig) { mutableStateOf(state.businessConfig.rfc) }
    var logoUri by remember(state.businessConfig) { mutableStateOf(state.businessConfig.logoUri) }

    var disenoTicket by remember(state.businessConfig) { mutableStateOf(state.businessConfig.disenoTicket) }
    var agregarStatusVenta by remember(state.businessConfig) { mutableStateOf(state.businessConfig.agregarStatusVenta) }
    var formatoTicket by remember(state.businessConfig) { mutableStateOf(state.businessConfig.formatoTicket) }
    var agregarLogoTicket by remember(state.businessConfig) { mutableStateOf(state.businessConfig.agregarLogoTicket) }
    var agregarInfoCliente by remember(state.businessConfig) { mutableStateOf(state.businessConfig.agregarInfoCliente) }

    var serverUrl by remember(state.businessConfig) { mutableStateOf(state.businessConfig.serverUrl) }
    var apiToken by remember(state.businessConfig) { mutableStateOf(state.businessConfig.apiToken) }

    var editingField by remember { mutableStateOf<String?>(null) }
    var tempFieldVal by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF9F9F9))
            .verticalScroll(rememberScrollState())
            .padding(bottom = 90.dp)
            .testTag("configuracion_screen")
    ) {
        // Cyan Top Header Bar (Screenshot 1)
        TopAppBar(
            title = {
                Column {
                    Text(
                        text = state.businessConfig.nombreNegocio,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "Información de mi negocio",
                        fontSize = 13.sp,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF03A9F4)),
            actions = {
                IconButton(onClick = {}) {
                    Icon(imageVector = Icons.Default.Notifications, contentDescription = null, tint = Color.White)
                }
            }
        )

        // Orange Notice Banner (Screenshot 1)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFFF9800)) // Bright Orange
                .padding(horizontal = 14.dp, vertical = 8.dp)
        ) {
            Text(
                text = "*La información sólo se utiliza para personalizar la aplicación y los recibos.",
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                lineHeight = 16.sp
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Business Profile Form
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Nombre del negocio
            ProfileFieldRow(
                label = "Nombre del negocio",
                value = nombreNegocio,
                onEditClick = {
                    editingField = "Nombre del negocio"
                    tempFieldVal = nombreNegocio
                }
            )

            // Seleccione logo (Avatar with Question Mark & 3 Buttons)
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Seleccione logo",
                        fontSize = 12.sp,
                        color = Color(0xFF757575),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Circular Logo / Question Mark Avatar (Screenshot 1)
                    Box(
                        modifier = Modifier
                            .size(110.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFE0E0E0))
                            .border(2.dp, Color(0xFFBDBDBD), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        if (logoUri.isNotBlank()) {
                            Text(text = "🏢", fontSize = 48.sp)
                        } else {
                            Text(
                                text = "?",
                                fontSize = 64.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF424242)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Three buttons: Galería, Cámara, Borrar
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = Color(0xFF546E7A),
                            modifier = Modifier.clickable { logoUri = "https://daniisa.app/logo.png" }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Image,
                                contentDescription = "Galería",
                                tint = Color.White,
                                modifier = Modifier.padding(10.dp)
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = Color(0xFF546E7A),
                            modifier = Modifier.clickable { logoUri = "camera_snapshot.jpg" }
                        ) {
                            Icon(
                                imageVector = Icons.Default.CameraAlt,
                                contentDescription = "Cámara",
                                tint = Color.White,
                                modifier = Modifier.padding(10.dp)
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = Color(0xFFEEEEEE),
                            modifier = Modifier.clickable { logoUri = "" }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Borrar",
                                tint = Color(0xFF424242),
                                modifier = Modifier.padding(10.dp)
                            )
                        }
                    }
                }
            }

            // Nombre de contacto
            ProfileFieldRow(
                label = "Nombre de contacto",
                value = nombreContacto,
                onEditClick = {
                    editingField = "Nombre de contacto"
                    tempFieldVal = nombreContacto
                }
            )

            // Email
            ProfileFieldRow(
                label = "Email",
                value = email,
                onEditClick = {
                    editingField = "Email"
                    tempFieldVal = email
                }
            )

            // Teléfono
            ProfileFieldRow(
                label = "Teléfono",
                value = telefono,
                onEditClick = {
                    editingField = "Teléfono"
                    tempFieldVal = telefono
                }
            )

            // Información Adicional (RFC)
            ProfileFieldRow(
                label = "Información Adicional",
                value = "RFC: $rfc",
                onEditClick = {
                    editingField = "RFC"
                    tempFieldVal = rfc
                }
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Configuración recibo / ticket de venta Section (Screenshot 1)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = "Configuración",
                    fontSize = 13.sp,
                    color = Color(0xFF757575)
                )
                Text(
                    text = "Configuración recibo / ticket de venta",
                    fontSize = 12.sp,
                    color = Color(0xFF9E9E9E)
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Radio: Diseño Simple vs Diseño Extra
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = Color(0xFFEEEEEE),
                        modifier = Modifier
                            .fillMaxWidth(0.7f)
                            .clickable { disenoTicket = "Simple" }
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)) {
                            RadioButton(
                                selected = disenoTicket == "Simple",
                                onClick = { disenoTicket = "Simple" },
                                colors = RadioButtonDefaults.colors(selectedColor = Color(0xFF03A9F4)),
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "Diseño Simple", fontSize = 13.sp, color = Color(0xFF424242))
                        }
                    }

                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = Color(0xFFEEEEEE),
                        modifier = Modifier
                            .fillMaxWidth(0.7f)
                            .clickable { disenoTicket = "Extra" }
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)) {
                            RadioButton(
                                selected = disenoTicket == "Extra",
                                onClick = { disenoTicket = "Extra" },
                                colors = RadioButtonDefaults.colors(selectedColor = Color(0xFF03A9F4)),
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "Diseño Extra", fontSize = 13.sp, color = Color(0xFF424242))
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Checkbox: Agregar status en la venta
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = Color(0xFFEEEEEE),
                    modifier = Modifier.clickable { agregarStatusVenta = !agregarStatusVenta }
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)) {
                        Checkbox(
                            checked = agregarStatusVenta,
                            onCheckedChange = { agregarStatusVenta = it },
                            colors = CheckboxDefaults.colors(checkedColor = Color(0xFF03A9F4)),
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = "Agregar status en la venta", fontSize = 13.sp, color = Color(0xFF424242))
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Radio: Formato Normal vs Formato Ticket
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = Color(0xFFEEEEEE),
                        modifier = Modifier
                            .fillMaxWidth(0.7f)
                            .clickable { formatoTicket = "Normal" }
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)) {
                            RadioButton(
                                selected = formatoTicket == "Normal",
                                onClick = { formatoTicket = "Normal" },
                                colors = RadioButtonDefaults.colors(selectedColor = Color(0xFF03A9F4)),
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "Formato Normal", fontSize = 13.sp, color = Color(0xFF424242))
                        }
                    }

                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = Color(0xFFEEEEEE),
                        modifier = Modifier
                            .fillMaxWidth(0.7f)
                            .clickable { formatoTicket = "Ticket" }
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)) {
                            RadioButton(
                                selected = formatoTicket == "Ticket",
                                onClick = { formatoTicket = "Ticket" },
                                colors = RadioButtonDefaults.colors(selectedColor = Color(0xFF03A9F4)),
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "Formato Ticket", fontSize = 13.sp, color = Color(0xFF424242))
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Checkbox: Agregar logo en el ticket
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = Color(0xFFEEEEEE),
                    modifier = Modifier
                        .padding(start = 24.dp)
                        .clickable { agregarLogoTicket = !agregarLogoTicket }
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)) {
                        Checkbox(
                            checked = agregarLogoTicket,
                            onCheckedChange = { agregarLogoTicket = it },
                            colors = CheckboxDefaults.colors(checkedColor = Color(0xFF03A9F4)),
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = "Agregar logo en el ticket", fontSize = 13.sp, color = Color(0xFF424242))
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Checkbox: Agregar información del cliente en el recibo
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = Color(0xFFEEEEEE),
                    modifier = Modifier.clickable { agregarInfoCliente = !agregarInfoCliente }
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)) {
                        Checkbox(
                            checked = agregarInfoCliente,
                            onCheckedChange = { agregarInfoCliente = it },
                            colors = CheckboxDefaults.colors(checkedColor = Color(0xFF03A9F4)),
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Agregar Información\ndel cliente en el recibo.",
                            fontSize = 13.sp,
                            color = Color(0xFF424242),
                            lineHeight = 16.sp
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Server Endpoint & Security Token Section
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(text = "Servidor Google Apps Script & Token", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color(0xFF1565C0))
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = serverUrl,
                    onValueChange = { serverUrl = it },
                    label = { Text("URL Base Apps Script (/exec)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(6.dp))

                OutlinedTextField(
                    value = apiToken,
                    onValueChange = { apiToken = it },
                    label = { Text("Token de Seguridad") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Role Selector
                Text(text = "Rol Activo (Permisos):", fontSize = 12.sp, color = Color.Gray, fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.height(4.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("admin", "vendedor", "bodeguero").forEach { r ->
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = if (state.rol == r) Color(0xFF03A9F4) else Color(0xFFEEEEEE),
                            modifier = Modifier.clickable { onSetRole(r) }
                        ) {
                            Text(
                                text = r.replaceFirstChar { it.uppercase() },
                                color = if (state.rol == r) Color.White else Color(0xFF424242),
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Save All Settings Button
        Button(
            onClick = {
                val updatedConfig = BusinessConfig(
                    nombreNegocio = nombreNegocio.trim(),
                    logoUri = logoUri,
                    nombreContacto = nombreContacto.trim(),
                    email = email.trim(),
                    telefono = telefono.trim(),
                    rfc = rfc.trim(),
                    disenoTicket = disenoTicket,
                    agregarStatusVenta = agregarStatusVenta,
                    formatoTicket = formatoTicket,
                    agregarLogoTicket = agregarLogoTicket,
                    agregarInfoCliente = agregarInfoCliente,
                    serverUrl = serverUrl.trim(),
                    apiToken = apiToken.trim()
                )
                onSaveConfig(updatedConfig)
            },
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF03A9F4)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp)
                .height(48.dp)
                .testTag("btn_save_config")
        ) {
            Icon(imageVector = Icons.Default.Save, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Guardar Configuración", fontWeight = FontWeight.Bold, fontSize = 15.sp)
        }
    }

    // Modal to edit fields
    if (editingField != null) {
        AlertDialog(
            onDismissRequest = { editingField = null },
            title = { Text(text = "Editar $editingField") },
            text = {
                OutlinedTextField(
                    value = tempFieldVal,
                    onValueChange = { tempFieldVal = it },
                    label = { Text(editingField ?: "") },
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        when (editingField) {
                            "Nombre del negocio" -> nombreNegocio = tempFieldVal
                            "Nombre de contacto" -> nombreContacto = tempFieldVal
                            "Email" -> email = tempFieldVal
                            "Teléfono" -> telefono = tempFieldVal
                            "RFC" -> rfc = tempFieldVal
                        }
                        editingField = null
                    }
                ) {
                    Text("Aceptar")
                }
            },
            dismissButton = {
                TextButton(onClick = { editingField = null }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
fun ProfileFieldRow(
    label: String,
    value: String,
    onEditClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onEditClick() }
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = label,
                    fontSize = 11.sp,
                    color = Color(0xFF757575)
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = value.ifEmpty { "—" },
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF212121)
                )
            }

            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "Editar",
                tint = Color(0xFF616161),
                modifier = Modifier.size(18.dp)
            )
        }
    }
}
