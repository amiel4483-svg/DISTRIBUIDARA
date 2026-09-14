package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.TinyDbStorage
import com.example.data.model.BusinessConfig
import com.example.data.model.Product
import com.example.data.model.ReportItem
import com.example.data.model.SaleItem
import com.example.data.model.SaleRecord
import com.example.data.remote.WebClient
import com.example.data.remote.WebResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class UiState(
    // Active Navigation Screen: "Login", "Dashboard", "Ventas", "Inventario", "Reportes", "Configuracion", "VentaDetalle"
    val currentScreen: String = "Dashboard",

    // Session & User
    val urlBase: String = TinyDbStorage.DEFAULT_URL_BASE,
    val token: String = TinyDbStorage.DEFAULT_TOKEN,
    val usuario: String = "oliverth",
    val nombre: String = "Oliverth",
    val rol: String = "admin", // "admin", "vendedor", "bodeguero"
    val tipoPeticion: String = "",

    // Login Form
    val txtUsuario: String = "oliverth",
    val txtPassword: String = "daniisa2026",
    val isConnecting: Boolean = false,
    val lblMensajeTexto: String = "",
    val lblMensajeVisible: Boolean = false,
    val notifAlerta: String? = null,
    val isInitialChecking: Boolean = false,

    // Business & Ticket Config
    val businessConfig: BusinessConfig = BusinessConfig(),

    // Products & Inventory
    val products: List<Product> = emptyList(),
    val inventorySearchQuery: String = "",
    val inventoryFilterTab: String = "Todos", // "Todos", "Reserva", "Merma", "Catalogo"

    // Ventas / POS State
    val salesSearchQuery: String = "",
    val selectedSaleItem: SaleItem? = null,
    val currentCart: List<SaleItem> = emptyList(),
    val saleEstadoPago: String = "Pagado", // "Pagado" or "Por Cobrar"
    val saleCliente: String = "",
    val saleClienteDoc: String = "",
    val saleFormaPago: String = "Efectivo",
    val saleEtiqueta: String = "Ruta Principal",
    val saleNotas: String = "",
    val saleDescuentoGlobal: Double = 0.0,

    // Sales History & Completed Tickets
    val salesHistory: List<SaleRecord> = emptyList(),
    val viewingTicket: SaleRecord? = null,

    // Dashboard BIMBO Metrics
    val metaPorcentaje: Double = 85.0,
    val ventaDiaMonto: Double = 6463.20,
    val devolucionesMonto: Double = 939.40,
    val devolucionesPorc: Double = 14.5,
    val productoMasVendidoNombre: String = "Donita Espolvoreada 8p 140g FLOW BIM",
    val productoMasVendidoTotal: Double = 901.80,
    val efectividadVisita: Double = 96.4,
    val efectividadVenta: Double = 92.9,
    val compraFresco: Double = 0.0,
    val existenciaMonto: Double = 4045.00,

    // 24 Reports
    val includeReportCharts: Boolean = true,
    val reportsList: List<ReportItem> = emptyList(),
    val selectedReport: ReportItem? = null,

    // Barcode Scanner
    val isScannerActive: Boolean = false,
    val lastScannedCode: String = "",

    // Terminar Día / Cerrar Sesión & Sync
    val showTerminarDiaDialog: Boolean = false,
    val isSyncingDay: Boolean = false,
    val terminarDiaSuccessMsg: String? = null
)

class DaniisaViewModel(application: Application) : AndroidViewModel(application) {
    private val tinyDb = TinyDbStorage(application.applicationContext)
    private val webClient = WebClient()

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        loadInitialData()
        inicializarApp()
    }

    private fun loadInitialData() {
        val config = tinyDb.loadBusinessConfig()
        val defaultProducts = getInitialProducts()
        val defaultReports = getInitial24Reports()
        val defaultSalesHistory = getInitialSalesHistory(defaultProducts)

        val prod1 = defaultProducts.find { it.codigo == "7501040092770" } ?: defaultProducts[1]
        val prod2 = defaultProducts.find { it.codigo == "POZ-MICH" } ?: defaultProducts[3]

        val initialCart = listOf(
            SaleItem(product = prod1, cantidad = 2.0, precioVenta = 28.50),
            SaleItem(product = prod2, cantidad = 3.0, precioVenta = 27.00)
        )

        _uiState.update {
            it.copy(
                businessConfig = config,
                products = defaultProducts,
                reportsList = defaultReports,
                salesHistory = defaultSalesHistory,
                currentCart = initialCart,
                currentScreen = "Login" // Start on Login screen as requested
            )
        }
    }

    private fun getInitialProducts(): List<Product> {
        return listOf(
            Product(
                codigo = "7501040091230",
                nombre = "PIEZA BEBIBLE YOPLAIT",
                grupo = "Lácteos",
                unidad = "Pieza",
                stock = 38,
                stockMin = 10,
                precioCompra = 9.80,
                precioVenta = 14.00,
                descripcion = "Yoghurt bebible sabor fresa 242g"
            ),
            Product(
                codigo = "7501040092770",
                nombre = "PIEZA DISFRUTA YOPLAIT BATIDO 442G",
                grupo = "Lácteos",
                unidad = "Pieza",
                stock = 0,
                stockMin = 5,
                precioCompra = 19.50,
                precioVenta = 28.50,
                descripcion = "Yoghurt batido con trozos de fruta 442g"
            ),
            Product(
                codigo = "220",
                nombre = "PIEZA YOGURTH BEBIBLE",
                grupo = "Lácteos",
                unidad = "Pieza",
                stock = -223,
                stockMin = 20,
                precioCompra = 9.20,
                precioVenta = 13.50,
                descripcion = "Yoghurt bebible surtido 250ml"
            ),
            Product(
                codigo = "12345",
                nombre = "PIEZA YOGURTH CEREALERO",
                grupo = "Lácteos",
                unidad = "Pieza",
                stock = 1,
                stockMin = 5,
                precioCompra = 9.50,
                precioVenta = 13.75,
                descripcion = "Yoghurt con cereal Crunch / Choco Krispis"
            ),
            Product(
                codigo = "POZ-MICH",
                nombre = "PIEZA POZOLERO MICHOACANO",
                grupo = "Abarrotes",
                unidad = "Pieza",
                stock = 1,
                stockMin = 5,
                precioCompra = 18.00,
                precioVenta = 27.00,
                descripcion = "Maíz pozolero precocido michoacano 1kg"
            ),
            Product(
                codigo = "0283",
                nombre = "CAJA BONICE",
                grupo = "Congelados",
                unidad = "Caja",
                stock = 94,
                stockMin = 15,
                precioCompra = 24.00,
                precioVenta = 34.00,
                descripcion = "Caja Bonice Icessote surtido x20 unidades"
            ),
            Product(
                codigo = "8236",
                nombre = "PIEZA ALPURA DESLACTOSADA",
                grupo = "Lácteos",
                unidad = "Litro",
                stock = -48,
                stockMin = 12,
                precioCompra = 18.00,
                precioVenta = 26.00,
                descripcion = "Leche Alpura Deslactosada 1L"
            ),
            Product(
                codigo = "15010407",
                nombre = "PIEZA BARRA JAMON FUD 3.5KG",
                grupo = "Embutidos",
                unidad = "Barra",
                stock = 2,
                stockMin = 2,
                precioCompra = 280.00,
                precioVenta = 385.00,
                descripcion = "Jamon de Pavo y Cerdo Fud Barra 3.5kg"
            ),
            Product(
                codigo = "400",
                nombre = "PIEZA CHIMEX SALCHICHA",
                grupo = "Embutidos",
                unidad = "Paquete",
                stock = 5,
                stockMin = 8,
                precioCompra = 19.00,
                precioVenta = 27.00,
                descripcion = "Salchicha Viena Chimex 500g"
            ),
            Product(
                codigo = "0000",
                nombre = "PIEZA CN HUEVO",
                grupo = "Abarrotes",
                unidad = "Cono",
                stock = 0,
                stockMin = 10,
                precioCompra = 65.00,
                precioVenta = 85.00,
                descripcion = "Cono de huevo fresco seleccionado x30 unidades"
            ),
            Product(
                codigo = "BIM-140G",
                nombre = "Donita Espolvoreada 8p 140g FLOW BIM",
                grupo = "Panadería",
                unidad = "Paquete",
                stock = 120,
                stockMin = 25,
                precioCompra = 16.50,
                precioVenta = 24.00,
                descripcion = "Donitas Bimbo azucaradas 8 piezas"
            ),
            Product(
                codigo = "BIM-TOST",
                nombre = "Tostadas Clásicas Bimbo 210g",
                grupo = "Panadería",
                unidad = "Paquete",
                stock = 85,
                stockMin = 15,
                precioCompra = 18.00,
                precioVenta = 26.50,
                descripcion = "Tostadas horneadas de maíz y trigo"
            ),
            Product(
                codigo = "ACE-1L",
                nombre = "Aceite Vegetal DANIISA 1L (Caja x12)",
                grupo = "Abarrotes",
                unidad = "Caja",
                stock = 45,
                stockMin = 10,
                precioCompra = 38.00,
                precioVenta = 48.50,
                descripcion = "Aceite puro 100% vegetal comestible"
            )
        )
    }

    private fun getInitial24Reports(): List<ReportItem> {
        return listOf(
            // Row 1
            ReportItem(1, "Transacciones por Día", "Transacciones", true, "Verde", "Listado consolidado de ventas y operaciones del día"),
            ReportItem(2, "Total de Transacciones", "Transacciones", true, "Verde", "Resumen acumulado de transacciones comerciales"),
            ReportItem(3, "Ventas por Cobrar", "Finanzas", false, "Amarillo", "Cuentas por cobrar a clientes a crédito (❌ Deshabilitado temporalmente)"),
            
            // Row 2
            ReportItem(4, "Compras por Pagar", "Finanzas", false, "Amarillo", "Facturas y órdenes de compra pendientes de pago"),
            ReportItem(5, "Ingresos Extra por Cobrar", "Finanzas", false, "Amarillo", "Otros ingresos pendientes"),
            ReportItem(6, "Gastos Extras por Pagar", "Finanzas", true, "Amarillo", "Gastos operativos y viáticos por liquidar"),
            
            // Row 3
            ReportItem(7, "Formas de Pago", "Finanzas", true, "Verde", "Desglose por Efectivo, Tarjeta, Transferencia y Crédito"),
            ReportItem(8, "Reporte Merma", "Inventario", false, "Verde", "Registro de productos dañados o vencidos"),
            ReportItem(9, "Transacciones por Producto", "Productos", true, "Verde", "Histórico de movimientos agrupado por producto"),
            
            // Row 4
            ReportItem(10, "Transacciones por Servicio", "Transacciones", true, "Verde", "Servicios de flete y entrega a domicilio"),
            ReportItem(11, "Top Productos", "Productos", true, "Verde", "Ranking de productos con mayor rotación e ingresos"),
            ReportItem(12, "Productos a Detalle", "Productos", true, "Verde", "Ficha técnica completa de precios, costos y márgenes"),
            
            // Row 5
            ReportItem(13, "Reporte Categoría", "Productos", true, "Verde", "Ventas totales y stock categorizado por familias"),
            ReportItem(14, "Reporte Inventario", "Inventario", true, "Verde", "Valoración de inventario al costo y precio de venta"),
            ReportItem(15, "Reporte Etiquetas", "Ventas", false, "Verde", "Ventas segmentadas por ruta y etiqueta comercial"),
            
            // Row 6
            ReportItem(16, "Reporte Empleados", "Finanzas", true, "Verde", "Rendimiento y comisiones por agente de venta"),
            ReportItem(17, "Reporte Cotización", "Ventas", false, "Verde", "Presupuestos y cotizaciones emitidas a clientes"),
            ReportItem(18, "Reporte Orden Compra", "Inventario", false, "Verde", "Órdenes de reabastecimiento a proveedores"),
            
            // Row 7
            ReportItem(19, "Venta por Día", "Ventas", true, "Naranja", "Gráfica comparativa de ventas de los últimos 7 días"),
            ReportItem(20, "Análisis ABC", "Inventario", false, "Naranja", "Clasificación de productos por volumen de facturación"),
            ReportItem(21, "Registro Compras", "Inventario", false, "Naranja", "Bitácora histórica de compras a proveedores"),
            
            // Row 8
            ReportItem(22, "Ingresos y Gastos Extra", "Finanzas", true, "Naranja", "Balance operativo de caja chica y gastos varios"),
            ReportItem(23, "Reporte Clientes", "Clientes", true, "Azul", "Directorio y récord de consumo por cliente"),
            ReportItem(24, "Top Clientes", "Clientes", true, "Azul", "Clientes con mayor volumen de compra en el periodo")
        )
    }

    private fun getInitialSalesHistory(products: List<Product>): List<SaleRecord> {
        val p1 = products.find { it.codigo == "7501040091230" } ?: products[0]
        val p2 = products.find { it.codigo == "BIM-140G" } ?: products[1]
        val p3 = products.find { it.codigo == "0283" } ?: products[2]

        return listOf(
            SaleRecord(
                id = "T-1082",
                fecha = "Hoy, 10:45 AM",
                tipoDoc = "TICKET",
                estadoPago = "Pagado",
                cliente = "Abarrotes Don Lucho",
                clienteDoc = "9512494964",
                formaPago = "Efectivo",
                etiqueta = "Ruta Norte",
                notas = "Entregar en puerta principal",
                items = listOf(SaleItem(p1, cantidad = 5.0), SaleItem(p2, cantidad = 10.0)),
                subtotal = 310.00,
                impuesto = 0.00,
                total = 310.00,
                empleado = "Oliverth"
            ),
            SaleRecord(
                id = "T-1081",
                fecha = "Hoy, 09:15 AM",
                tipoDoc = "TICKET",
                estadoPago = "Pagado",
                cliente = "Supermercado San José",
                clienteDoc = "RFC: ZUCE931118BT4",
                formaPago = "Transferencia",
                etiqueta = "Ruta Centro",
                notas = "Factura electrónica solicitada",
                items = listOf(SaleItem(p3, cantidad = 6.0), SaleItem(p1, cantidad = 12.0)),
                subtotal = 372.00,
                impuesto = 0.00,
                total = 372.00,
                empleado = "Oliverth"
            ),
            SaleRecord(
                id = "T-1080",
                fecha = "Ayer, 04:30 PM",
                tipoDoc = "TICKET",
                estadoPago = "Por Cobrar",
                cliente = "Minimarket Los Pinos",
                clienteDoc = "9511223344",
                formaPago = "Crédito 15 días",
                etiqueta = "Ruta Sur",
                notas = "Cobrar el próximo viernes",
                items = listOf(SaleItem(p2, cantidad = 15.0)),
                subtotal = 360.00,
                impuesto = 0.00,
                total = 360.00,
                empleado = "Amiel"
            )
        )
    }

    fun inicializarApp() {
        val savedUrl = tinyDb.getUrlBase()
        val savedToken = tinyDb.getToken()
        val savedUsuario = tinyDb.getUsuario()
        val savedNombre = tinyDb.getNombre()
        val savedRol = tinyDb.getRol()

        _uiState.update {
            it.copy(
                urlBase = savedUrl,
                token = savedToken,
                usuario = savedUsuario,
                nombre = savedNombre,
                rol = savedRol,
                txtUsuario = savedUsuario
            )
        }
    }

    // Navigation
    fun navigateTo(screen: String) {
        _uiState.update { it.copy(currentScreen = screen) }
    }

    // Login Actions
    fun onUsuarioChange(u: String) { _uiState.update { it.copy(txtUsuario = u, lblMensajeVisible = false) } }
    fun onPasswordChange(p: String) { _uiState.update { it.copy(txtPassword = p, lblMensajeVisible = false) } }

    fun onBtnEntrarClick() {
        val state = _uiState.value
        if (state.txtUsuario.trim().isEmpty()) {
            _uiState.update { it.copy(lblMensajeTexto = "Escribe tu usuario", lblMensajeVisible = true) }
            return
        }
        if (state.txtPassword.trim().isEmpty()) {
            _uiState.update { it.copy(lblMensajeTexto = "Escribe tu contraseña", lblMensajeVisible = true) }
            return
        }

        _uiState.update { it.copy(isConnecting = true, tipoPeticion = "login", lblMensajeVisible = false) }

        viewModelScope.launch {
            val result = webClient.sendLoginRequest(state.urlBase, state.txtUsuario.trim(), state.txtPassword.trim())
            handleWebResult(result)
        }
    }

    private fun handleWebResult(result: WebResult) {
        _uiState.update { it.copy(isConnecting = false, isInitialChecking = false) }
        when (result) {
            is WebResult.Error -> {
                _uiState.update { it.copy(lblMensajeTexto = "Sin conexión. Modo offline activo.", lblMensajeVisible = true) }
            }
            is WebResult.Success -> {
                val resp = result.response
                if (resp.ok && resp.data != null) {
                    val data = resp.data
                    val newToken = data.token ?: tinyDb.getToken()
                    val newUsuario = data.usuario ?: "oliverth"
                    val newNombre = data.nombre ?: "Oliverth"
                    val newRol = data.rol ?: "admin"

                    tinyDb.setToken(newToken)
                    tinyDb.setUsuario(newUsuario)
                    tinyDb.setNombre(newNombre)
                    tinyDb.setRol(newRol)

                    _uiState.update {
                        it.copy(
                            token = newToken,
                            usuario = newUsuario,
                            nombre = newNombre,
                            rol = newRol,
                            notifAlerta = "¡Bienvenido $newNombre!",
                            currentScreen = "Dashboard",
                            lblMensajeVisible = false
                        )
                    }
                } else {
                    _uiState.update { it.copy(lblMensajeTexto = resp.msg ?: "Usuario o contraseña inválidos", lblMensajeVisible = true) }
                }
            }
        }
    }

    // Role switching for demoing
    fun setRole(newRole: String) {
        tinyDb.setRol(newRole)
        _uiState.update { it.copy(rol = newRole, notifAlerta = "Rol cambiado a: $newRole") }
    }

    // Inventory & Products Actions
    fun setInventorySearchQuery(q: String) {
        _uiState.update { it.copy(inventorySearchQuery = q) }
    }

    fun setInventoryFilterTab(tab: String) {
        _uiState.update { it.copy(inventoryFilterTab = tab) }
    }

    fun addOrUpdateProduct(product: Product) {
        _uiState.update { current ->
            val list = current.products.toMutableList()
            val index = list.indexOfFirst { it.codigo == product.codigo }
            if (index >= 0) {
                list[index] = product
            } else {
                list.add(0, product)
            }
            current.copy(products = list, notifAlerta = "Producto guardado: ${product.nombre}")
        }
    }

    fun deleteProduct(codigo: String) {
        _uiState.update { current ->
            val list = current.products.filterNot { it.codigo == codigo }
            current.copy(products = list, notifAlerta = "Producto eliminado")
        }
    }

    // Ventas / Multi-Item Cart POS Actions
    fun setSalesSearchQuery(q: String) {
        _uiState.update { it.copy(salesSearchQuery = q) }
    }

    fun addProductToCart(product: Product) {
        _uiState.update { current ->
            val cart = current.currentCart.toMutableList()
            val existingIndex = cart.indexOfFirst { it.product.codigo == product.codigo }
            if (existingIndex >= 0) {
                val currentItem = cart[existingIndex]
                cart[existingIndex] = currentItem.copy(cantidad = currentItem.cantidad + 1.0)
            } else {
                cart.add(SaleItem(product = product, cantidad = 1.0, precioVenta = product.precioVenta))
            }
            current.copy(currentCart = cart, notifAlerta = "Agregado al carrito: ${product.nombre}")
        }
    }

    fun updateCartItemQuantity(index: Int, qty: Double) {
        _uiState.update { current ->
            if (index !in current.currentCart.indices) return@update current
            val cart = current.currentCart.toMutableList()
            cart[index] = cart[index].copy(cantidad = maxOf(0.1, qty))
            current.copy(currentCart = cart)
        }
    }

    fun updateCartItemDelta(index: Int, delta: Double) {
        _uiState.update { current ->
            if (index !in current.currentCart.indices) return@update current
            val cart = current.currentCart.toMutableList()
            val newQty = maxOf(0.1, cart[index].cantidad + delta)
            cart[index] = cart[index].copy(cantidad = newQty)
            current.copy(currentCart = cart)
        }
    }

    fun updateCartItemPrice(index: Int, price: Double) {
        _uiState.update { current ->
            if (index !in current.currentCart.indices) return@update current
            val cart = current.currentCart.toMutableList()
            cart[index] = cart[index].copy(precioVenta = maxOf(0.0, price))
            current.copy(currentCart = cart)
        }
    }

    fun updateCartItemDevolucion(index: Int, devol: Double) {
        _uiState.update { current ->
            if (index !in current.currentCart.indices) return@update current
            val cart = current.currentCart.toMutableList()
            cart[index] = cart[index].copy(devolucion = maxOf(0.0, devol))
            current.copy(currentCart = cart)
        }
    }

    fun updateCartItemDiscount(index: Int, disc: Double) {
        _uiState.update { current ->
            if (index !in current.currentCart.indices) return@update current
            val cart = current.currentCart.toMutableList()
            cart[index] = cart[index].copy(descuentoPorc = disc.coerceIn(0.0, 100.0))
            current.copy(currentCart = cart)
        }
    }

    fun removeCartItem(index: Int) {
        _uiState.update { current ->
            if (index !in current.currentCart.indices) return@update current
            val cart = current.currentCart.toMutableList()
            val removed = cart.removeAt(index)
            current.copy(currentCart = cart, notifAlerta = "Eliminado: ${removed.product.nombre}")
        }
    }

    fun clearCart() {
        _uiState.update {
            it.copy(
                currentCart = emptyList(),
                saleCliente = "",
                saleNotas = "",
                notifAlerta = "Venta cancelada / Carrito limpio"
            )
        }
    }

    fun startSaleWithProduct(product: Product) {
        addProductToCart(product)
        _uiState.update { it.copy(currentScreen = "Ventas") }
    }

    fun updateSaleQuantity(delta: Double) {
        if (_uiState.value.currentCart.isNotEmpty()) {
            updateCartItemDelta(0, delta)
        }
    }

    fun setSaleQuantity(qty: Double) {
        if (_uiState.value.currentCart.isNotEmpty()) {
            updateCartItemQuantity(0, qty)
        }
    }

    fun setSaleDevolucion(devol: Double) {
        if (_uiState.value.currentCart.isNotEmpty()) {
            updateCartItemDevolucion(0, devol)
        }
    }

    fun setSaleDescuentoPorc(desc: Double) {
        if (_uiState.value.currentCart.isNotEmpty()) {
            updateCartItemDiscount(0, desc)
        }
    }

    fun setSaleEstadoPago(estado: String) {
        _uiState.update { it.copy(saleEstadoPago = estado) }
    }

    fun setSaleCliente(cliente: String) {
        _uiState.update { it.copy(saleCliente = cliente) }
    }

    fun setSaleFormaPago(forma: String) {
        _uiState.update { it.copy(saleFormaPago = forma) }
    }

    fun setSaleEtiqueta(etiqueta: String) {
        _uiState.update { it.copy(saleEtiqueta = etiqueta) }
    }

    fun setSaleNotas(notas: String) {
        _uiState.update { it.copy(saleNotas = notas) }
    }

    fun finalizeSale() {
        val state = _uiState.value
        val items = if (state.currentCart.isNotEmpty()) state.currentCart else if (state.selectedSaleItem != null) listOf(state.selectedSaleItem) else return
        if (items.isEmpty()) return

        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        val fechaStr = sdf.format(Date())
        val nextDocId = "T-" + (1083 + state.salesHistory.size)
        val totalAmount = items.sumOf { it.subtotal }

        val sale = SaleRecord(
            id = nextDocId,
            fecha = fechaStr,
            tipoDoc = state.businessConfig.formatoTicket.uppercase(),
            estadoPago = state.saleEstadoPago,
            cliente = if (state.saleCliente.isNotBlank()) state.saleCliente else "CLIENTE GENERAL",
            clienteDoc = state.saleClienteDoc,
            formaPago = state.saleFormaPago,
            etiqueta = state.saleEtiqueta,
            notas = state.saleNotas,
            items = items,
            subtotal = totalAmount,
            impuesto = 0.0,
            total = totalAmount,
            empleado = state.nombre
        )

        // Decrement product stock in local inventory for each item sold
        val updatedProducts = state.products.map { prod ->
            val soldItem = items.find { it.product.codigo == prod.codigo }
            if (soldItem != null) {
                prod.copy(stock = (prod.stock - soldItem.cantidad.toInt()))
            } else prod
        }

        val updatedHistory = listOf(sale) + state.salesHistory
        val newVentaDia = state.ventaDiaMonto + totalAmount

        _uiState.update {
            it.copy(
                products = updatedProducts,
                salesHistory = updatedHistory,
                ventaDiaMonto = newVentaDia,
                currentCart = emptyList(),
                selectedSaleItem = null,
                viewingTicket = sale,
                currentScreen = "Ventas",
                notifAlerta = "Venta $nextDocId registrada con éxito ($ ${String.format(Locale.US, "%.2f", totalAmount)})"
            )
        }
    }

    fun viewTicket(sale: SaleRecord) {
        _uiState.update { it.copy(viewingTicket = sale) }
    }

    fun dismissTicket() {
        _uiState.update { it.copy(viewingTicket = null) }
    }

    // Reports Actions
    fun toggleIncludeReportCharts(inc: Boolean) {
        _uiState.update { it.copy(includeReportCharts = inc) }
    }

    fun selectReport(report: ReportItem) {
        _uiState.update { it.copy(selectedReport = report) }
    }

    fun dismissReportDetail() {
        _uiState.update { it.copy(selectedReport = null) }
    }

    // Business Configuration Actions
    fun updateBusinessConfig(cfg: BusinessConfig) {
        tinyDb.saveBusinessConfig(cfg)
        _uiState.update {
            it.copy(
                businessConfig = cfg,
                urlBase = cfg.serverUrl,
                token = cfg.apiToken,
                notifAlerta = "Configuración guardada correctamente"
            )
        }
    }

    // Barcode Scanner
    fun openBarcodeScanner() {
        _uiState.update { it.copy(isScannerActive = true) }
    }

    fun closeBarcodeScanner() {
        _uiState.update { it.copy(isScannerActive = false) }
    }

    fun onBarcodeScanned(code: String) {
        val clean = code.trim()
        val foundProduct = _uiState.value.products.find { it.codigo.equals(clean, ignoreCase = true) }
        _uiState.update { it.copy(isScannerActive = false, lastScannedCode = clean) }

        if (foundProduct != null) {
            startSaleWithProduct(foundProduct)
        } else {
            _uiState.update {
                it.copy(
                    salesSearchQuery = clean,
                    inventorySearchQuery = clean,
                    notifAlerta = "Código escaneado: $clean"
                )
            }
        }
    }

    fun dismissNotification() {
        _uiState.update { it.copy(notifAlerta = null) }
    }

    fun openTerminarDiaDialog() {
        _uiState.update { it.copy(showTerminarDiaDialog = true, terminarDiaSuccessMsg = null) }
    }

    fun closeTerminarDiaDialog() {
        _uiState.update { it.copy(showTerminarDiaDialog = false, isSyncingDay = false, terminarDiaSuccessMsg = null) }
    }

    fun ejecutarTerminarDia(cerrarSesionDespues: Boolean) {
        val state = _uiState.value
        _uiState.update { it.copy(isSyncingDay = true) }

        viewModelScope.launch {
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            val fechaStr = sdf.format(Date())

            val totalDevoluciones = state.currentCart.sumOf { it.devolucion } + state.devolucionesMonto
            val result = webClient.sendTerminarDiaRequest(
                url = state.urlBase,
                usuario = state.usuario,
                token = state.token,
                fecha = fechaStr,
                totalVentas = state.ventaDiaMonto,
                totalDevoluciones = totalDevoluciones,
                cantidadTransacciones = state.salesHistory.size
            )

            val successMsg = "✅ ¡Día finalizado con éxito! Datos sincronizados con tu Google Apps Script (${state.salesHistory.size} transacciones, Venta Total: $ ${String.format(Locale.US, "%.2f", state.ventaDiaMonto)})"

            _uiState.update {
                it.copy(
                    isSyncingDay = false,
                    terminarDiaSuccessMsg = successMsg,
                    notifAlerta = "Sincronización de día completada"
                )
            }

            if (cerrarSesionDespues) {
                kotlinx.coroutines.delay(1500)
                cerrarSesion()
            }
        }
    }

    fun cerrarSesion() {
        tinyDb.borrarTodo()
        _uiState.update {
            it.copy(
                token = "",
                usuario = "",
                nombre = "",
                currentScreen = "Login",
                showTerminarDiaDialog = false,
                notifAlerta = "Sesión cerrada"
            )
        }
    }
}
