/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package com.icerockdev.service.sms.driver.twilio

import com.twilio.http.TwilioRestClient

data class TwilioConfig(
    val accountSID: String,
    val authToken: String,
    val client: TwilioRestClient = TwilioRestClient.Builder(
        accountSID,
        authToken
    ).build(),
    val phone: String
)
