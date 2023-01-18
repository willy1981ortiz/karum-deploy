package com.helper

import com.Env
import com.ErrorResponseException
import com.tables.pojos.Tc41
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

suspend fun Tc41.formatDateForInput(): String {
    return "$year-$month-$day"
}

suspend fun ApplicationCall.receiveSafeParameters(): Parameters {
    try {
        return this.receiveParameters()
    } catch (e: Exception) {
    }
    return Parameters.Empty
}

fun List<PartData>.getValueOrThrow(key: String, errorMessage: String = "invalid parameter: $key") =
    (this.find { it.name == key && it is PartData.FormItem } as? PartData.FormItem)?.value
        ?: throw ErrorResponseException(errorMessage)

fun List<PartData>.getValueOrNull(key: String) =
    (this.find { it.name == key && it is PartData.FormItem } as? PartData.FormItem)?.value

fun List<PartData>.getFileOrNull(key: String) =
    this.find { it.name == key && it is PartData.FileItem } as? PartData.FileItem

fun List<PartData>.getFileOrThrow(key: String, errorMessage: String = "invalid parameter: $key") =
    this.find { it.name == key && it is PartData.FileItem } as? PartData.FileItem
        ?: throw ErrorResponseException(errorMessage)

fun LocalDate?.formatDateForMysql(): String? {
    return this?.let { DateTimeFormatter.ofPattern("yyyy-MM-dd").format(it) }
}

fun LocalDateTime?.formatDateTimeForMysql(): String? {
    return this?.let { DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(it) }
}

fun String.toLocalDate() = LocalDate.parse(this, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
    ?: throw error("check your date format 'yyyy-MM-dd'")

fun String.toLocalDateTime() = LocalDateTime.parse(this, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
    ?: throw error("check your date format 'yyyy-MM-dd HH:mm:ss'")

fun String?.toLocalDateOrNull(): LocalDate? {
    return try {
        LocalDate.parse(this, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
    } catch (e: Exception) {
        null
    }
}

fun Long.milliToLocalDate(): String {
    val result = Date(this)
    return SimpleDateFormat("yyyy-MM-dd").format(result)
}

private fun getFormatTimeFromMilli(milliSec: Long): String {
    // Creating date format
    val formatter: DateFormat = SimpleDateFormat("yyyy-MM-dd")
    val result = Date(milliSec)
    // given format
    println(formatter.format(result))
    return formatter.format(result)
}

class CircularIterator<T>(vararg t: T) {
    val vehicles: MutableList<T> = mutableListOf()
    var index = 0

    init {
        vehicles.addAll(t)
    }

    fun next(): T {
        return vehicles[index++ % vehicles.size]
    }

    fun add(t: T) {
        vehicles.add(t)
    }
}

fun Double.round(decimals: Int = 2): Double = "%.${decimals}f".format(this).toDouble()

fun String.withBaseUrl(): String {
    return if (this.startsWith("/")) {
        "${Env.BASE_URL}$this"
    } else {
        this
    }
}

fun String.isValidUrl(): Boolean {
    return this.length > 6 && (this.startsWith("http://", ignoreCase = true) || this.startsWith(
        "https://",
        ignoreCase = true
    )) && this.contains(".")
}


fun Tc41.isValid(): Boolean {
    return zipCode.isNullOrBlank().not() && name.isNullOrBlank().not() && parentSurname.isNullOrBlank()
        .not() && motherSurname.isNullOrBlank().not() && curpNo.isNullOrBlank().not() && ine.isNullOrBlank().not()
}