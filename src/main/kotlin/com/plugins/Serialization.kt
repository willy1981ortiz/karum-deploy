package com.plugins

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import com.helper.formatDateForMysql
import com.helper.formatDateTimeForMysql
import com.helper.toLocalDate
import com.helper.toLocalDateTime
import io.ktor.serialization.gson.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import java.time.LocalDate
import java.time.LocalDateTime

fun Application.configureSerialization() {
    install(ContentNegotiation) {
        gson {
            setPrettyPrinting()
            registerTypeAdapter(LocalDate::class.java, LocalDateAdapter())
            registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeAdapter())
        }
    }

}

class LocalDateAdapter : TypeAdapter<LocalDate>() {
    override fun write(out: JsonWriter?, value: LocalDate?) {
        out?.value(value?.formatDateForMysql())
    }

    override fun read(`in`: JsonReader?): LocalDate? {
        return `in`?.nextString()?.takeIf { it.isNotBlank() }?.toLocalDate()
    }
}


class LocalDateTimeAdapter : TypeAdapter<LocalDateTime>() {
    override fun write(out: JsonWriter?, value: LocalDateTime?) {
        out?.value(value?.formatDateTimeForMysql())
    }

    override fun read(`in`: JsonReader?): LocalDateTime? {
        return `in`?.nextString()?.takeIf { it.isNotBlank() }?.toLocalDateTime()
    }
}
