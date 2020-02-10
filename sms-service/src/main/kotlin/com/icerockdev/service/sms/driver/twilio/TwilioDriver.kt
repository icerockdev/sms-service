/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package com.icerockdev.service.sms.driver.twilio

import com.icerockdev.service.sms.SmsException
import com.icerockdev.service.sms.driver.ISmsDriver
import com.twilio.exception.ApiException
import com.twilio.rest.api.v2010.account.MessageCreator
import com.twilio.type.PhoneNumber

class TwilioDriver(private val config: TwilioConfig) : ISmsDriver {

    @Throws(SmsException::class)
    override suspend fun send(to: String, text: String): Boolean {
        try {
            MessageCreator(
                PhoneNumber(to),
                PhoneNumber(config.phone),
                text
            ).create(config.client)
        } catch (e: ApiException) {
            throw SmsException(e.message!!, e)
        }

        return true
    }
}
