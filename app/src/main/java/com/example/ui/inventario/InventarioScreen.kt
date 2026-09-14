package com.example.ui.inventario

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.FileUpload
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Product
import com.example.ui.viewmodel.UiState
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InventarioScreen(
    state: UiState,
    onSearchChange: (String) -> Unit,
    onFilterTabChange: (String) -> Unit,
    onAddOrUpdateProduct: (Product) -> Unit,
    onDeleteProduct: (String) -> Unit,
    onOpenScanner: () -> Unit
) {
    var showAddDialog by remember { mutableStateOf(false) }
    var editingProduct by remember { mutableStateOf<Product?>(null) }

    val filteredProducts = remember(state.products, state.inventorySearchQuery, state.inventoryFilterTab) {
        var list = state.products
        if (state.inventorySearchQuery.isNotBlank()) {
            val q = state.inventorySearchQuery.lowercase()
            list = list.filter { it.nombre.lowercase().contains(q) || it.codigo.lowercase().contains(q) }
        }
        when (state.inventoryFilterTab) {
            "Reserva" -> list.filter { it.esReserva || it.stock > 50 }
            "Merma" -> list.filter { it.esMerma || it.stock <= 0 }
            else -> list
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
            .testTag("inventario_screen")
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Cyan Top Bar (Screenshot 3)
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
                            text = "Inventario",
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

            // Search Bar matching Screenshot 3
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .background(Color(0xFFF1F3F4), RoundedCornerShape(8.dp))
                        .padding(horizontal = 10.dp, vertical = 2.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.Search, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        TextField(
                            value = state.inventorySearchQuery,
                            onValueChange = onSearchChange,
                            placeholder = { Text("Nombre/Clave", fontSize = 14.sp, color = Color.Gray) },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("inventario_search_input"),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent
                            ),
                            singleLine = true
                        )
                        if (state.inventorySearchQuery.isNotEmpty()) {
                            IconButton(onClick = { onSearchChange("") }, modifier = Modifier.size(24.dp)) {
                                Icon(imageVector = Icons.Default.Close, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(16.dp))
                            }
                        }
                    }
                }

                // QR Scanner Button
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFFE0E0E0),
                    modifier = Modifier.clickable { onOpenScanner() }
                ) {
                    Icon(
                        imageVector = Icons.Default.QrCodeScanner,
                        contentDescription = "Escanear",
                        tint = Color(0xFF333333),
                        modifier = Modifier.padding(10.dp)
                    )
                }
            }

            // Horizontal Button Pills: Productos Reserva, Merma, Catálogo, Importar / Exportar (Screenshot 3)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 4.dp)
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                InventoryPillButton(
                    iconText = "📦",
                    title = "Productos Reserva",
                    isSelected = state.inventoryFilterTab == "Reserva",
                    onClick = { onFilterTabChange(if (state.inventoryFilterTab == "Reserva") "Todos" else "Reserva") }
                )

                InventoryPillButton(
                    iconText = "✖",
                    title = "Merma",
                    isSelected = state.inventoryFilterTab == "Merma",
                    onClick = { onFilterTabChange(if (state.inventoryFilterTab == "Merma") "Todos" else "Merma") }
                )

                InventoryPillButton(
                    iconText = "PDF",
                    title = "Catálogo",
                    isSelected = false,
                    onClick = { /* Export Catalog */ }
                )

                InventoryPillButton(
                    iconText = "CSV",
                    title = "Importar / Exportar",
                    isSelected = false,
                    onClick = { /* Import / Export */ }
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Big Cards List matching Screenshot 3
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 12.dp, vertical = 4.dp)
                    .padding(bottom = 80.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(filteredProducts, key = { it.codigo }) { prod ->
                    InventoryBigCard(
                        product = prod,
                        onEdit = {
                            editingProduct = prod
                            showAddDialog = true
                        },
                        onDelete = { onDeleteProduct(prod.codigo) }
                    )
                }

                if (filteredProducts.isEmpty()) {
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(40.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("📦", fontSize = 36.sp)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("No hay productos en esta vista", color = Color.Gray)
                        }
                    }
                }
            }
        }

        // Floating Action Button ➕ in Cyan (Screenshot 3)
        FloatingActionButton(
            onClick = {
                editingProduct = null
                showAddDialog = true
            },
            containerColor = Color(0xFF29B6F6), // Cyan matching FAB in screenshot
            contentColor = Color.White,
            shape = CircleShape,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 85.dp, end = 20.dp)
                .size(56.dp)
                .testTag("fab_add_product")
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Agregar Producto",
                modifier = Modifier.size(28.dp)
            )
        }
    }

    // Add / Edit Product Dialog
    if (showAddDialog) {
        AddEditProductDialog(
            initialProduct = editingProduct,
            onDismiss = {
                showAddDialog = false
                editingProduct = null
            },
            onSave = { prod ->
                onAddOrUpdateProduct(prod)
                showAddDialog = false
                editingProduct = null
            }
        )
    }
}

@Composable
fun InventoryPillButton(
    iconText: String,
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(18.dp),
        color = if (isSelected) Color(0xFFE1F5FE) else Color.White,
        border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) Color(0xFF03A9F4) else Color(0xFFE0E0E0)),
        modifier = Modifier.clickable { onClick() }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp)
        ) {
            Text(text = iconText, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF455A64))
            Spacer(modifier = Modifier.width(6.dp))
            Text(text = title, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = if (isSelected) Color(0xFF0288D1) else Color(0xFF455A64))
        }
    }
}

@Composable
fun InventoryBigCard(
    product: Product,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onEdit() }
            .testTag("inventory_card_${product.codigo}"),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFFB3E5FC)) // Light cyan border matching screenshot
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Circular Product Thumbnail (Screenshot 3)
            Box(
                modifier = Modifier
                    .size(62.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE1F5FE)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = when (product.grupo) {
                        "Congelados" -> "🧊"
                        "Lácteos" -> "🥛"
                        "Embutidos" -> "🥩"
                        "Abarrotes" -> "🥚"
                        else -> "📦"
                    },
                    fontSize = 28.sp
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            // Main Product Details
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = product.codigo,
                    fontSize = 12.sp,
                    color = Color(0xFF757575),
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = product.nombre,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF37474F),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(text = "Cantidad", fontSize = 11.sp, color = Color.Gray)
                        Text(
                            text = "${product.stock}",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (product.tieneStockOk) Color(0xFF212121) else Color(0xFFD32F2F)
                        )
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text(text = "Precio de Venta", fontSize = 11.sp, color = Color.Gray)
                        Text(
                            text = "$${String.format(Locale.US, "%.0f", product.precioVenta)}",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF212121)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Semaphore Icon (Green Check or Red Warning Triangle)
            if (product.tieneStockOk) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Stock OK",
                    tint = Color(0xFF4CAF50),
                    modifier = Modifier.size(20.dp)
                )
            } else {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = "Stock Bajo / Merma",
                    tint = Color(0xFFE53935),
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditProductDialog(
    initialProduct: Product?,
    onDismiss: () -> Unit,
    onSave: (Product) -> Unit
) {
    var codigo by remember { mutableStateOf(initialProduct?.codigo ?: "8236") }
    var nombre by remember { mutableStateOf(initialProduct?.nombre ?: "PIEZA ALPURA DESLACTOSADA") }
    var grupo by remember { mutableStateOf(initialProduct?.grupo ?: "Sin Categoría") }
    var stock by remember { mutableStateOf(initialProduct?.stock?.toString() ?: "422") }
    var reserva by remember { mutableStateOf("4") }
    var precioCompra by remember { mutableStateOf(initialProduct?.precioCompra?.let { String.format(Locale.US, "%.2f", it) } ?: "23.84") }
    var precioVenta by remember { mutableStateOf(initialProduct?.precioVenta?.let { String.format(Locale.US, "%.2f", it) } ?: "26.00") }
    var precio2 by remember { mutableStateOf("26.5") }
    var precio3 by remember { mutableStateOf("25") }

    var editingFieldTitle by remember { mutableStateOf<String?>(null) }
    var editingFieldValue by remember { mutableStateOf("") }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color(0xFFF0F2F5)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Cyan Top Bar (Screenshot 1)
                TopAppBar(
                    title = {
                        Text(
                            text = "Detalle del Producto",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = onDismiss) {
                            Icon(imageVector = Icons.Default.Close, contentDescription = "Cerrar", tint = Color.White)
                        }
                    },
                    actions = {
                        IconButton(onClick = onDismiss) {
                            Icon(imageVector = Icons.Default.Delete, contentDescription = "Eliminar", tint = Color.White)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF03A9F4))
                )

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Big Image Card & Photo Action Buttons (Screenshot 1)
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            // Product Image Graphic
                            Box(
                                modifier = Modifier
                                    .size(140.dp)
                                    .background(Color(0xFFF9F9F9), RoundedCornerShape(8.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = if (nombre.contains("ALPURA", ignoreCase = true) || nombre.contains("LECHE", ignoreCase = true)) "🥛🐮" else "📦",
                                    fontSize = 60.sp
                                )
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            // 4 Icon Buttons: Galería, Cámara, Borrar, Rotar
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(4.dp),
                                    color = Color(0xFF455A64),
                                    modifier = Modifier.clickable { }
                                ) {
                                    Icon(imageVector = Icons.Default.Image, contentDescription = "Galería", tint = Color.White, modifier = Modifier.padding(8.dp))
                                }
                                Surface(
                                    shape = RoundedCornerShape(4.dp),
                                    color = Color(0xFF455A64),
                                    modifier = Modifier.clickable { }
                                ) {
                                    Icon(imageVector = Icons.Default.CameraAlt, contentDescription = "Cámara", tint = Color.White, modifier = Modifier.padding(8.dp))
                                }
                                Surface(
                                    shape = RoundedCornerShape(4.dp),
                                    color = Color(0xFF455A64),
                                    modifier = Modifier.clickable { }
                                ) {
                                    Icon(imageVector = Icons.Default.Close, contentDescription = "Borrar", tint = Color.White, modifier = Modifier.padding(8.dp))
                                }
                                Surface(
                                    shape = RoundedCornerShape(4.dp),
                                    color = Color(0xFF455A64),
                                    modifier = Modifier.clickable { }
                                ) {
                                    Icon(imageVector = Icons.Default.Refresh, contentDescription = "Rotar", tint = Color.White, modifier = Modifier.padding(8.dp))
                                }
                            }
                        }
                    }

                    // Field 1: Clave
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(6.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 14.dp, vertical = 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(text = "Clave", fontSize = 11.sp, color = Color.Gray)
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(text = "🔑", fontSize = 12.sp)
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(text = codigo, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF212121))
                                }
                            }
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                Icon(imageVector = Icons.Default.QrCodeScanner, contentDescription = "QR", tint = Color.Black, modifier = Modifier.size(18.dp))
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "Editar",
                                    tint = Color.Gray,
                                    modifier = Modifier
                                        .size(18.dp)
                                        .clickable {
                                            editingFieldTitle = "Clave"
                                            editingFieldValue = codigo
                                        }
                                )
                            }
                        }
                    }

                    // Field 2: Producto (Nombre)
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(6.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 14.dp, vertical = 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(text = "Producto", fontSize = 11.sp, color = Color.Gray)
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(text = "📦", fontSize = 12.sp)
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(text = nombre, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF212121))
                                }
                            }
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Editar",
                                tint = Color.Gray,
                                modifier = Modifier
                                    .size(18.dp)
                                    .clickable {
                                        editingFieldTitle = "Producto"
                                        editingFieldValue = nombre
                                    }
                            )
                        }
                    }

                    // Field 3: Categoría
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(6.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 14.dp, vertical = 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(text = "Categoría", fontSize = 11.sp, color = Color.Gray)
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(text = "▲", fontSize = 12.sp, color = Color.Black)
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(text = grupo, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF212121))
                                }
                            }
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                Icon(imageVector = Icons.Default.Close, contentDescription = "Limpiar", tint = Color.Black, modifier = Modifier.size(18.dp))
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "Editar",
                                    tint = Color.Gray,
                                    modifier = Modifier
                                        .size(18.dp)
                                        .clickable {
                                            editingFieldTitle = "Categoría"
                                            editingFieldValue = grupo
                                        }
                                )
                            }
                        }
                    }

                    // Row 1: Cantidad | Reserva (Screenshot 1)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Card(
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(6.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 12.dp, vertical = 10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(text = "Cantidad", fontSize = 11.sp, color = Color.Gray)
                                    Text(text = stock, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color(0xFF212121))
                                }
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "Editar",
                                    tint = Color.Gray,
                                    modifier = Modifier
                                        .size(16.dp)
                                        .clickable {
                                            editingFieldTitle = "Cantidad"
                                            editingFieldValue = stock
                                        }
                                )
                            }
                        }

                        Card(
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(6.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 12.dp, vertical = 10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(text = "Reserva", fontSize = 11.sp, color = Color.Gray)
                                    Text(text = reserva, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color(0xFF212121))
                                }
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "Editar",
                                    tint = Color.Gray,
                                    modifier = Modifier
                                        .size(16.dp)
                                        .clickable {
                                            editingFieldTitle = "Reserva"
                                            editingFieldValue = reserva
                                        }
                                )
                            }
                        }
                    }

                    // Row 2: Compra | Venta (Screenshot 1)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Card(
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(6.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 12.dp, vertical = 10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(text = "Compra", fontSize = 11.sp, color = Color.Gray)
                                    Text(text = "$$precioCompra", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color(0xFF212121))
                                }
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "Editar",
                                    tint = Color.Gray,
                                    modifier = Modifier
                                        .size(16.dp)
                                        .clickable {
                                            editingFieldTitle = "Compra"
                                            editingFieldValue = precioCompra
                                        }
                                )
                            }
                        }

                        Card(
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(6.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 12.dp, vertical = 10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(text = "Venta", fontSize = 11.sp, color = Color.Gray)
                                    Text(text = "$$precioVenta", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color(0xFF212121))
                                }
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "Editar",
                                    tint = Color.Gray,
                                    modifier = Modifier
                                        .size(16.dp)
                                        .clickable {
                                            editingFieldTitle = "Venta"
                                            editingFieldValue = precioVenta
                                        }
                                )
                            }
                        }
                    }

                    // Card: Precios de venta adicionales (opcional) (Screenshot 1)
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(6.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = "🏷️", fontSize = 12.sp)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(text = "Precios de venta adicionales (opcional)", fontSize = 12.sp, color = Color.Gray)
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                    // Precio 2
                                    Surface(
                                        shape = RoundedCornerShape(4.dp),
                                        color = Color(0xFFEEEEEE),
                                        modifier = Modifier.padding(2.dp)
                                    ) {
                                        Column(modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)) {
                                            Text(text = "Precio 2", fontSize = 9.sp, color = Color.Gray)
                                            Text(text = "$$precio2", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                                Text(text = "🗑️", fontSize = 10.sp)
                                                Text(text = "✏️", fontSize = 10.sp)
                                            }
                                        }
                                    }

                                    // Precio 3
                                    Surface(
                                        shape = RoundedCornerShape(4.dp),
                                        color = Color(0xFFEEEEEE),
                                        modifier = Modifier.padding(2.dp)
                                    ) {
                                        Column(modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)) {
                                            Text(text = "Precio 3", fontSize = 9.sp, color = Color.Gray)
                                            Text(text = "$$precio3", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                                Text(text = "🗑️", fontSize = 10.sp)
                                                Text(text = "✏️", fontSize = 10.sp)
                                            }
                                        }
                                    }
                                }

                                // Cyan + Button
                                Surface(
                                    shape = CircleShape,
                                    color = Color(0xFF03A9F4),
                                    modifier = Modifier
                                        .size(38.dp)
                                        .clickable { }
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text(text = "+", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }

                    // Card: Vincular fecha de caducidad al producto (opcional) (Screenshot 1)
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(6.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                                Text(text = "📅", fontSize = 13.sp)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Vincular fecha de caducidad al producto (opcional). ❓",
                                    fontSize = 11.sp,
                                    color = Color(0xFF616161)
                                )
                            }

                            // Cyan + Button
                            Surface(
                                shape = CircleShape,
                                color = Color(0xFF03A9F4),
                                modifier = Modifier
                                    .size(38.dp)
                                    .clickable { }
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(text = "+", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Guardar Cambios Button
                    Button(
                        onClick = {
                            val updated = Product(
                                codigo = codigo.trim(),
                                nombre = nombre.trim(),
                                grupo = grupo.trim(),
                                unidad = "Pieza",
                                stock = stock.toIntOrNull() ?: 0,
                                precioCompra = precioCompra.toDoubleOrNull() ?: 0.0,
                                precioVenta = precioVenta.toDoubleOrNull() ?: 0.0,
                                descripcion = ""
                            )
                            onSave(updated)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF03A9F4))
                    ) {
                        Text("Guardar Cambios", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    }
                }
            }
        }
    }

    // Modal field editor
    if (editingFieldTitle != null) {
        AlertDialog(
            onDismissRequest = { editingFieldTitle = null },
            title = { Text("Editar $editingFieldTitle") },
            text = {
                OutlinedTextField(
                    value = editingFieldValue,
                    onValueChange = { editingFieldValue = it },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    when (editingFieldTitle) {
                        "Clave" -> codigo = editingFieldValue
                        "Producto" -> nombre = editingFieldValue
                        "Categoría" -> grupo = editingFieldValue
                        "Cantidad" -> stock = editingFieldValue
                        "Reserva" -> reserva = editingFieldValue
                        "Compra" -> precioCompra = editingFieldValue
                        "Venta" -> precioVenta = editingFieldValue
                    }
                    editingFieldTitle = null
                }) {
                    Text("Aceptar")
                }
            },
            dismissButton = {
                TextButton(onClick = { editingFieldTitle = null }) {
                    Text("Cancelar")
                }
            }
        )
    }
}
