package com.example

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component
import org.springframework.validation.annotation.Validated
import javax.validation.constraints.NotEmpty

@Component
@ConfigurationProperties(prefix = "clova")
@Validated
class ClovaProperties {
    @field:NotEmpty
    lateinit var applicationId: String
}
