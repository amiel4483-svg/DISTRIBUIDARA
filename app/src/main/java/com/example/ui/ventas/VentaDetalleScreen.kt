package com.example.ui.ventas

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.viewmodel.UiState
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VentaDetalleScreen(
    state: UiState,
    onQuantityChange: (Double) -> Unit,
    onDevolucionChange: (Double) -> Unit,
    onDescuentoChange: (Double) -> Unit,
    onEstadoPagoChange: (String) -> Unit,
    onClienteChange: (String) -> Unit,
    onFormaPagoChange: (String) -> Unit,
    onEtiquetaChange: (String) -> Unit,
    onNotasChange: (String) -> Unit,
    onGuardarVenta: () -> Unit,
    onCancelarVenta: () -> Unit
) {
    val item = state.selectedSaleItem
    var showDiscountDialog by remember { mutableStateOf(false) }
    var showClientDialog by remember { mutableStateOf(false) }
    var showPaymentDialog by remember { mutableStateOf(false) }
    var tempDiscountText by remember { mutableStateOf("") }
    var tempClientText by remember { mutableStateOf("") }

    if (item == null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("No hay producto seleccionado")
            Spacer(modifier = Modifier.height(12.dp))
            Button(onClick = onCancelarVenta) {
                Text("Volver a Ventas")
            }
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFAFAFA))
            .verticalScroll(rememberScrollState())
            .padding(bottom = 80.dp)
            .testTag("venta_detalle_screen")
    ) {
        // Cyan Top Header Bar
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
                        text = "Ventas",
                        fontSize = 13.sp,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color(0xFF03A9F4)
            ),
            actions = {
                IconButton(onClick = {}) {
                    Icon(imageVector = Icons.Default.Notifications, contentDescription = null, tint = Color.White)
                }
            }
        )

        // Horizontal Quick Action Pills
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF03A9F4))
                .padding(horizontal = 12.dp, vertical = 6.dp)
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Surface(shape = RoundedCornerShape(20.dp), color = Color.White) {
                Row(modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.History, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(15.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Historial de Ventas", color = Color(0xFF455A64), fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                }
            }
            Surface(shape = RoundedCornerShape(20.dp), color = Color.White) {
                Row(modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.ReceiptLong, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(15.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Cotización", color = Color(0xFF455A64), fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Selected Product Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            shape = RoundedCornerShape(10.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                // Product Title with box icon
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "📦", fontSize = 18.sp)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = item.product.nombre,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = Color(0xFF37474F)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Quantity / Venta / Devol / Subtotal Header Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Cantidad", fontSize = 11.sp, color = Color.Gray, modifier = Modifier.weight(1.3f))
                    Text(text = "Venta $", fontSize = 11.sp, color = Color.Gray, modifier = Modifier.weight(1f))
                    Text(text = "Devol. $", fontSize = 11.sp, color = Color.Gray, modifier = Modifier.weight(1f))
                    Text(text = "Subtotal $", fontSize = 11.sp, color = Color.Gray, modifier = Modifier.weight(1.1f))
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Interactive Inputs Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    // Minus / Plus Quantity Box
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .weight(1.3f)
                            .border(1.dp, Color(0xFFFF9800), RoundedCornerShape(6.dp))
                            .padding(horizontal = 4.dp, vertical = 2.dp)
                    ) {
                        IconButton(
                            onClick = { if (item.cantidad > 1) onQuantityChange(item.cantidad - 1) },
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(imageVector = Icons.Default.Remove, contentDescription = "Restar", tint = Color(0xFF1565C0), modifier = Modifier.size(16.dp))
                        }
                        Text(
                            text = "${item.cantidad.toInt()}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            modifier = Modifier.weight(1f),
                            color = Color(0xFF212121)
                        )
                        IconButton(
                            onClick = { onQuantityChange(item.cantidad + 1) },
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(imageVector = Icons.Default.Add, contentDescription = "Sumar", tint = Color(0xFF1565C0), modifier = Modifier.size(16.dp))
                        }
                    }

                    // Venta $ Display
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .background(Color(0xFFF5F5F5), RoundedCornerShape(6.dp))
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = String.format(Locale.US, "%.1f", item.precioVenta),
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = Color(0xFF424242)
                        )
                    }

                    // Devol. $ Input
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .border(1.dp, Color(0xFFFF9800), RoundedCornerShape(6.dp))
                            .padding(vertical = 2.dp, horizontal = 4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        TextField(
                            value = if (item.devolucion > 0) "${item.devolucion}" else "",
                            onValueChange = { onDevolucionChange(it.toDoubleOrNull() ?: 0.0) },
                            placeholder = { Text("0", fontSize = 12.sp, color = Color.Gray) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent
                            ),
                            singleLine = true
                        )
                    }

                    // Subtotal $
                    Box(
                        modifier = Modifier
                            .weight(1.1f)
                            .background(Color(0xFFE8F5E9), RoundedCornerShape(6.dp))
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = String.format(Locale.US, "%.2f", item.subtotal),
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 13.sp,
                            color = Color(0xFF2E7D32)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Action buttons: Info, Discount, Delete
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = CircleShape,
                        color = Color(0xFF29B6F6),
                        modifier = Modifier.size(30.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(imageVector = Icons.Default.Info, contentDescription = "Info", tint = Color.White, modifier = Modifier.size(18.dp))
                        }
                    }

                    Surface(
                        shape = CircleShape,
                        color = Color(0xFFFF9800),
                        modifier = Modifier
                            .size(32.dp)
                            .clickable { showDiscountDialog = true }
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(text = "+%", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }

                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = Color(0xFFFFEBEE),
                        modifier = Modifier.clickable { onCancelarVenta() }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Borrar",
                            tint = Color(0xFFD32F2F),
                            modifier = Modifier.padding(6.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Radio Options: Pagado vs Por Cobrar (Screenshot 6)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { onEstadoPagoChange("Pagado") }
            ) {
                RadioButton(
                    selected = state.saleEstadoPago == "Pagado",
                    onClick = { onEstadoPagoChange("Pagado") },
                    colors = RadioButtonDefaults.colors(selectedColor = Color(0xFF0288D1))
                )
                Text(
                    text = "Pagado",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = Color(0xFF424242)
                )
            }

            Spacer(modifier = Modifier.width(36.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { onEstadoPagoChange("Por Cobrar") }
            ) {
                RadioButton(
                    selected = state.saleEstadoPago == "Por Cobrar",
                    onClick = { onEstadoPagoChange("Por Cobrar") },
                    colors = RadioButtonDefaults.colors(selectedColor = Color(0xFF0288D1))
                )
                Text(
                    text = "Por Cobrar",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = Color(0xFF424242)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Total $ Pill Card with Discount Badge (Screenshot 6)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp),
            shape = RoundedCornerShape(10.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.LocalOffer,
                    contentDescription = null,
                    tint = Color(0xFF4CAF50),
                    modifier = Modifier.size(24.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Surface(
                    color = Color(0xFFFF9800),
                    shape = CircleShape
                ) {
                    Text(
                        text = "+%",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "Total $",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF212121)
                )

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = "$ ${String.format(Locale.US, "%,.2f", item.subtotal)}",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF1565C0)
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Optional Form Fields: Cliente, Forma de Pago, Etiqueta, Notas (Screenshot 6)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Cliente Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, RoundedCornerShape(8.dp))
                    .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(8.dp))
                    .padding(horizontal = 10.dp, vertical = 2.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(imageVector = Icons.Default.Person, contentDescription = null, tint = Color.Black, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                TextField(
                    value = state.saleCliente,
                    onValueChange = onClienteChange,
                    placeholder = { Text("Cliente (opcional)", fontSize = 13.sp, color = Color.Gray) },
                    modifier = Modifier.weight(1f),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    singleLine = true
                )
                if (state.saleCliente.isNotEmpty()) {
                    IconButton(onClick = { onClienteChange("") }, modifier = Modifier.size(24.dp)) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Borrar", tint = Color.Gray, modifier = Modifier.size(16.dp))
                    }
                }
                IconButton(onClick = { showClientDialog = true }, modifier = Modifier.size(32.dp)) {
                    Icon(imageVector = Icons.Default.PersonAdd, contentDescription = "Añadir", tint = Color(0xFF673AB7), modifier = Modifier.size(20.dp))
                }
            }

            // Forma de Pago Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, RoundedCornerShape(8.dp))
                    .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(8.dp))
                    .padding(horizontal = 10.dp, vertical = 2.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(imageVector = Icons.Default.AttachMoney, contentDescription = null, tint = Color.Black, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                TextField(
                    value = state.saleFormaPago,
                    onValueChange = onFormaPagoChange,
                    placeholder = { Text("Forma de pago (opcional)", fontSize = 13.sp, color = Color.Gray) },
                    modifier = Modifier.weight(1f),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    singleLine = true
                )
                IconButton(onClick = { showPaymentDialog = true }, modifier = Modifier.size(28.dp)) {
                    Text(text = "▼", fontSize = 10.sp, color = Color.Gray)
                }
            }

            // Etiqueta Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, RoundedCornerShape(8.dp))
                    .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(8.dp))
                    .padding(horizontal = 10.dp, vertical = 2.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(imageVector = Icons.Default.Tag, contentDescription = null, tint = Color.Black, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                TextField(
                    value = state.saleEtiqueta,
                    onValueChange = onEtiquetaChange,
                    placeholder = { Text("Etiqueta (opcional)", fontSize = 13.sp, color = Color.Gray) },
                    modifier = Modifier.weight(1f),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    singleLine = true
                )
            }

            // Información Adicional / Notas
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, RoundedCornerShape(8.dp))
                    .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(8.dp))
                    .padding(horizontal = 10.dp, vertical = 2.dp),
                verticalAlignment = Alignment.Top
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    tint = Color.Black,
                    modifier = Modifier
                        .padding(top = 10.dp)
                        .size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                TextField(
                    value = state.saleNotas,
                    onValueChange = onNotasChange,
                    placeholder = { Text("Información adicional de la venta (opcional)", fontSize = 13.sp, color = Color.Gray) },
                    modifier = Modifier.weight(1f),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    maxLines = 3
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Bottom Action Buttons: 🔴 Cancelar & 🟢 Guardar (Screenshot 6)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Cancelar (Red circle button)
            Surface(
                shape = CircleShape,
                color = Color(0xFFE53935), // Red
                modifier = Modifier
                    .size(54.dp)
                    .clickable { onCancelarVenta() }
                    .testTag("btn_cancelar_venta")
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Cancelar",
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(24.dp))

            // Guardar (Green pill button)
            Button(
                onClick = onGuardarVenta,
                shape = RoundedCornerShape(26.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)), // Green
                modifier = Modifier
                    .height(52.dp)
                    .weight(1f)
                    .testTag("btn_guardar_venta")
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Guardar",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }

    // Discount Dialog
    if (showDiscountDialog) {
        AlertDialog(
            onDismissRequest = { showDiscountDialog = false },
            title = { Text("Aplicar Descuento (%)") },
            text = {
                OutlinedTextField(
                    value = tempDiscountText,
                    onValueChange = { tempDiscountText = it },
                    label = { Text("Porcentaje de descuento") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    val d = tempDiscountText.toDoubleOrNull() ?: 0.0
                    onDescuentoChange(d)
                    showDiscountDialog = false
                }) {
                    Text("Aplicar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDiscountDialog = false }) { Text("Cancelar") }
            }
        )
    }

    // Quick Client Picker Dialog
    if (showClientDialog) {
        AlertDialog(
            onDismissRequest = { showClientDialog = false },
            title = { Text("Seleccionar / Nuevo Cliente") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("Abarrotes Don Lucho", "Supermercado San José", "Minimarket Los Pinos", "Tienda La Bendición", "Cliente Mostrador").forEach { cli ->
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onClienteChange(cli)
                                    showClientDialog = false
                                },
                            shape = RoundedCornerShape(6.dp),
                            color = Color(0xFFF5F5F5)
                        ) {
                            Text(text = cli, modifier = Modifier.padding(10.dp), fontWeight = FontWeight.Medium)
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showClientDialog = false }) { Text("Cerrar") }
            }
        )
    }

    // Quick Payment Picker Dialog
    if (showPaymentDialog) {
        AlertDialog(
            onDismissRequest = { showPaymentDialog = false },
            title = { Text("Forma de Pago") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("Efectivo", "Tarjeta Débito/Crédito", "Transferencia SPEI", "Crédito 15 días", "Crédito 30 días").forEach { forma ->
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onFormaPagoChange(forma)
                                    showPaymentDialog = false
                                },
                            shape = RoundedCornerShape(6.dp),
                            color = Color(0xFFF5F5F5)
                        ) {
                            Text(text = forma, modifier = Modifier.padding(10.dp), fontWeight = FontWeight.Medium)
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showPaymentDialog = false }) { Text("Cerrar") }
            }
        )
    }
}
