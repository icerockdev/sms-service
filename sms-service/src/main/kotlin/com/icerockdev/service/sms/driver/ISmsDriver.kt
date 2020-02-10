/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package com.icerockdev.service.sms.driver

import com.icerockdev.service.sms.SmsException

interface ISmsDriver {
    @Throws(SmsException::class)
    suspend fun send(to: String, text: String): Boolean
}
