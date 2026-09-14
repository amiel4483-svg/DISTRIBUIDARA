package com.example.ui.reportes

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Print
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ReportItem
import com.example.ui.viewmodel.UiState
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportesScreen(
    state: UiState,
    onToggleIncludeCharts: (Boolean) -> Unit,
    onSelectReport: (ReportItem) -> Unit,
    onDismissReportDetail: () -> Unit
) {
    val activeReports = remember(state.reportsList) {
        state.reportsList.filter { it.habilitado }
    }

    var viewingReportScreen by remember { mutableStateOf<String?>(null) }

    when (viewingReportScreen) {
        "Transacciones por Día", "1" -> {
            TransaccionesPorDiaScreen(
                state = state,
                onBack = { viewingReportScreen = null }
            )
            return
        }
        "Total de Transacciones", "5" -> {
            TotalTransaccionesScreen(
                state = state,
                onBack = { viewingReportScreen = null }
            )
            return
        }
        "Transacciones por Producto", "Transacciones por Pro...", "Top Productos Más Vendidos", "7" -> {
            TransaccionesPorProductoScreen(
                state = state,
                onBack = { viewingReportScreen = null }
            )
            return
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .testTag("reportes_screen")
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
                        text = "Reportes",
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

        // Header Controls: Vistas de reportes, Incluir Gráficas & OBTENER VISTAS DE REPORTES (Screenshot 5)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            shape = RoundedCornerShape(10.dp),
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
                    Text(
                        text = "Vistas de reportes:",
                        fontSize = 13.sp,
                        color = Color(0xFF616161)
                    )
                    Text(
                        text = "${activeReports.size}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF212121)
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    // Checkbox Incluir Gráficas
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { onToggleIncludeCharts(!state.includeReportCharts) }
                    ) {
                        Checkbox(
                            checked = state.includeReportCharts,
                            onCheckedChange = { onToggleIncludeCharts(it) },
                            colors = CheckboxDefaults.colors(checkedColor = Color(0xFF03A9F4)),
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Incluir Gráficas",
                            fontSize = 12.sp,
                            color = Color(0xFF424242)
                        )
                    }
                }

                // Yellow Button: OBTENER VISTAS DE REPORTES (Screenshot 5)
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFFE6B800), // Gold / Mustard Yellow
                    modifier = Modifier.clickable { }
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "OBTENER",
                            color = Color.White,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 11.sp
                        )
                        Text(
                            text = "VISTAS DE",
                            color = Color.White,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 11.sp
                        )
                        Text(
                            text = "REPORTES",
                            color = Color.White,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }

        // Active Grid Buttons Matrix
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 80.dp)
        ) {
            items(activeReports, key = { it.id }) { report ->
                ReportGridCard(
                    report = report,
                    onClick = {
                        val title = report.titulo.trim()
                        if (title.contains("Día", ignoreCase = true) || title.contains("Dia", ignoreCase = true) || report.id == 1) {
                            viewingReportScreen = "Transacciones por Día"
                        } else if (title.contains("Total", ignoreCase = true) || title.contains("Transacciones", ignoreCase = true) || report.id == 5) {
                            viewingReportScreen = "Total de Transacciones"
                        } else if (title.contains("Producto", ignoreCase = true) || title.contains("Top", ignoreCase = true) || report.id == 7) {
                            viewingReportScreen = "Transacciones por Producto"
                        } else {
                            onSelectReport(report)
                        }
                    }
                )
            }
        }
    }

    // Report Detail Dialog
    if (state.selectedReport != null) {
        val rep = state.selectedReport
        AlertDialog(
            onDismissRequest = onDismissReportDetail,
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "📋", fontSize = 20.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = rep.titulo, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    if (!rep.habilitado) {
                        Surface(
                            color = Color(0xFFFFEBEE),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
                                Text(text = "❌", fontSize = 18.sp)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Módulo en sincronización o deshabilitado según su plan.",
                                    color = Color(0xFFC62828),
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }

                    Text(text = rep.descripcion, fontSize = 13.sp, color = Color(0xFF424242))

                    // Summary Stats for active reports
                    Card(
                        shape = RoundedCornerShape(8.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F8E9))
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(text = "Métricas Rápidas:", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color(0xFF2E7D32))
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(text = "• Total Transacciones: ${state.salesHistory.size} registradas", fontSize = 12.sp)
                            Text(text = "• Venta del Día: $ ${String.format(Locale.US, "%,.2f", state.ventaDiaMonto)}", fontSize = 12.sp)
                            Text(text = "• Existencia en Almacén: $ ${String.format(Locale.US, "%,.2f", state.existenciaMonto)}", fontSize = 12.sp)
                            Text(text = "• Efectividad en Ruta: ${state.efectividadVenta}%", fontSize = 12.sp)
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = onDismissReportDetail) {
                    Text("Cerrar")
                }
            }
        )
    }
}

@Composable
fun ReportGridCard(
    report: ReportItem,
    onClick: () -> Unit
) {
    // Match colors exactly from Screenshot 5:
    // Verde: Color(0xFF4CAF50)
    // Amarillo: Color(0xFFFFA000)
    // Naranja: Color(0xFFFF5722)
    // Azul: Color(0xFF03A9F4)
    val cardColor = when (report.colorTipo) {
        "Verde" -> Color(0xFF4CAF50)
        "Amarillo" -> Color(0xFFFFA000)
        "Naranja" -> Color(0xFFFF5722)
        "Azul" -> Color(0xFF03A9F4)
        else -> Color(0xFF4CAF50)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(95.dp)
            .clickable { onClick() }
            .testTag("report_card_${report.id}"),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(6.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Clipboard Document Icon (Screenshot 5)
                Icon(
                    imageVector = Icons.Default.Assignment,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(26.dp)
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Title
                Text(
                    text = report.titulo,
                    color = Color.White,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    lineHeight = 13.sp,
                    maxLines = 2
                )
            }

            // Bold Strikethrough ❌ if report is disabled in screenshot 5
            if (!report.habilitado) {
                Text(
                    text = "✖",
                    color = Color.Black.copy(alpha = 0.85f),
                    fontSize = 58.sp,
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
