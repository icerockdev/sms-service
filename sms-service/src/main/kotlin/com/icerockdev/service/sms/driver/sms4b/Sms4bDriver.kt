/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package com.icerockdev.service.sms.driver.sms4b

import com.fasterxml.jackson.core.type.TypeReference
import com.icerockdev.service.sms.SmsException
import com.icerockdev.service.sms.driver.ISmsDriver
import io.ktor.client.request.forms.FormDataContent
import io.ktor.client.request.post
import io.ktor.http.Parameters


class Sms4bDriver(private val config: Sms4bConfig) : ISmsDriver {

    @Throws(SmsException::class)
    override suspend fun send(to: String, text: String): Boolean {
        val payload: Parameters = Parameters.build {
            append("Login", config.login)
            append("Password", config.password)
            append("Source", config.source)
            append("Phone", to)
            append("Text", text)
        }

        val xml: String?
        try {
            xml = config.client.post<String>(config.url) {
                body = FormDataContent(payload)
            }

        } catch (e: Throwable) {
            throw SmsException("Send sms failed with code: $text", e)
        }
        handleResponse(xml)

        return true
    }

    private fun handleResponse(xml: String) {

        val response: String = config.mapper.readValue(xml, object : TypeReference<String>() {}) ?: ""

        if (!response.matches(Regex("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}\$"))) {
            throw SmsException("Send sms failed with code: $response")
        }
    }
}
