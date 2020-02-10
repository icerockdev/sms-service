/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package com.icerockdev.service.sms

import com.icerockdev.service.sms.driver.ISmsDriver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class SmsService(private val coroutineScope: CoroutineScope?, private val driver: ISmsDriver) {

    suspend fun send(to: String, text: String): Boolean {
        return try {
            driver.send(to, text)
        } catch (t: SmsException) {
            LOGGER.error(t.localizedMessage)
            throw  t
        }

    }

    fun sendAsync(to: String, text: String): Deferred<Boolean> {

        if (coroutineScope === null) {
            throw SmsException("Async sending unsupported")
        }

        return coroutineScope.async {
            try {
                driver.send(to, text)
            } catch (t: SmsException) {
                LOGGER.error(t.localizedMessage)
                throw  t
            }
        }
    }


    private companion object {
        val LOGGER: Logger = LoggerFactory.getLogger(SmsService::class.java)
    }

}
