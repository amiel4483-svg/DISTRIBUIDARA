package com.example.data.local

import android.content.Context
import android.content.SharedPreferences
import com.example.data.model.BusinessConfig
import com.example.data.model.Product
import com.example.data.model.SaleItem
import com.example.data.model.SaleRecord
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

/**
 * Enhanced TinyDB storage storing persistent business configuration,
 * offline products, and sales.
 */
class TinyDbStorage(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences(
        "TinyDB1_DANIISA_ERP",
        Context.MODE_PRIVATE
    )

    private val moshi: Moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()

    fun guardarValor(etiqueta: String, valor: String) {
        prefs.edit().putString(etiqueta, valor).apply()
    }

    fun obtenerValor(etiqueta: String, valorSiNoExiste: String = ""): String {
        return prefs.getString(etiqueta, valorSiNoExiste) ?: valorSiNoExiste
    }

    fun guardarBoolean(etiqueta: String, valor: Boolean) {
        prefs.edit().putBoolean(etiqueta, valor).apply()
    }

    fun obtenerBoolean(etiqueta: String, valorSiNoExiste: Boolean = false): Boolean {
        return prefs.getBoolean(etiqueta, valorSiNoExiste)
    }

    fun borrarTodo() {
        val currentUrl = getUrlBase()
        val currentToken = getToken()
        val businessName = getNombreNegocio()
        prefs.edit().clear().apply()
        setUrlBase(currentUrl)
        setToken(currentToken)
        setNombreNegocio(businessName)
    }

    // Session variables
    fun getToken(): String = obtenerValor("token", "321174e7-0fe2-47f4-8a7f-86191cb8ea2a")
    fun getUsuario(): String = obtenerValor("usuario", "oliverth")
    fun getNombre(): String = obtenerValor("nombre", "Oliverth")
    fun getRol(): String = obtenerValor("rol", "admin") // admin, vendedor, bodeguero
    fun getUrlBase(): String = obtenerValor("URL_BASE", DEFAULT_URL_BASE)

    fun setToken(value: String) = guardarValor("token", value)
    fun setUsuario(value: String) = guardarValor("usuario", value)
    fun setNombre(value: String) = guardarValor("nombre", value)
    fun setRol(value: String) = guardarValor("rol", value)
    fun setUrlBase(value: String) = guardarValor("URL_BASE", value)

    // Business Config Persistence
    fun getNombreNegocio(): String = obtenerValor("cfg_nombre_negocio", "DISTRIBUIDORA DANIISA")
    fun setNombreNegocio(value: String) = guardarValor("cfg_nombre_negocio", value)

    fun getLogoUri(): String = obtenerValor("cfg_logo_uri", "")
    fun setLogoUri(value: String) = guardarValor("cfg_logo_uri", value)

    fun getNombreContacto(): String = obtenerValor("cfg_contacto", "Oliverth")
    fun setNombreContacto(value: String) = guardarValor("cfg_contacto", value)

    fun getEmail(): String = obtenerValor("cfg_email", "eddyzun18@gmail.com")
    fun setEmail(value: String) = guardarValor("cfg_email", value)

    fun getTelefono(): String = obtenerValor("cfg_telefono", "9512494964")
    fun setTelefono(value: String) = guardarValor("cfg_telefono", value)

    fun getRfc(): String = obtenerValor("cfg_rfc", "ZUCE931118BT4")
    fun setRfc(value: String) = guardarValor("cfg_rfc", value)

    fun getDisenoTicket(): String = obtenerValor("cfg_diseno_ticket", "Simple")
    fun setDisenoTicket(value: String) = guardarValor("cfg_diseno_ticket", value)

    fun getAgregarStatusVenta(): Boolean = obtenerBoolean("cfg_status_venta", true)
    fun setAgregarStatusVenta(value: Boolean) = guardarBoolean("cfg_status_venta", value)

    fun getFormatoTicket(): String = obtenerValor("cfg_formato_ticket", "Ticket")
    fun setFormatoTicket(value: String) = guardarValor("cfg_formato_ticket", value)

    fun getAgregarLogoTicket(): Boolean = obtenerBoolean("cfg_logo_ticket", true)
    fun setAgregarLogoTicket(value: Boolean) = guardarBoolean("cfg_logo_ticket", value)

    fun getAgregarInfoCliente(): Boolean = obtenerBoolean("cfg_info_cliente", false)
    fun setAgregarInfoCliente(value: Boolean) = guardarBoolean("cfg_info_cliente", value)

    fun loadBusinessConfig(): BusinessConfig {
        return BusinessConfig(
            nombreNegocio = getNombreNegocio(),
            logoUri = getLogoUri(),
            nombreContacto = getNombreContacto(),
            email = getEmail(),
            telefono = getTelefono(),
            rfc = getRfc(),
            disenoTicket = getDisenoTicket(),
            agregarStatusVenta = getAgregarStatusVenta(),
            formatoTicket = getFormatoTicket(),
            agregarLogoTicket = getAgregarLogoTicket(),
            agregarInfoCliente = getAgregarInfoCliente(),
            serverUrl = getUrlBase(),
            apiToken = getToken()
        )
    }

    fun saveBusinessConfig(cfg: BusinessConfig) {
        setNombreNegocio(cfg.nombreNegocio)
        setLogoUri(cfg.logoUri)
        setNombreContacto(cfg.nombreContacto)
        setEmail(cfg.email)
        setTelefono(cfg.telefono)
        setRfc(cfg.rfc)
        setDisenoTicket(cfg.disenoTicket)
        setAgregarStatusVenta(cfg.agregarStatusVenta)
        setFormatoTicket(cfg.formatoTicket)
        setAgregarLogoTicket(cfg.agregarLogoTicket)
        setAgregarInfoCliente(cfg.agregarInfoCliente)
        setUrlBase(cfg.serverUrl)
        setToken(cfg.apiToken)
    }

    companion object {
        const val DEFAULT_URL_BASE = "https://script.google.com/macros/s/AKfycbwym3JudLPcKDuZeFWvkntaCTDieMLhrKPFFG-SBaGcevEbv4lA5yN0vMX2wPRe73ko/exec"
        const val DEFAULT_TOKEN = "321174e7-0fe2-47f4-8a7f-86191cb8ea2a"
    }
}
