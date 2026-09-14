package com.example.ui.reportes

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Product
import com.example.data.model.ReportItem
import com.example.data.model.SaleRecord
import com.example.ui.viewmodel.UiState
import java.util.Locale

// -------------------------------------------------------------------------------------------------
// 1. TRANSACCIONES POR DÍA SCREEN (Matches WhatsApp Image 2026-09-13 at 22.07.03.jpeg)
// -------------------------------------------------------------------------------------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransaccionesPorDiaScreen(
    state: UiState,
    onBack: () -> Unit
) {
    var selectedDate by remember { mutableStateOf("2026-09-09") }
    var selectedFilterType by remember { mutableStateOf("Ventas") }
    var showFilterDropdown by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF0F2F5))
            .testTag("transacciones_dia_screen")
    ) {
        // Cyan Top App Bar
        TopAppBar(
            title = {
                Text(
                    text = "Transacciones por Día",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Cerrar", tint = Color.White)
                }
            },
            actions = {
                // [PDF] Button
                IconButton(onClick = {}) {
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = Color.Transparent,
                        border = BorderStroke(1.dp, Color.White),
                        modifier = Modifier.padding(2.dp)
                    ) {
                        Text(
                            text = "PDF",
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                        )
                    }
                }
                // [Calendar] Button
                IconButton(onClick = {}) {
                    Icon(imageVector = Icons.Default.CalendarMonth, contentDescription = "Calendario", tint = Color.White)
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF03A9F4))
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 80.dp)
        ) {
            // Filter Bar: [📅 2026-09-09] [Ventas ▼]
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Date Button
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = Color.White,
                        border = BorderStroke(1.dp, Color(0xFFCCCCCC)),
                        modifier = Modifier
                            .weight(1f)
                            .clickable { }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(imageVector = Icons.Default.CalendarToday, contentDescription = null, tint = Color.Black, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = selectedDate, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF212121))
                        }
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    // Dropdown Type
                    Box(modifier = Modifier.weight(1f)) {
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = Color.White,
                            border = BorderStroke(1.dp, Color(0xFFCCCCCC)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { showFilterDropdown = true }
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = selectedFilterType, fontSize = 13.sp, color = Color(0xFF212121))
                                Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = null, tint = Color.Black)
                            }
                        }

                        DropdownMenu(
                            expanded = showFilterDropdown,
                            onDismissRequest = { showFilterDropdown = false }
                        ) {
                            listOf("Ventas", "Compras", "Pagos", "Todos").forEach { opt ->
                                DropdownMenuItem(
                                    text = { Text(opt) },
                                    onClick = {
                                        selectedFilterType = opt
                                        showFilterDropdown = false
                                    }
                                )
                            }
                        }
                    }
                }
            }

            // Dual / Stacked Bar Chart Card (Screenshot 2: 0, 400, 800, 1200, 1600, 2000)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    DailyBarChart(
                        data = listOf(
                            Pair(450f, 120f),
                            Pair(1850f, 320f),
                            Pair(1000f, 200f),
                            Pair(320f, 60f),
                            Pair(850f, 150f),
                            Pair(620f, 100f)
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Resumen de ingresos Table Card
            GreyHeaderSection(title = "Resumen de ingresos")
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
                shape = RoundedCornerShape(0.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    ReportTableCol(label = "Ventas\nPagadas", value = "$5,050.5")
                    ReportTableCol(label = "Pagos de\nVentas", value = "$0")
                    ReportTableCol(label = "Ingresos\nPagados", value = "$0")
                    ReportTableCol(label = "Pagos de\nIngresos", value = "$0")
                    ReportTableCol(label = "Total\nIngresos", value = "$5,050.5", isHighlight = true)
                }
            }

            // Resumen de gastos Table Card
            GreyHeaderSection(title = "Resumen de gastos")
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
                shape = RoundedCornerShape(0.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    ReportTableCol(label = "Compras\nPagadas", value = "$0")
                    ReportTableCol(label = "Pagos de\nCompras", value = "$0")
                    ReportTableCol(label = "Gastos\nPagados", value = "$0")
                    ReportTableCol(label = "Salario\nEmpleados", value = "$0")
                    ReportTableCol(label = "Pagos de\nGastos", value = "$0")
                }
            }

            // Balance Highlight
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Balance $ 5,050.5",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF212121)
                )
            }

            // Ventas Section
            GreyHeaderSection(title = "Ventas")
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 6.dp)
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                DailySaleMiniCard(
                    folio = "1293",
                    time = "17:22:02",
                    client = "Sin información",
                    total = "$451",
                    ganancia = "$85.8",
                    status = "Pagado",
                    metodo = "EFECTIVO"
                )

                DailySaleMiniCard(
                    folio = "1292",
                    time = "17:15:40",
                    client = "Sin información",
                    total = "$184.5",
                    ganancia = "$19.5",
                    status = "Pagado",
                    metodo = "EFECTIVO"
                )

                DailySaleMiniCard(
                    folio = "1291",
                    time = "16:40:12",
                    client = "Abarrotes Don Pepe",
                    total = "$920.0",
                    ganancia = "$140.0",
                    status = "Pagado",
                    metodo = "EFECTIVO"
                )
            }

            // Pagos de Ventas Section
            GreyHeaderSection(title = "Pagos de Ventas")
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Text(
                    text = "Sin información",
                    color = Color.Gray,
                    fontSize = 13.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    textAlign = TextAlign.Center
                )
            }

            // Compras Section
            GreyHeaderSection(title = "Compras")
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Text(
                    text = "Sin información",
                    color = Color.Gray,
                    fontSize = 13.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

// -------------------------------------------------------------------------------------------------
// 2. TOTAL DE TRANSACCIONES SCREEN (Matches WhatsApp Image 2026-09-13 at 22.07.03 (1).jpeg)
// -------------------------------------------------------------------------------------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TotalTransaccionesScreen(
    state: UiState,
    onBack: () -> Unit
) {
    var includePorCobrar by remember { mutableStateOf(false) }
    var includeSinReferencia by remember { mutableStateOf(true) }
    var fechaInicial by remember { mutableStateOf("2026-08-13") }
    var fechaFinal by remember { mutableStateOf("2026-09-13") }
    var selectedType by remember { mutableStateOf("Ventas") }
    var showTypeDropdown by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF0F2F5))
            .testTag("total_transacciones_screen")
    ) {
        // Cyan Top App Bar
        TopAppBar(
            title = {
                Text(
                    text = "Total de Transacciones",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Cerrar", tint = Color.White)
                }
            },
            actions = {
                IconButton(onClick = {}) {
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = Color.Transparent,
                        border = BorderStroke(1.dp, Color.White),
                        modifier = Modifier.padding(2.dp)
                    ) {
                        Text(
                            text = "PDF",
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                        )
                    }
                }
                IconButton(onClick = {}) {
                    Icon(imageVector = Icons.Default.CalendarMonth, contentDescription = "Calendario", tint = Color.White)
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF03A9F4))
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 80.dp)
        ) {
            // Checkboxes Row: [ ] Incluir por cobrar | [✓] Incluir pagos sin referencia
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(
                                checked = includePorCobrar,
                                onCheckedChange = { includePorCobrar = it },
                                colors = CheckboxDefaults.colors(checkedColor = Color(0xFF03A9F4))
                            )
                            Text(text = "Incluir por cobrar", fontSize = 11.sp, color = Color(0xFF616161))
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(
                                checked = includeSinReferencia,
                                onCheckedChange = { includeSinReferencia = it },
                                colors = CheckboxDefaults.colors(checkedColor = Color(0xFF03A9F4))
                            )
                            Text(text = "Incluir pagos sin\nreferencia de transacción", fontSize = 10.sp, color = Color(0xFF616161), lineHeight = 11.sp)
                            Spacer(modifier = Modifier.width(2.dp))
                            Icon(imageVector = Icons.Default.Info, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(14.dp))
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    // Date range selectors: Fecha Inicial & Fecha Final
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = "Fecha Inicial", fontSize = 11.sp, color = Color.Gray, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = Color.White,
                                border = BorderStroke(1.dp, Color(0xFFCCCCCC)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(imageVector = Icons.Default.CalendarToday, contentDescription = null, tint = Color.Black, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(text = fechaInicial, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF212121))
                                }
                            }
                        }

                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = "Fecha Final", fontSize = 11.sp, color = Color.Gray, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = Color.White,
                                border = BorderStroke(1.dp, Color(0xFFCCCCCC)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(imageVector = Icons.Default.CalendarToday, contentDescription = null, tint = Color.Black, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(text = fechaFinal, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF212121))
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Dropdown "Ventas ▼"
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = Color.White,
                            border = BorderStroke(1.dp, Color(0xFFCCCCCC)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { showTypeDropdown = true }
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = selectedType, fontSize = 13.sp, color = Color(0xFF212121))
                                Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = null, tint = Color.Black)
                            }
                        }

                        DropdownMenu(
                            expanded = showTypeDropdown,
                            onDismissRequest = { showTypeDropdown = false }
                        ) {
                            listOf("Ventas", "Compras", "Todos").forEach { opt ->
                                DropdownMenuItem(
                                    text = { Text(opt) },
                                    onClick = {
                                        selectedType = opt
                                        showTypeDropdown = false
                                    }
                                )
                            }
                        }
                    }
                }
            }

            // Smooth Wave Line Chart (Screenshot 3: 0 to 12,000)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    SmoothWaveLineChart(
                        ingresosPoints = listOf(9500f, 6500f, 1500f, 4800f, 3000f, 10500f, 6200f, 9800f, 3800f, 8500f, 7000f, 11800f, 5200f, 4800f),
                        gananciasPoints = listOf(1200f, 900f, 400f, 800f, 600f, 1800f, 1100f, 1600f, 700f, 1400f, 1200f, 2100f, 900f, 800f)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Summary 3-Columns: Ingresos | Egresos | Balance
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "Ingresos", fontSize = 12.sp, color = Color.Gray)
                            Text(text = "$134,566.52", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF212121))
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "Egresos", fontSize = 12.sp, color = Color.Gray)
                            Text(text = "$0", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF212121))
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "Balance", fontSize = 12.sp, color = Color.Gray)
                            Text(text = "$134,566.52", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF212121))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Ventas Pagadas Section
            GreyHeaderSection(title = "Ventas pagadas")
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
                shape = RoundedCornerShape(0.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    ReportTableCol(label = "Cantidad", value = "522")
                    ReportTableCol(label = "Ingresos", value = "$134,566.52")
                    ReportTableCol(label = "Ganancia", value = "$21,531.05", isHighlight = true)
                }
            }

            // Ventas por Cobrar Section
            GreyHeaderSection(title = "Ventas por cobrar")
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
                shape = RoundedCornerShape(0.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        ReportTableCol(label = "Cantidad", value = "0")
                        ReportTableCol(label = "Ingresos", value = "$0")
                        ReportTableCol(label = "Deuda", value = "$0")
                        ReportTableCol(label = "Pagos", value = "$0")
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "Total Ingresos\n(incluye ventas por cobrar)", fontSize = 10.sp, color = Color.Gray, textAlign = TextAlign.Center)
                            Text(text = "$134,566.52", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "Total Ganancias\n(incluye ventas por cobrar)", fontSize = 10.sp, color = Color.Gray, textAlign = TextAlign.Center)
                            Text(text = "$21,531.05", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2E7D32))
                        }
                    }
                }
            }

            // Compras Pagadas Section
            GreyHeaderSection(title = "Compras pagadas")
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
                shape = RoundedCornerShape(0.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    ReportTableCol(label = "Cantidad", value = "0")
                    ReportTableCol(label = "Egresos", value = "$0")
                }
            }

            // Compras por Pagar Section
            GreyHeaderSection(title = "Compras por pagar")
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
                shape = RoundedCornerShape(0.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    ReportTableCol(label = "Cantidad", value = "0")
                    ReportTableCol(label = "Egresos", value = "$0")
                    ReportTableCol(label = "Deuda", value = "$0")
                    ReportTableCol(label = "Pagos", value = "$0")
                }
            }

            // Ingresos Extras Pagados Section
            GreyHeaderSection(title = "Ingresos extras pagados")
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
                shape = RoundedCornerShape(0.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    ReportTableCol(label = "Cantidad", value = "0")
                    ReportTableCol(label = "Ingresos", value = "$0")
                }
            }

            // Ingresos Extras por Cobrar Section
            GreyHeaderSection(title = "Ingresos extras por cobrar")
        }
    }
}

// -------------------------------------------------------------------------------------------------
// 3. TRANSACCIONES POR PRODUCTO SCREEN (Matches WhatsApp Image 2026-09-13 at 22.07.03 (2).jpeg)
// -------------------------------------------------------------------------------------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransaccionesPorProductoScreen(
    state: UiState,
    onBack: () -> Unit
) {
    var selectedCategory by remember { mutableStateOf("Todos los productos") }
    var showCategoryDropdown by remember { mutableStateOf(false) }
    var fechaInicial by remember { mutableStateOf("2026-09-08") }
    var fechaFinal by remember { mutableStateOf("2026-09-08") }
    var selectedTab by remember { mutableStateOf("Ventas") } // "Ventas" or "Compras"

    val sampleProductTransactions = remember {
        listOf(
            ProductTransItem("PIEZA JAMON FUD", 27, 960.0),
            ProductTransItem("PIEZA POZOLERO MORELOS", 22, 748.0),
            ProductTransItem("PIEZA SALCHICHA FUD", 21, 468.0),
            ProductTransItem("PIEZA YOGURTH VASO", 42, 345.0),
            ProductTransItem("PIEZA PHILADELHIA QUESO", 8, 345.0),
            ProductTransItem("PIEZA MANTEQUILLA 90G", 12, 249.6),
            ProductTransItem("PIEZA ALPURA DESLACTOSADA", 8, 206.0),
            ProductTransItem("PIEZA PHILADELHIA CHICA 120G", 7, 195.65),
            ProductTransItem("PIEZA CREMA ACIDA LALA 200ML", 9, 189.0),
            ProductTransItem("PIEZA GRIEGO KIDS S/AZUCAR 100G", 18, 180.0),
            ProductTransItem("PIEZA JAMON CHIMEX 250G", 6, 177.0),
            ProductTransItem("PIEZA BEBIBLE YOPLAIT", 12, 168.0),
            ProductTransItem("PIEZA YOPLAIT CHOCOGALLETA", 10, 140.0),
            ProductTransItem("PIEZA BONICE CAJA", 4, 120.0)
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF0F2F5))
            .testTag("transacciones_producto_screen")
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Top App Bar
            TopAppBar(
                title = {
                    Text(
                        text = "Transacciones por Pro...",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Cerrar", tint = Color.White)
                    }
                },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(imageVector = Icons.Default.FilterList, contentDescription = "Filtro", tint = Color.White)
                    }
                    IconButton(onClick = {}) {
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = Color.Transparent,
                            border = BorderStroke(1.dp, Color.White),
                            modifier = Modifier.padding(2.dp)
                        ) {
                            Text(
                                text = "PDF",
                                color = Color.White,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                            )
                        }
                    }
                    IconButton(onClick = {}) {
                        Icon(imageVector = Icons.Default.CalendarMonth, contentDescription = "Calendario", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF03A9F4))
            )

            // Filter Area: Category Dropdown + Date Selectors
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                shape = RoundedCornerShape(6.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(8.dp)) {
                    // Category Selector
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { showCategoryDropdown = true }
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "▲", fontSize = 12.sp, color = Color(0xFF424242))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = selectedCategory, fontSize = 14.sp, color = Color(0xFF212121), fontWeight = FontWeight.Medium)
                        }

                        DropdownMenu(
                            expanded = showCategoryDropdown,
                            onDismissRequest = { showCategoryDropdown = false }
                        ) {
                            listOf("Todos los productos", "Lácteos", "Carnes Frías", "Abarrotes", "Congelados").forEach { cat ->
                                DropdownMenuItem(
                                    text = { Text(cat) },
                                    onClick = {
                                        selectedCategory = cat
                                        showCategoryDropdown = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    // Date range
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = "Fecha Inicial", fontSize = 10.sp, color = Color.Gray, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = Color.White,
                                border = BorderStroke(1.dp, Color(0xFFCCCCCC)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                                    Icon(imageVector = Icons.Default.CalendarToday, contentDescription = null, tint = Color.Black, modifier = Modifier.size(12.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(text = fechaInicial, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }

                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = "Fecha Final", fontSize = 10.sp, color = Color.Gray, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = Color.White,
                                border = BorderStroke(1.dp, Color(0xFFCCCCCC)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                                    Icon(imageVector = Icons.Default.CalendarToday, contentDescription = null, tint = Color.Black, modifier = Modifier.size(12.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(text = fechaFinal, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }

            // Tabs: [Productos en ventas $5,105.25] | [Productos en compras $0] (Screenshot 4)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { selectedTab = "Ventas" },
                    shape = RoundedCornerShape(4.dp),
                    colors = CardDefaults.cardColors(containerColor = if (selectedTab == "Ventas") Color.White else Color(0xFFE0E0E0)),
                    border = if (selectedTab == "Ventas") BorderStroke(1.5.dp, Color(0xFF03A9F4)) else null
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "Productos en ventas", fontSize = 11.sp, color = Color(0xFF616161))
                        Text(text = "$5,105.25", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF212121))
                    }
                }

                Card(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { selectedTab = "Compras" },
                    shape = RoundedCornerShape(4.dp),
                    colors = CardDefaults.cardColors(containerColor = if (selectedTab == "Compras") Color.White else Color(0xFFE0E0E0)),
                    border = if (selectedTab == "Compras") BorderStroke(1.5.dp, Color(0xFF03A9F4)) else null
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "Productos en compras", fontSize = 11.sp, color = Color(0xFF616161))
                        Text(text = "$0", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF212121))
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // 2-Column Grid of Product Cards (Screenshot 4)
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 80.dp)
            ) {
                items(sampleProductTransactions) { item ->
                    ProductReportGridCard(item = item)
                }
            }
        }

        // Floating Action Button with geometric shapes (Screenshot 4)
        FloatingActionButton(
            onClick = { },
            containerColor = Color(0xFF03A9F4),
            contentColor = Color.White,
            shape = CircleShape,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 90.dp, end = 16.dp)
                .size(54.dp)
        ) {
            Text(text = "▲\n● ■", fontSize = 10.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center, lineHeight = 10.sp)
        }
    }
}

// -------------------------------------------------------------------------------------------------
// Helper UI Components
// -------------------------------------------------------------------------------------------------

data class ProductTransItem(
    val nombre: String,
    val cantidad: Int,
    val total: Double
)

@Composable
fun ProductReportGridCard(item: ProductTransItem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "📦", fontSize = 13.sp)
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = item.nombre,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF424242),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(text = "Cantidad", fontSize = 10.sp, color = Color.Gray)
                    Text(text = "${item.cantidad}", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF212121))
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(text = "Total", fontSize = 10.sp, color = Color.Gray)
                    Text(text = "$${if (item.total % 1.0 == 0.0) item.total.toInt().toString() else String.format(Locale.US, "%.2f", item.total)}", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF212121))
                }
            }
        }
    }
}

@Composable
fun GreyHeaderSection(title: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 2.dp)
            .background(Color(0xFF9E9E9E), RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
            .padding(vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp
        )
    }
}

@Composable
fun ReportTableCol(label: String, value: String, isHighlight: Boolean = false) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = label,
            fontSize = 10.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center,
            lineHeight = 12.sp
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = value,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = if (isHighlight) Color(0xFF0288D1) else Color(0xFF212121)
        )
    }
}

@Composable
fun DailySaleMiniCard(
    folio: String,
    time: String,
    client: String,
    total: String,
    ganancia: String,
    status: String,
    metodo: String
) {
    Card(
        modifier = Modifier.width(180.dp),
        shape = RoundedCornerShape(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Folio: $folio", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF424242))
                Text(text = "🕒 $time", fontSize = 10.sp, color = Color.Gray)
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "👤", fontSize = 11.sp)
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = client, fontSize = 10.sp, color = Color(0xFF616161), maxLines = 1, overflow = TextOverflow.Ellipsis)
            }

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(text = "Total", fontSize = 9.sp, color = Color.Gray)
                    Text(text = total, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF212121))
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(text = "Ganancia", fontSize = 9.sp, color = Color.Gray)
                    Text(text = ganancia, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2E7D32))
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = status, fontSize = 10.sp, color = Color(0xFF4CAF50), fontWeight = FontWeight.Bold)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = metodo, fontSize = 9.sp, color = Color.Gray, fontWeight = FontWeight.Medium)
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(text = "💵", fontSize = 10.sp)
                }
            }
        }
    }
}

// Dual / Stacked Bar Chart with 0, 400, 800, 1200, 1600, 2000 markers
@Composable
fun DailyBarChart(
    data: List<Pair<Float, Float>>, // (Main Blue, Accent Green)
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(150.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Left Axis
        Column(
            modifier = Modifier.height(130.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            listOf("2,000", "1,600", "1,200", "800", "400", "0").forEach { label ->
                Text(text = label, fontSize = 9.sp, color = Color.Gray)
            }
        }

        Spacer(modifier = Modifier.width(6.dp))

        // Bars Canvas
        Canvas(
            modifier = Modifier
                .weight(1f)
                .height(130.dp)
        ) {
            val maxVal = 2000f
            val barCount = data.size
            val barWidth = size.width / (barCount * 1.6f)
            val spacing = size.width / barCount

            // Draw horizontal grid lines
            for (i in 0..5) {
                val y = size.height * (i / 5f)
                drawLine(
                    color = Color(0xFFE0E0E0),
                    start = Offset(0f, y),
                    end = Offset(size.width, y),
                    strokeWidth = 1f
                )
            }

            // Draw Bars
            data.forEachIndexed { index, pair ->
                val x = index * spacing + (spacing - barWidth) / 2f
                val totalH = (pair.first / maxVal) * size.height
                val greenH = (pair.second / maxVal) * size.height

                // Blue Bar
                drawRect(
                    color = Color(0xFF0D47A1), // Deep Blue
                    topLeft = Offset(x, size.height - totalH),
                    size = Size(barWidth, totalH)
                )

                // Green bottom stacked portion
                drawRect(
                    color = Color(0xFF43A047), // Green accent
                    topLeft = Offset(x, size.height - greenH),
                    size = Size(barWidth, greenH)
                )
            }
        }

        Spacer(modifier = Modifier.width(6.dp))

        // Right Axis
        Column(
            modifier = Modifier.height(130.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            listOf("2,000", "1,600", "1,200", "800", "400", "0").forEach { label ->
                Text(text = label, fontSize = 9.sp, color = Color.Gray)
            }
        }
    }
}

// Wave Line Chart with 0 to 12,000 markers (Screenshot 3)
@Composable
fun SmoothWaveLineChart(
    ingresosPoints: List<Float>,
    gananciasPoints: List<Float>,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(160.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Left Axis
        Column(
            modifier = Modifier.height(130.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            listOf("12,000", "9,000", "6,000", "3,000", "0").forEach { label ->
                Text(text = label, fontSize = 9.sp, color = Color.Gray)
            }
        }

        Spacer(modifier = Modifier.width(6.dp))

        // Graph Canvas
        Canvas(
            modifier = Modifier
                .weight(1f)
                .height(130.dp)
        ) {
            val maxVal = 12000f
            val count = ingresosPoints.size
            val stepX = size.width / (count - 1).coerceAtLeast(1)

            // Horizontal Grid Lines
            for (i in 0..4) {
                val y = size.height * (i / 4f)
                drawLine(
                    color = Color(0xFFEEEEEE),
                    start = Offset(0f, y),
                    end = Offset(size.width, y),
                    strokeWidth = 1f
                )
            }

            // Path 1: Ingresos (Blue Line with dots)
            val pathIngresos = Path()
            ingresosPoints.forEachIndexed { i, v ->
                val x = i * stepX
                val y = size.height - (v / maxVal) * size.height
                if (i == 0) pathIngresos.moveTo(x, y) else pathIngresos.lineTo(x, y)
            }

            drawPath(
                path = pathIngresos,
                color = Color(0xFF0288D1), // Cyan/Blue
                style = Stroke(width = 3.5f)
            )

            // Draw Dots for Ingresos
            ingresosPoints.forEachIndexed { i, v ->
                val x = i * stepX
                val y = size.height - (v / maxVal) * size.height
                drawCircle(color = Color(0xFF01579B), radius = 4.5f, center = Offset(x, y))
                drawCircle(color = Color.White, radius = 2.5f, center = Offset(x, y))
            }

            // Path 2: Ganancias (Teal Line with dots)
            val pathGanancias = Path()
            gananciasPoints.forEachIndexed { i, v ->
                val x = i * stepX
                val y = size.height - (v / maxVal) * size.height
                if (i == 0) pathGanancias.moveTo(x, y) else pathGanancias.lineTo(x, y)
            }

            drawPath(
                path = pathGanancias,
                color = Color(0xFF26A69A), // Teal
                style = Stroke(width = 2.5f)
            )

            // Draw Dots for Ganancias
            gananciasPoints.forEachIndexed { i, v ->
                val x = i * stepX
                val y = size.height - (v / maxVal) * size.height
                drawCircle(color = Color(0xFF00796B), radius = 3.5f, center = Offset(x, y))
                drawCircle(color = Color.White, radius = 2f, center = Offset(x, y))
            }
        }

        Spacer(modifier = Modifier.width(6.dp))

        // Right Axis
        Column(
            modifier = Modifier.height(130.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            listOf("12,000", "9,000", "6,000", "3,000", "0").forEach { label ->
                Text(text = label, fontSize = 9.sp, color = Color.Gray)
            }
        }
    }
}
