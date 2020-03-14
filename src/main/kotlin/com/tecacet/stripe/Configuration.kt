package com.tecacet.stripe

import com.stripe.Stripe
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class Configuration {

    @Value("\${stripe-api-key}")
    private lateinit var apiKey: String

    @Bean
    fun configureApiKey() {
        Stripe.apiKey = apiKey
    }

}