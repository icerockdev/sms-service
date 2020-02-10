/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

import com.icerockdev.service.sms.SmsException
import com.icerockdev.service.sms.SmsService
import com.icerockdev.service.sms.driver.sms4b.Sms4bConfig
import com.icerockdev.service.sms.driver.sms4b.Sms4bDriver
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.request.forms.FormDataContent
import io.ktor.http.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.runBlocking
import org.junit.Test

class Sms4bTest {

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private val smsUrl = "https://sms4b.ru/ws/sms.asmx/SendSMS"
    private val smsLogin = "login"
    private val smsPass = "pass"
    private val smsSource = "SMS4B-Test"

    private val Url.hostWithPortIfRequired: String get() = if (port == protocol.defaultPort) host else hostWithPort
    private val Url.fullUrl: String get() = "${protocol.name}://$hostWithPortIfRequired$fullPath"

    private val successResponse = "<?xml version=\"1.0\" encoding=\"utf-8\"?><string xmlns =\"SMS4B\">7CF4AC03-9E8F-37BD-4100-AB52004B004B</string>"
    private val successPhone = "+79139999999"
    private val errorResponse = "<?xml version=\"1.0\" encoding=\"utf-8\"?><string xmlns =\"SMS4B\">-51</string>"

    private val httpClient = HttpClient(MockEngine) {
        engine {
            addHandler { request ->

                when (request.url.fullUrl) {
                    smsUrl -> {
                        val responseHeaders =
                            headersOf("Content-Type" to listOf(ContentType.Application.Xml.toString()))

                        val formDataContent = request.body as FormDataContent
                        val params = formDataContent.formData

                        val phone = params["Phone"]
                        val login = params["Login"]
                        val password = params["Password"]
                        val source = params["Source"]

                        // TODO: send custom response for errors
                        if (login != smsLogin || password != smsPass || source != smsSource) {
                            return@addHandler respond(
                                errorResponse,
                                headers = responseHeaders
                            )
                        }

                        if (phone != successPhone) {
                            return@addHandler respond(
                                errorResponse,
                                headers = responseHeaders
                            )
                        }

                        return@addHandler respond(
                            successResponse,
                            headers = responseHeaders
                        )
                    }
                    else -> error("Unhandled ${request.url.fullUrl}")
                }
            }
        }
    }

    private val smsConfig = Sms4bConfig(
        client = httpClient,
        url = smsUrl,
        login = smsLogin,
        password = smsPass,
        source = smsSource
    )

    private val smsService = SmsService(
        scope,
        Sms4bDriver(
            smsConfig
        )
    )

    @Test
    fun testSuccessSend() {
        runBlocking {
            smsService
                .sendAsync(successPhone, "TEST MESSAGE")
                .await()
        }
    }

    @Test(expected = SmsException::class)
    fun testFailedFormatPhoneNumber() {
        runBlocking {
            smsService
                .sendAsync("79134", "TEST MESSAGE")
                .await()
        }
    }

}
