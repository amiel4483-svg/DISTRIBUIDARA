package com.example.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LoginRequest(
    @Json(name = "action") val action: String = "login",
    @Json(name = "usuario") val usuario: String,
    @Json(name = "password") val password: String
)

@JsonClass(generateAdapter = true)
data class VerifyTokenRequest(
    @Json(name = "action") val action: String = "verificarToken",
    @Json(name = "token") val token: String
)

@JsonClass(generateAdapter = true)
data class UserData(
    @Json(name = "token") val token: String? = null,
    @Json(name = "usuario") val usuario: String? = null,
    @Json(name = "nombre") val nombre: String? = null,
    @Json(name = "rol") val rol: String? = null
)

@JsonClass(generateAdapter = true)
data class ApiResponse(
    @Json(name = "ok") val ok: Boolean = false,
    @Json(name = "data") val data: UserData? = null,
    @Json(name = "msg") val msg: String? = null
)

/**
 * Product Item for Inventory and Sales
 */
data class Product(
    val codigo: String,
    val nombre: String,
    val grupo: String = "General",
    val unidad: String = "Piezas",
    val stock: Int = 0,
    val stockMin: Int = 5,
    val precioCompra: Double = 0.0,
    val precioVenta: Double = 0.0,
    val imagenUrl: String = "",
    val descripcion: String = "",
    val esReserva: Boolean = false,
    val esMerma: Boolean = false
) {
    val tieneStockOk: Boolean get() = stock > 0
}

/**
 * Item in Cart / Detailed Sale
 */
data class SaleItem(
    val product: Product,
    var cantidad: Double = 1.0,
    var precioVenta: Double = product.precioVenta,
    var devolucion: Double = 0.0,
    var descuentoPorc: Double = 0.0
) {
    val subtotal: Double
        get() {
            val base = (cantidad * precioVenta) - devolucion
            val desc = base * (descuentoPorc / 100.0)
            return maxOf(0.0, base - desc)
        }
}

/**
 * Completed Sale Record
 */
data class SaleRecord(
    val id: String,
    val fecha: String,
    val tipoDoc: String = "TICKET",
    val estadoPago: String = "Pagado", // "Pagado" or "Por Cobrar"
    val cliente: String = "CLIENTE GENERAL",
    val clienteDoc: String = "",
    val formaPago: String = "Efectivo",
    val etiqueta: String = "Ruta Principal",
    val notas: String = "",
    val items: List<SaleItem>,
    val subtotal: Double,
    val impuesto: Double,
    val total: Double,
    val empleado: String = "Oliverth",
    val sincronizado: Boolean = true
)

/**
 * Business & Ticket Configuration
 */
data class BusinessConfig(
    val nombreNegocio: String = "DISTRIBUIDORA DANIISA",
    val logoUri: String = "",
    val nombreContacto: String = "Oliverth",
    val email: String = "eddyzun18@gmail.com",
    val telefono: String = "9512494964",
    val rfc: String = "ZUCE931118BT4",
    
    // Receipt / Ticket settings
    val disenoTicket: String = "Simple", // "Simple" or "Extra"
    val agregarStatusVenta: Boolean = true,
    val formatoTicket: String = "Ticket", // "Normal" (A4/Carta) or "Ticket" (80mm/58mm)
    val agregarLogoTicket: Boolean = true,
    val agregarInfoCliente: Boolean = false,
    
    // Server endpoint & security token
    val serverUrl: String = "https://script.google.com/macros/s/AKfycbwym3JudLPcKDuZeFWvkntaCTDieMLhrKPFFG-SBaGcevEbv4lA5yN0vMX2wPRe73ko/exec",
    val apiToken: String = "321174e7-0fe2-47f4-8a7f-86191cb8ea2a",
    val modoOffline: Boolean = true
)

/**
 * 24 Reports Definitions
 */
data class ReportItem(
    val id: Int,
    val titulo: String,
    val categoria: String, // "Transacciones", "Finanzas", "Productos", "Inventario", "Clientes"
    val habilitado: Boolean,
    val colorTipo: String, // "Verde", "Amarillo", "Naranja", "Azul"
    val descripcion: String = ""
)
