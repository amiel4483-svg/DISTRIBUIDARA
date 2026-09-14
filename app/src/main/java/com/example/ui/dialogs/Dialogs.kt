package com.example.ui.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.BusinessConfig
import com.example.data.model.SaleRecord
import java.util.Locale

@Composable
fun TicketReceiptDialog(
    sale: SaleRecord,
    config: BusinessConfig,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "🧾 Comprobante de Venta", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Cerrar")
                }
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .background(Color(0xFFFFFDE7), RoundedCornerShape(8.dp))
                    .border(1.dp, Color(0xFFFFE082), RoundedCornerShape(8.dp))
                    .padding(14.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header Logo if configured
                if (config.agregarLogoTicket) {
                    Text(text = "🏢", fontSize = 28.sp)
                    Spacer(modifier = Modifier.height(2.dp))
                }

                Text(
                    text = config.nombreNegocio,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 15.sp,
                    color = Color.Black,
                    textAlign = TextAlign.Center
                )
                Text(text = "RFC: ${config.rfc}", fontSize = 10.sp, color = Color.DarkGray)
                Text(text = "Tel: ${config.telefono} | ${config.email}", fontSize = 9.sp, color = Color.DarkGray)

                Spacer(modifier = Modifier.height(6.dp))
                Text(text = "--------------------------------", color = Color.Gray, fontFamily = FontFamily.Monospace)

                Text(
                    text = "${sale.tipoDoc} N° ${sale.id}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = Color(0xFF1565C0)
                )
                Text(text = "Fecha: ${sale.fecha}", fontSize = 11.sp, color = Color.DarkGray)
                Text(text = "Atendió: ${sale.empleado}", fontSize = 11.sp, color = Color.DarkGray)

                if (config.agregarInfoCliente || sale.cliente.isNotBlank()) {
                    Text(text = "Cliente: ${sale.cliente}", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                }

                if (config.agregarStatusVenta) {
                    Text(
                        text = "Estado: ${sale.estadoPago.uppercase()}",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (sale.estadoPago == "Pagado") Color(0xFF2E7D32) else Color(0xFFC62828)
                    )
                }

                Text(text = "--------------------------------", color = Color.Gray, fontFamily = FontFamily.Monospace)

                // Items list
                sale.items.forEach { item ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "${item.cantidad.toInt()}x ${item.product.nombre}",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = "$ ${String.format(Locale.US, "%.2f", item.subtotal)}",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    if (item.devolucion > 0) {
                        Text(text = "  Devol: -$ ${item.devolucion}", fontSize = 9.sp, color = Color(0xFFD32F2F))
                    }
                }

                Text(text = "--------------------------------", color = Color.Gray, fontFamily = FontFamily.Monospace)

                // Total Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "TOTAL:", fontWeight = FontWeight.ExtraBold, fontSize = 15.sp)
                    Text(
                        text = "$ ${String.format(Locale.US, "%,.2f", sale.total)}",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 16.sp,
                        color = Color(0xFF1565C0)
                    )
                }

                Text(text = "Forma de pago: ${sale.formaPago}", fontSize = 10.sp, color = Color.DarkGray)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "¡Gracias por su compra!", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.DarkGray)
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF03A9F4))
            ) {
                Icon(imageVector = Icons.Default.Print, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Imprimir / Aceptar")
            }
        }
    )
}

@Composable
fun BarcodeScannerDialog(
    onDismiss: () -> Unit,
    onScanned: (String) -> Unit
) {
    var manualCode by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Default.QrCodeScanner, contentDescription = null, tint = Color(0xFF03A9F4))
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Lector de Código de Barras")
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "Apunta el lector o escribe la clave del producto:",
                    fontSize = 13.sp,
                    color = Color(0xFF424242)
                )

                OutlinedTextField(
                    value = manualCode,
                    onValueChange = { manualCode = it },
                    label = { Text("Código / Clave") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Text(text = "Códigos de prueba rápidos:", fontSize = 11.sp, color = Color.Gray)
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    listOf("7501040091230", "0283", "BIM-140G").forEach { c ->
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = Color(0xFFE0F7FA),
                            modifier = Modifier.clickable { onScanned(c) }
                        ) {
                            Text(text = c, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF006064), modifier = Modifier.padding(6.dp))
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (manualCode.isNotBlank()) onScanned(manualCode)
                }
            ) {
                Text("Buscar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}

@Composable
fun SalesHistoryDialog(
    sales: List<SaleRecord>,
    onSelectSale: (SaleRecord) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "📜 Historial de Ventas (${sales.size})", fontWeight = FontWeight.Bold) },
        text = {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(350.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(sales) { sale ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSelectSale(sale) },
                        shape = RoundedCornerShape(8.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(text = "${sale.id} • ${sale.cliente}", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                Text(text = "${sale.fecha} • ${sale.formaPago}", fontSize = 11.sp, color = Color.Gray)
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = "$ ${String.format(Locale.US, "%,.2f", sale.total)}",
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 14.sp,
                                    color = Color(0xFF1565C0)
                                )
                                Text(
                                    text = sale.estadoPago,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (sale.estadoPago == "Pagado") Color(0xFF2E7D32) else Color(0xFFE65100)
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) { Text("Cerrar") }
        }
    )
}

@Composable
fun TerminarDiaDialog(
    ventaDiaTotal: Double,
    devolucionesTotal: Double,
    transaccionesCount: Int,
    urlBackend: String,
    usuario: String,
    isSyncing: Boolean,
    successMsg: String?,
    onConfirmSync: (cerrarSesion: Boolean) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { if (!isSyncing) onDismiss() },
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "🌅", fontSize = 20.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Terminar Día de Ventas",
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp,
                        color = Color(0xFF0D47A1)
                    )
                }
                if (!isSyncing) {
                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Cerrar", tint = Color.Gray)
                    }
                }
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Sincroniza y guarda el balance de operaciones realizadas por $usuario con tu base de datos Google Apps Script.",
                    fontSize = 12.sp,
                    color = Color(0xFF424242),
                    lineHeight = 16.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Balance summary box
                Surface(
                    color = Color(0xFFF1F8E9),
                    shape = RoundedCornerShape(10.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFC5E1A5)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "📊 Total Ventas del Día:", fontSize = 12.sp, color = Color(0xFF33691E), fontWeight = FontWeight.Bold)
                            Text(
                                text = "$ ${String.format(Locale.US, "%,.2f", ventaDiaTotal)}",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color(0xFF1B5E20)
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "↩️ Total Devoluciones:", fontSize = 12.sp, color = Color(0xFFB71C1C), fontWeight = FontWeight.Bold)
                            Text(
                                text = "$ ${String.format(Locale.US, "%,.2f", devolucionesTotal)}",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color(0xFFC62828)
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "🧾 Transacciones Registradas:", fontSize = 12.sp, color = Color(0xFF424242))
                            Text(text = "$transaccionesCount tickets", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1565C0))
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Backend destination
                Surface(
                    color = Color(0xFFE1F5FE),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "☁️", fontSize = 16.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(text = "Destino Apps Script", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0277BD))
                            Text(
                                text = if (urlBackend.length > 40) urlBackend.take(38) + "..." else urlBackend,
                                fontSize = 9.sp,
                                color = Color(0xFF01579B),
                                maxLines = 1
                            )
                        }
                    }
                }

                if (isSyncing) {
                    Spacer(modifier = Modifier.height(16.dp))
                    androidx.compose.material3.CircularProgressIndicator(
                        color = Color(0xFF03A9F4),
                        modifier = Modifier.size(36.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Sincronizando con Google Apps Script...",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0288D1)
                    )
                }

                if (successMsg != null) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Surface(
                        color = Color(0xFFE8F5E9),
                        shape = RoundedCornerShape(8.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFA5D6A7)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = successMsg,
                            color = Color(0xFF2E7D32),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(10.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        },
        confirmButton = {
            if (!isSyncing && successMsg == null) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { onConfirmSync(false) },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00897B)),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("🔄 Sincronizar y Mantener Sesión", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }

                    Button(
                        onClick = { onConfirmSync(true) },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F)),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("🔒 Sincronizar y Cerrar Sesión", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                }
            } else if (successMsg != null) {
                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1565C0)),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Aceptar", fontWeight = FontWeight.Bold)
                }
            }
        },
        dismissButton = {
            if (!isSyncing && successMsg == null) {
                TextButton(onClick = onDismiss) {
                    Text("Cancelar", color = Color.Gray)
                }
            }
        }
    )
}
