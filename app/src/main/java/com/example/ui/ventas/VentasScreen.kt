package com.example.ui.ventas

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddBusiness
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.Work
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Product
import com.example.data.model.SaleItem
import com.example.ui.viewmodel.UiState
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VentasScreen(
    state: UiState,
    onSearchChange: (String) -> Unit,
    onAddProductToCart: (Product) -> Unit,
    onUpdateCartQuantity: (Int, Double) -> Unit,
    onUpdateCartDelta: (Int, Double) -> Unit,
    onUpdateCartPrice: (Int, Double) -> Unit,
    onUpdateCartDevolucion: (Int, Double) -> Unit,
    onUpdateCartDiscount: (Int, Double) -> Unit,
    onRemoveCartItem: (Int) -> Unit,
    onClearCart: () -> Unit,
    onEstadoPagoChange: (String) -> Unit,
    onClienteChange: (String) -> Unit,
    onFormaPagoChange: (String) -> Unit,
    onEtiquetaChange: (String) -> Unit,
    onNotasChange: (String) -> Unit,
    onGuardarVenta: () -> Unit,
    onOpenScanner: () -> Unit,
    onOpenHistory: () -> Unit,
    onOpenTerminarDia: () -> Unit = {},
    onCerrarSesion: () -> Unit = {}
) {
    var showCatalogPicker by remember { mutableStateOf(false) }
    var showNewClientDialog by remember { mutableStateOf(false) }
    var showItemDiscountDialog by remember { mutableStateOf<Int?>(null) }
    var showItemInfoDialog by remember { mutableStateOf<SaleItem?>(null) }
    var showItemQtyDialog by remember { mutableStateOf<Int?>(null) }
    var showItemPriceDialog by remember { mutableStateOf<Int?>(null) }
    var showItemDevolucionDialog by remember { mutableStateOf<Int?>(null) }
    var tempDiscountVal by remember { mutableStateOf("0") }
    var tempGenericVal by remember { mutableStateOf("") }
    var showTopDropdownMenu by remember { mutableStateOf(false) }

    val matchingProducts = remember(state.products, state.salesSearchQuery) {
        if (state.salesSearchQuery.isBlank()) emptyList()
        else {
            val q = state.salesSearchQuery.lowercase()
            state.products.filter { it.nombre.lowercase().contains(q) || it.codigo.lowercase().contains(q) }
        }
    }

    val totalCalculated = remember(state.currentCart) {
        state.currentCart.sumOf { it.subtotal }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF0F2F5))
            .testTag("ventas_screen")
    ) {
        // Cyan Top Header Bar (Screenshot 3 / Image 3)
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
            navigationIcon = {
                IconButton(onClick = {}) {
                    Icon(imageVector = Icons.Default.Menu, contentDescription = "Menú", tint = Color.White)
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF03A9F4)),
            actions = {
                IconButton(onClick = {}) {
                    Icon(imageVector = Icons.Default.Notifications, contentDescription = "Alertas", tint = Color.White)
                }
                Box {
                    IconButton(onClick = { showTopDropdownMenu = true }) {
                        Icon(imageVector = Icons.Default.MoreVert, contentDescription = "Opciones", tint = Color.White)
                    }
                    DropdownMenu(
                        expanded = showTopDropdownMenu,
                        onDismissRequest = { showTopDropdownMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("🌅 Terminar Día y Sincronizar", fontWeight = FontWeight.Bold, color = Color(0xFF00897B)) },
                            onClick = {
                                showTopDropdownMenu = false
                                onOpenTerminarDia()
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("📜 Historial de Ventas") },
                            onClick = {
                                showTopDropdownMenu = false
                                onOpenHistory()
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("🔒 Cerrar Sesión", color = Color(0xFFD32F2F)) },
                            onClick = {
                                showTopDropdownMenu = false
                                onCerrarSesion()
                            }
                        )
                    }
                }
            }
        )

        // Quick Action Pills Row: [🕒 Historial de Ventas] [🌅 Terminar Día] [📄 Cotización] [➕ Crear Venta]
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(horizontal = 12.dp, vertical = 8.dp)
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = Color.White,
                border = BorderStroke(1.dp, Color(0xFFB0BEC5)),
                modifier = Modifier.clickable { onOpenHistory() }
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Icon(imageVector = Icons.Default.History, contentDescription = null, tint = Color(0xFF546E7A), modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "Historial de Ventas", fontSize = 12.sp, color = Color(0xFF37474F), fontWeight = FontWeight.Medium)
                }
            }

            Surface(
                shape = RoundedCornerShape(20.dp),
                color = Color(0xFFE0F2F1),
                border = BorderStroke(1.dp, Color(0xFF80CBC4)),
                modifier = Modifier.clickable { onOpenTerminarDia() }
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Icon(imageVector = Icons.Default.Sync, contentDescription = null, tint = Color(0xFF00796B), modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "Terminar Día", fontSize = 12.sp, color = Color(0xFF004D40), fontWeight = FontWeight.Bold)
                }
            }

            Surface(
                shape = RoundedCornerShape(20.dp),
                color = Color.White,
                border = BorderStroke(1.dp, Color(0xFFB0BEC5)),
                modifier = Modifier.clickable { /* Cotización */ }
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Icon(imageVector = Icons.Default.Description, contentDescription = null, tint = Color(0xFF546E7A), modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "Cotización", fontSize = 12.sp, color = Color(0xFF37474F), fontWeight = FontWeight.Medium)
                }
            }

            Surface(
                shape = RoundedCornerShape(20.dp),
                color = Color.White,
                border = BorderStroke(1.dp, Color(0xFFB0BEC5)),
                modifier = Modifier.clickable { onClearCart() }
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = null, tint = Color(0xFF546E7A), modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "Crear Venta", fontSize = 12.sp, color = Color(0xFF37474F), fontWeight = FontWeight.Medium)
                }
            }
        }

        // Live Search Bar with Orange Border & 3 Action Icons (Screenshot 3)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Search Input with Orange Border
            Box(
                modifier = Modifier
                    .weight(1f)
                    .background(Color.White, RoundedCornerShape(6.dp))
                    .border(2.dp, Color(0xFFFF9800), RoundedCornerShape(6.dp))
                    .padding(horizontal = 8.dp, vertical = 2.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Search, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    TextField(
                        value = state.salesSearchQuery,
                        onValueChange = onSearchChange,
                        placeholder = { Text("Buscar producto / código...", fontSize = 14.sp, color = Color.Gray) },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("pos_search_input"),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        singleLine = true
                    )
                    if (state.salesSearchQuery.isNotEmpty()) {
                        IconButton(onClick = { onSearchChange("") }, modifier = Modifier.size(24.dp)) {
                            Icon(imageVector = Icons.Default.Close, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(16.dp))
                        }
                    }
                }
            }

            // [▲●■] Shapes / Category Button
            Surface(
                shape = RoundedCornerShape(6.dp),
                color = Color(0xFFEEEEEE),
                modifier = Modifier.clickable { showCatalogPicker = !showCatalogPicker }
            ) {
                Text(
                    text = "▲●■",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF333333),
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 11.dp)
                )
            }

            // [💼] Catalog Button
            Surface(
                shape = RoundedCornerShape(6.dp),
                color = Color(0xFFEEEEEE),
                modifier = Modifier.clickable { showCatalogPicker = true }
            ) {
                Icon(
                    imageVector = Icons.Default.Work,
                    contentDescription = "Catálogo",
                    tint = Color(0xFF333333),
                    modifier = Modifier.padding(8.dp)
                )
            }

            // [📷 Scan Barcode] Button
            Surface(
                shape = RoundedCornerShape(6.dp),
                color = Color(0xFFEEEEEE),
                modifier = Modifier.clickable { onOpenScanner() }
            ) {
                Icon(
                    imageVector = Icons.Default.QrCodeScanner,
                    contentDescription = "Escanear",
                    tint = Color(0xFF333333),
                    modifier = Modifier.padding(8.dp)
                )
            }
        }

        // Live Product Matching Suggestions Popover
        if (matchingProducts.isNotEmpty()) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 2.dp),
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(8.dp)) {
                    Text(
                        text = "Productos encontrados (Toca para agregar):",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0288D1)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    matchingProducts.take(4).forEach { prod ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onAddProductToCart(prod)
                                    onSearchChange("")
                                }
                                .padding(vertical = 6.dp, horizontal = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(text = prod.nombre, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF212121))
                                Text(text = "${prod.codigo} • Stock: ${prod.stock}", fontSize = 10.sp, color = if (prod.tieneStockOk) Color(0xFF2E7D32) else Color(0xFFC62828))
                            }
                            Text(
                                text = "+ $ ${String.format(Locale.US, "%.2f", prod.precioVenta)}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color(0xFF03A9F4)
                            )
                        }
                        HorizontalDivider(color = Color(0xFFEEEEEE))
                    }
                }
            }
        }

        // Main Scrollable Area: Cart Items Stack + Payment Summary Form
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 85.dp)
        ) {
            // Cart Items List matching Image 3
            if (state.currentCart.isEmpty()) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "🛒", fontSize = 36.sp)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(text = "El carrito de venta está vacío", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color(0xFF616161))
                        Text(text = "Busca productos arriba o abre el catálogo para agregar", fontSize = 12.sp, color = Color.Gray, textAlign = TextAlign.Center)
                    }
                }
            } else {
                state.currentCart.forEachIndexed { index, item ->
                    CartItemCard(
                        item = item,
                        onDeltaQuantity = { delta -> onUpdateCartDelta(index, delta) },
                        onQuantityChange = { q -> onUpdateCartQuantity(index, q) },
                        onPriceChange = { p -> onUpdateCartPrice(index, p) },
                        onDevolucionChange = { dev -> onUpdateCartDevolucion(index, dev) },
                        onDelete = { onRemoveCartItem(index) },
                        onOpenDiscount = {
                            showItemDiscountDialog = index
                            tempDiscountVal = item.descuentoPorc.toInt().toString()
                        },
                        onOpenInfo = { showItemInfoDialog = item },
                        onEditQty = {
                            showItemQtyDialog = index
                            tempGenericVal = if (item.cantidad % 1.0 == 0.0) item.cantidad.toInt().toString() else item.cantidad.toString()
                        },
                        onEditPrice = {
                            showItemPriceDialog = index
                            tempGenericVal = if (item.precioVenta % 1.0 == 0.0) item.precioVenta.toInt().toString() else item.precioVenta.toString()
                        },
                        onEditDevolucion = {
                            showItemDevolucionDialog = index
                            tempGenericVal = if (item.devolucion % 1.0 == 0.0) item.devolucion.toInt().toString() else item.devolucion.toString()
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Payment Status Radio: [●] Pagado / [○] Por Cobrar (Image 3)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { onEstadoPagoChange("Pagado") }
                    ) {
                        RadioButton(
                            selected = state.saleEstadoPago == "Pagado",
                            onClick = { onEstadoPagoChange("Pagado") },
                            colors = RadioButtonDefaults.colors(selectedColor = Color(0xFF03A9F4))
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "Pagado", fontSize = 14.sp, color = Color(0xFF424242), fontWeight = FontWeight.Medium)
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { onEstadoPagoChange("Por Cobrar") }
                    ) {
                        RadioButton(
                            selected = state.saleEstadoPago == "Por Cobrar",
                            onClick = { onEstadoPagoChange("Por Cobrar") },
                            colors = RadioButtonDefaults.colors(selectedColor = Color(0xFF03A9F4))
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "Por Cobrar", fontSize = 14.sp, color = Color(0xFF424242), fontWeight = FontWeight.Medium)
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Total Pill Card with Green Tag Icon & +% (Image 3)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // Green Price Tag Icon (Image 3)
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = Color(0xFF4CAF50),
                            modifier = Modifier.size(32.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(text = "🏷️", fontSize = 16.sp)
                            }
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        // +% Orange Pill
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color(0xFFFF9800)
                        ) {
                            Text(
                                text = "+%",
                                color = Color.White,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        Text(
                            text = "Total $",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF424242)
                        )
                    }

                    Text(
                        text = "${String.format(Locale.US, "%.0f", totalCalculated)}",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF212121),
                        modifier = Modifier.testTag("pos_total_amount")
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Additional Form Fields (Image 3)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                // [👤 Cliente (opcional)] [✖] [👤+]
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(6.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(imageVector = Icons.Default.Person, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
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
                                Icon(imageVector = Icons.Default.Close, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(16.dp))
                            }
                        }
                        IconButton(onClick = { showNewClientDialog = true }, modifier = Modifier.size(28.dp)) {
                            Icon(imageVector = Icons.Default.PersonAdd, contentDescription = "Nuevo Cliente", tint = Color(0xFF5E35B1))
                        }
                    }
                }

                // [💵 Forma de pago (opcional)] [✖]
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(6.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(imageVector = Icons.Default.Payments, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
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
                        if (state.saleFormaPago.isNotEmpty()) {
                            IconButton(onClick = { onFormaPagoChange("") }, modifier = Modifier.size(24.dp)) {
                                Icon(imageVector = Icons.Default.Close, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(16.dp))
                            }
                        }
                    }
                }

                // [💠 Etiqueta (opcional)] [✖] [?]
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(6.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(imageVector = Icons.Default.Tag, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
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
                        if (state.saleEtiqueta.isNotEmpty()) {
                            IconButton(onClick = { onEtiquetaChange("") }, modifier = Modifier.size(24.dp)) {
                                Icon(imageVector = Icons.Default.Close, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(16.dp))
                            }
                        }
                        Icon(imageVector = Icons.Default.HelpOutline, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(20.dp))
                    }
                }

                // [ℹ️ Información adicional de la venta (opcional)]
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(6.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(imageVector = Icons.Default.Info, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        TextField(
                            value = state.saleNotas,
                            onValueChange = onNotasChange,
                            placeholder = { Text("Información adicional de la venta\n(opcional)", fontSize = 12.sp, color = Color.Gray, lineHeight = 14.sp) },
                            modifier = Modifier.weight(1f),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent
                            ),
                            maxLines = 2
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Cancelar & Guardar Action Buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Red circular cancel button
                Surface(
                    shape = CircleShape,
                    color = Color(0xFFEF5350),
                    modifier = Modifier
                        .size(48.dp)
                        .clickable { onClearCart() }
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Cancelar Venta",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

                // Main Guardar Button (Large Green Pill)
                Button(
                    onClick = onGuardarVenta,
                    enabled = state.currentCart.isNotEmpty(),
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .testTag("btn_guardar_venta"),
                    shape = RoundedCornerShape(24.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF4CAF50),
                        disabledContainerColor = Color(0xFFA5D6A7)
                    )
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Guardar",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = Color.White
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Quick Sync / Terminar Día Bar
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = Color(0xFFECEFF1),
                border = BorderStroke(1.dp, Color(0xFFCFD8DC)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
                    .clickable { onOpenTerminarDia() }
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Sync,
                            contentDescription = null,
                            tint = Color(0xFF00796B),
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Terminar Día / Sincronizar con Apps Script",
                            fontSize = 12.sp,
                            color = Color(0xFF37474F),
                            fontWeight = FontWeight.Medium
                        )
                    }
                    Text(
                        text = "Sincronizar",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF00796B)
                    )
                }
            }
        }
    }

    // Catalog Picker Dialog
    if (showCatalogPicker) {
        AlertDialog(
            onDismissRequest = { showCatalogPicker = false },
            title = { Text(text = "📦 Catálogo de Productos", fontWeight = FontWeight.Bold) },
            text = {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(350.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    items(state.products.size) { idx ->
                        val p = state.products[idx]
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onAddProductToCart(p)
                                    showCatalogPicker = false
                                },
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(text = p.nombre, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                    Text(text = "Clave: ${p.codigo} • Stock: ${p.stock}", fontSize = 11.sp, color = Color.Gray)
                                }
                                Text(
                                    text = "$ ${String.format(Locale.US, "%.2f", p.precioVenta)}",
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 14.sp,
                                    color = Color(0xFF1565C0)
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showCatalogPicker = false }) { Text("Cerrar") }
            }
        )
    }

    // New Client Dialog
    if (showNewClientDialog) {
        var newClientName by remember { mutableStateOf("") }
        var newClientPhone by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = { showNewClientDialog = false },
            title = { Text(text = "👤 Agregar Nuevo Cliente") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = newClientName,
                        onValueChange = { newClientName = it },
                        label = { Text("Nombre del Cliente / Negocio") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = newClientPhone,
                        onValueChange = { newClientPhone = it },
                        label = { Text("Teléfono / RFC") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newClientName.isNotBlank()) {
                            onClienteChange(newClientName.trim())
                            showNewClientDialog = false
                        }
                    }
                ) {
                    Text("Guardar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showNewClientDialog = false }) { Text("Cancelar") }
            }
        )
    }

    // Edit Quantity Dialog
    if (showItemQtyDialog != null) {
        val idx = showItemQtyDialog!!
        val currentItem = state.currentCart.getOrNull(idx)
        if (currentItem != null) {
            AlertDialog(
                onDismissRequest = { showItemQtyDialog = null },
                title = { Text(text = "Modificar Cantidad: ${currentItem.product.nombre}", fontSize = 16.sp, fontWeight = FontWeight.Bold) },
                text = {
                    Column {
                        Text(text = "Ingrese la cantidad a vender:", fontSize = 13.sp, color = Color.Gray)
                        Spacer(modifier = Modifier.height(6.dp))
                        OutlinedTextField(
                            value = tempGenericVal,
                            onValueChange = { tempGenericVal = it },
                            label = { Text("Cantidad") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            val q = tempGenericVal.toDoubleOrNull() ?: 1.0
                            if (q > 0) {
                                onUpdateCartQuantity(idx, q)
                            }
                            showItemQtyDialog = null
                        }
                    ) {
                        Text("Aceptar")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showItemQtyDialog = null }) { Text("Cancelar") }
                }
            )
        }
    }

    // Edit Price Dialog
    if (showItemPriceDialog != null) {
        val idx = showItemPriceDialog!!
        val currentItem = state.currentCart.getOrNull(idx)
        if (currentItem != null) {
            AlertDialog(
                onDismissRequest = { showItemPriceDialog = null },
                title = { Text(text = "Precio de Venta: ${currentItem.product.nombre}", fontSize = 16.sp, fontWeight = FontWeight.Bold) },
                text = {
                    Column {
                        Text(text = "Ingrese el precio de venta unitario ($):", fontSize = 13.sp, color = Color.Gray)
                        Spacer(modifier = Modifier.height(6.dp))
                        OutlinedTextField(
                            value = tempGenericVal,
                            onValueChange = { tempGenericVal = it },
                            label = { Text("Venta $") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            val p = tempGenericVal.toDoubleOrNull() ?: currentItem.product.precioVenta
                            if (p >= 0) {
                                onUpdateCartPrice(idx, p)
                            }
                            showItemPriceDialog = null
                        }
                    ) {
                        Text("Aceptar")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showItemPriceDialog = null }) { Text("Cancelar") }
                }
            )
        }
    }

    // Edit Devolución Dialog
    if (showItemDevolucionDialog != null) {
        val idx = showItemDevolucionDialog!!
        val currentItem = state.currentCart.getOrNull(idx)
        if (currentItem != null) {
            AlertDialog(
                onDismissRequest = { showItemDevolucionDialog = null },
                title = { Text(text = "Devolución: ${currentItem.product.nombre}", fontSize = 16.sp, fontWeight = FontWeight.Bold) },
                text = {
                    Column {
                        Text(text = "Ingrese el monto de devolución / descuento ($):", fontSize = 13.sp, color = Color.Gray)
                        Spacer(modifier = Modifier.height(6.dp))
                        OutlinedTextField(
                            value = tempGenericVal,
                            onValueChange = { tempGenericVal = it },
                            label = { Text("Devolución $") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            val dev = tempGenericVal.toDoubleOrNull() ?: 0.0
                            if (dev >= 0) {
                                onUpdateCartDevolucion(idx, dev)
                            }
                            showItemDevolucionDialog = null
                        }
                    ) {
                        Text("Aceptar")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showItemDevolucionDialog = null }) { Text("Cancelar") }
                }
            )
        }
    }

    // Discount Dialog
    if (showItemDiscountDialog != null) {
        val idx = showItemDiscountDialog!!
        AlertDialog(
            onDismissRequest = { showItemDiscountDialog = null },
            title = { Text(text = "+% Aplicar Descuento") },
            text = {
                OutlinedTextField(
                    value = tempDiscountVal,
                    onValueChange = { tempDiscountVal = it },
                    label = { Text("Porcentaje (%)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        val d = tempDiscountVal.toDoubleOrNull() ?: 0.0
                        onUpdateCartDiscount(idx, d)
                        showItemDiscountDialog = null
                    }
                ) {
                    Text("Aplicar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showItemDiscountDialog = null }) { Text("Cancelar") }
            }
        )
    }

    // Item Info Dialog
    if (showItemInfoDialog != null) {
        val prod = showItemInfoDialog!!.product
        AlertDialog(
            onDismissRequest = { showItemInfoDialog = null },
            title = { Text(text = "ℹ️ ${prod.nombre}") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(text = "Código: ${prod.codigo}", fontWeight = FontWeight.Bold)
                    Text(text = "Categoría: ${prod.grupo}")
                    Text(text = "Unidad: ${prod.unidad}")
                    Text(text = "Existencia en bodega: ${prod.stock} unidades")
                    Text(text = "Descripción: ${prod.descripcion.ifEmpty { "Sin descripción adicional" }}")
                }
            },
            confirmButton = {
                TextButton(onClick = { showItemInfoDialog = null }) { Text("Cerrar") }
            }
        )
    }
}

// Single Cart Item Card matching Image 3 with exact columns:
// [- Cantidad +] | Venta $ | Devol. $ | Subtotal $
// and 4 input boxes with orange borders for Cantidad and Devolución, [i], [+%], [trash], blue underline
@Composable
fun CartItemCard(
    item: SaleItem,
    onDeltaQuantity: (Double) -> Unit,
    onQuantityChange: (Double) -> Unit,
    onPriceChange: (Double) -> Unit,
    onDevolucionChange: (Double) -> Unit,
    onDelete: () -> Unit,
    onOpenDiscount: () -> Unit,
    onOpenInfo: () -> Unit,
    onEditQty: () -> Unit = {},
    onEditPrice: () -> Unit = {},
    onEditDevolucion: () -> Unit = {}
) {
    val qtyStr = if (item.cantidad % 1.0 == 0.0) item.cantidad.toInt().toString() else item.cantidad.toString()
    val priceStr = if (item.precioVenta % 1.0 == 0.0) item.precioVenta.toInt().toString() else item.precioVenta.toString()
    val devolStr = if (item.devolucion > 0) (if (item.devolucion % 1.0 == 0.0) item.devolucion.toInt().toString() else item.devolucion.toString()) else ""

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 4.dp),
        shape = RoundedCornerShape(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFDE7)), // Light yellow/cream matching Image 3
        border = BorderStroke(1.dp, Color(0xFFFFF59D))
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            // Header: 📦 {PRODUCT_NAME}
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "📦", fontSize = 14.sp)
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = item.product.nombre,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF424242)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Column Headers Row (Image 3):
            // [-] Cantidad [+]   |   Venta $   |   Devol. $   |   Subtotal $
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                // Header 1: [-] Cantidad [+]
                Row(
                    modifier = Modifier.weight(1.35f),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Minus Button
                    Surface(
                        shape = CircleShape,
                        color = Color.Transparent,
                        border = BorderStroke(1.5.dp, Color(0xFF424242)),
                        modifier = Modifier
                            .size(22.dp)
                            .clickable { onDeltaQuantity(-1.0) }
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(imageVector = Icons.Default.Remove, contentDescription = "Menos", tint = Color(0xFF424242), modifier = Modifier.size(14.dp))
                        }
                    }

                    Text(
                        text = "Cantidad",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF616161)
                    )

                    // Plus Button
                    Surface(
                        shape = CircleShape,
                        color = Color.Transparent,
                        border = BorderStroke(1.5.dp, Color(0xFF424242)),
                        modifier = Modifier
                            .size(22.dp)
                            .clickable { onDeltaQuantity(1.0) }
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(imageVector = Icons.Default.Add, contentDescription = "Más", tint = Color(0xFF424242), modifier = Modifier.size(14.dp))
                        }
                    }
                }

                // Header 2: Venta $
                Box(modifier = Modifier.weight(0.9f), contentAlignment = Alignment.Center) {
                    Text(text = "Venta $", fontSize = 11.sp, color = Color(0xFF616161), textAlign = TextAlign.Center)
                }

                // Header 3: Devol. $ (Devolución)
                Box(modifier = Modifier.weight(0.9f), contentAlignment = Alignment.Center) {
                    Text(text = "Devol. $", fontSize = 11.sp, color = Color(0xFF616161), textAlign = TextAlign.Center)
                }

                // Header 4: Subtotal $
                Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                    Text(text = "Subtotal $", fontSize = 11.sp, color = Color(0xFF616161), textAlign = TextAlign.Center)
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // 4 Input Boxes Row:
            // [ Box 1 (Orange): Cantidad ] | [ Box 2 (Grey): Venta $ ] | [ Box 3 (Orange): Devol. $ ] | [ Box 4: Subtotal $ ]
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                // Box 1: Cantidad with Orange Border
                Box(
                    modifier = Modifier
                        .weight(1.35f)
                        .height(34.dp)
                        .background(Color.White, RoundedCornerShape(4.dp))
                        .border(2.dp, Color(0xFFFF9800), RoundedCornerShape(4.dp))
                        .clickable { onEditQty() },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = qtyStr,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF212121),
                        textAlign = TextAlign.Center
                    )
                }

                // Box 2: Venta $ with Grey Border
                Box(
                    modifier = Modifier
                        .weight(0.9f)
                        .height(34.dp)
                        .background(Color.White, RoundedCornerShape(4.dp))
                        .border(1.dp, Color(0xFFCCCCCC), RoundedCornerShape(4.dp))
                        .clickable { onEditPrice() },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = priceStr,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF212121),
                        textAlign = TextAlign.Center
                    )
                }

                // Box 3: Devol. $ with Orange Border
                Box(
                    modifier = Modifier
                        .weight(0.9f)
                        .height(34.dp)
                        .background(Color.White, RoundedCornerShape(4.dp))
                        .border(2.dp, Color(0xFFFF9800), RoundedCornerShape(4.dp))
                        .clickable { onEditDevolucion() },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (devolStr.isNotEmpty()) devolStr else "0.0",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (devolStr.isNotEmpty()) Color(0xFFD32F2F) else Color(0xFF9E9E9E),
                        textAlign = TextAlign.Center
                    )
                }

                // Box 4: Subtotal $ (or ...)
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(34.dp)
                        .background(Color.White, RoundedCornerShape(4.dp))
                        .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(4.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = String.format(Locale.US, "%.1f", item.subtotal),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF212121),
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Action Row: [ℹ️ (Cyan)]   [ +% (Orange) ]   [ 🗑️ (Red) ]
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Info Button (Cyan circle with white 'i')
                Surface(
                    shape = CircleShape,
                    color = Color(0xFF29B6F6),
                    modifier = Modifier
                        .size(26.dp)
                        .clickable { onOpenInfo() }
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(text = "i", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }
                }

                // Discount Button (+% Orange pill)
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFFFF9800),
                    modifier = Modifier.clickable { onOpenDiscount() }
                ) {
                    Text(
                        text = if (item.descuentoPorc > 0) "-${item.descuentoPorc.toInt()}%" else "+%",
                        color = Color.White,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp)
                    )
                }

                // Delete Button (Red outlined trash)
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = Color.Transparent,
                    border = BorderStroke(1.5.dp, Color(0xFFE53935)),
                    modifier = Modifier
                        .size(26.dp)
                        .clickable { onDelete() }
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(text = "✖", color = Color(0xFFE53935), fontWeight = FontWeight.ExtraBold, fontSize = 13.sp)
                    }
                }
            }

            // Blue underline at bottom of item card (Image 3)
            HorizontalDivider(
                color = Color(0xFF03A9F4),
                thickness = 2.dp,
                modifier = Modifier.padding(top = 8.dp)
            )

            // Warning Notice: ⚠️ *La cantidad es mayor a la que existe en el inventario. (Image 3)
            if (item.cantidad > item.product.stock) {
                Spacer(modifier = Modifier.height(6.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = null,
                        tint = Color(0xFF212121),
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "*La cantidad es mayor a la que existe en el inventario.",
                        fontSize = 10.sp,
                        color = Color(0xFF616161)
                    )
                }
            }
        }
    }
}
