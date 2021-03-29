# Sms service

## Installation
````kotlin
// Append repository
repositories {
    mavenCentral()
}

// Append dependency
implementation("com.icerockdev.service:sms-service:0.1.1")
````

## Library usage

### Twilio
````kotlin
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private val smsService = SmsService(
        scope,
        TwilioDriver(
            TwilioConfig(
                accountSID = "sid",
                authToken = "token",
                phone = "<fromNumber>"
            )
        )
    )

    
````

### Sms4b
````kotlin
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private val smsService = SmsService(
        scope,
        Sms4bDriver(
            Sms4bConfig(
                login = "login",
                password = "pass",
                source = "<Sender string>"
            )
        )
    )
````

### Sending
````kotlin
    runBlocking {
        smsService
            .sendAsync("<toNumber>", "TEST MESSAGE")
            .await()
    }
````
 
## Contributing
All development (both new features and bug fixes) is performed in the `develop` branch. This way `master` always contains the sources of the most recently released version. Please send PRs with bug fixes to the `develop` branch. Documentation fixes in the markdown files are an exception to this rule. They are updated directly in `master`.

The `develop` branch is pushed to `master` on release.

For more details on contributing please see the [contributing guide](CONTRIBUTING.md).

## License
        
    Copyright 2020 IceRock MAG Inc.
    
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at
    
       http://www.apache.org/licenses/LICENSE-2.0
    
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
