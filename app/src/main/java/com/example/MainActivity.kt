package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.PointOfSale
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.configuracion.ConfiguracionScreen
import com.example.ui.dashboard.DashboardScreen
import com.example.ui.dialogs.BarcodeScannerDialog
import com.example.ui.dialogs.SalesHistoryDialog
import com.example.ui.dialogs.TicketReceiptDialog
import com.example.ui.dialogs.TerminarDiaDialog
import com.example.ui.inventario.InventarioScreen
import com.example.ui.login.LoginScreen
import com.example.ui.reportes.ReportesScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.ventas.VentaDetalleScreen
import com.example.ui.ventas.VentasScreen
import com.example.ui.viewmodel.DaniisaViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .systemBarsPadding()
                ) {
                    DaniisaApp()
                }
            }
        }
    }
}

sealed class BottomNavItem(val route: String, val title: String, val icon: ImageVector) {
    object Dashboard : BottomNavItem("Dashboard", "Dashboard", Icons.Default.Dashboard)
    object Ventas : BottomNavItem("Ventas", "Ventas", Icons.Default.ShoppingCart)
    object Inventario : BottomNavItem("Inventario", "Inventario", Icons.Default.Inventory)
    object Reportes : BottomNavItem("Reportes", "Reportes", Icons.Default.Assessment)
    object Configuracion : BottomNavItem("Configuracion", "Config.", Icons.Default.Settings)
}

@Composable
fun DaniisaApp(
    viewModel: DaniisaViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    var showSalesHistoryDialog by remember { mutableStateOf(false) }

    LaunchedEffect(state.notifAlerta) {
        state.notifAlerta?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.dismissNotification()
        }
    }

    if (state.currentScreen == "Login") {
        LoginScreen(
            state = state,
            onUsuarioChange = { viewModel.onUsuarioChange(it) },
            onPasswordChange = { viewModel.onPasswordChange(it) },
            onBtnEntrarClick = { viewModel.onBtnEntrarClick() },
            onUrlBaseChange = {},
            onDismissNotification = { viewModel.dismissNotification() }
        )
        return
    }

    val navItems = listOf(
        BottomNavItem.Dashboard,
        BottomNavItem.Ventas,
        BottomNavItem.Inventario,
        BottomNavItem.Reportes,
        BottomNavItem.Configuracion
    )

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            if (state.currentScreen != "VentaDetalle") {
                NavigationBar(
                    containerColor = Color.White,
                    tonalElevation = 8.dp,
                    modifier = Modifier
                        .navigationBarsPadding()
                        .testTag("bottom_navigation_bar")
                ) {
                    navItems.forEach { item ->
                        val selected = state.currentScreen == item.route
                        NavigationBarItem(
                            icon = {
                                Icon(
                                    imageVector = item.icon,
                                    contentDescription = item.title,
                                    modifier = Modifier.size(22.dp)
                                )
                            },
                            label = {
                                Text(
                                    text = item.title,
                                    fontSize = 10.sp,
                                    fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            selected = selected,
                            onClick = { viewModel.navigateTo(item.route) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Color(0xFF03A9F4),
                                selectedTextColor = Color(0xFF03A9F4),
                                unselectedIconColor = Color(0xFF757575),
                                unselectedTextColor = Color(0xFF757575),
                                indicatorColor = Color(0xFFE1F5FE)
                            )
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Crossfade(
                targetState = state.currentScreen,
                label = "MainNavigationCrossfade"
            ) { screen ->
                when (screen) {
                    "Dashboard" -> {
                        DashboardScreen(
                            state = state,
                            onNavigateTo = { viewModel.navigateTo(it) },
                            onOpenTerminarDia = { viewModel.openTerminarDiaDialog() }
                        )
                    }
                    "Ventas" -> {
                        VentasScreen(
                            state = state,
                            onSearchChange = { viewModel.setSalesSearchQuery(it) },
                            onAddProductToCart = { viewModel.addProductToCart(it) },
                            onUpdateCartQuantity = { idx, qty -> viewModel.updateCartItemQuantity(idx, qty) },
                            onUpdateCartDelta = { idx, delta -> viewModel.updateCartItemDelta(idx, delta) },
                            onUpdateCartPrice = { idx, price -> viewModel.updateCartItemPrice(idx, price) },
                            onUpdateCartDevolucion = { idx, dev -> viewModel.updateCartItemDevolucion(idx, dev) },
                            onUpdateCartDiscount = { idx, disc -> viewModel.updateCartItemDiscount(idx, disc) },
                            onRemoveCartItem = { idx -> viewModel.removeCartItem(idx) },
                            onClearCart = { viewModel.clearCart() },
                            onEstadoPagoChange = { viewModel.setSaleEstadoPago(it) },
                            onClienteChange = { viewModel.setSaleCliente(it) },
                            onFormaPagoChange = { viewModel.setSaleFormaPago(it) },
                            onEtiquetaChange = { viewModel.setSaleEtiqueta(it) },
                            onNotasChange = { viewModel.setSaleNotas(it) },
                            onGuardarVenta = { viewModel.finalizeSale() },
                            onOpenScanner = { viewModel.openBarcodeScanner() },
                            onOpenHistory = { showSalesHistoryDialog = true },
                            onOpenTerminarDia = { viewModel.openTerminarDiaDialog() },
                            onCerrarSesion = { viewModel.cerrarSesion() }
                        )
                    }
                    "VentaDetalle" -> {
                        VentaDetalleScreen(
                            state = state,
                            onQuantityChange = { viewModel.setSaleQuantity(it) },
                            onDevolucionChange = { viewModel.setSaleDevolucion(it) },
                            onDescuentoChange = { viewModel.setSaleDescuentoPorc(it) },
                            onEstadoPagoChange = { viewModel.setSaleEstadoPago(it) },
                            onClienteChange = { viewModel.setSaleCliente(it) },
                            onFormaPagoChange = { viewModel.setSaleFormaPago(it) },
                            onEtiquetaChange = { viewModel.setSaleEtiqueta(it) },
                            onNotasChange = { viewModel.setSaleNotas(it) },
                            onGuardarVenta = { viewModel.finalizeSale() },
                            onCancelarVenta = { viewModel.navigateTo("Ventas") }
                        )
                    }
                    "Inventario" -> {
                        InventarioScreen(
                            state = state,
                            onSearchChange = { viewModel.setInventorySearchQuery(it) },
                            onFilterTabChange = { viewModel.setInventoryFilterTab(it) },
                            onAddOrUpdateProduct = { viewModel.addOrUpdateProduct(it) },
                            onDeleteProduct = { viewModel.deleteProduct(it) },
                            onOpenScanner = { viewModel.openBarcodeScanner() }
                        )
                    }
                    "Reportes" -> {
                        ReportesScreen(
                            state = state,
                            onToggleIncludeCharts = { viewModel.toggleIncludeReportCharts(it) },
                            onSelectReport = { viewModel.selectReport(it) },
                            onDismissReportDetail = { viewModel.dismissReportDetail() }
                        )
                    }
                    "Configuracion" -> {
                        ConfiguracionScreen(
                            state = state,
                            onSaveConfig = { viewModel.updateBusinessConfig(it) },
                            onSetRole = { viewModel.setRole(it) }
                        )
                    }
                    else -> {
                        DashboardScreen(
                            state = state,
                            onNavigateTo = { viewModel.navigateTo(it) }
                        )
                    }
                }
            }
        }
    }

    // Modal Dialogs
    if (state.isScannerActive) {
        BarcodeScannerDialog(
            onDismiss = { viewModel.closeBarcodeScanner() },
            onScanned = { viewModel.onBarcodeScanned(it) }
        )
    }

    if (showSalesHistoryDialog) {
        SalesHistoryDialog(
            sales = state.salesHistory,
            onSelectSale = { sale ->
                showSalesHistoryDialog = false
                viewModel.viewTicket(sale)
            },
            onDismiss = { showSalesHistoryDialog = false }
        )
    }

    if (state.viewingTicket != null) {
        TicketReceiptDialog(
            sale = state.viewingTicket!!,
            config = state.businessConfig,
            onDismiss = { viewModel.dismissTicket() }
        )
    }

    if (state.showTerminarDiaDialog) {
        TerminarDiaDialog(
            ventaDiaTotal = state.ventaDiaMonto,
            devolucionesTotal = state.devolucionesMonto,
            transaccionesCount = state.salesHistory.size,
            urlBackend = state.urlBase,
            usuario = state.nombre,
            isSyncing = state.isSyncingDay,
            successMsg = state.terminarDiaSuccessMsg,
            onConfirmSync = { cerrarSesion ->
                viewModel.ejecutarTerminarDia(cerrarSesion)
            },
            onDismiss = { viewModel.closeTerminarDiaDialog() }
        )
    }
}
