/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package com.icerockdev.service.sms.driver.sms4b

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.module.kotlin.kotlinModule
import io.ktor.client.HttpClient
import io.ktor.client.engine.apache.Apache

data class Sms4bConfig(
    val client: HttpClient = HttpClient(Apache),
    val mapper: ObjectMapper = XmlMapper.builder()
        .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true)
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        .addModule(JacksonXmlModule().apply {
            setDefaultUseWrapper(false)
        })
        .addModule(kotlinModule())
        .build(),
    val url: String = "https://sms4b.ru/ws/sms.asmx/SendSMS",
    val login: String,
    val password: String,
    val source: String
)
