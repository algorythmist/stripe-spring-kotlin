package com.tecacet.stripe.service

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("local")
internal class StripeCustomerServiceTest {

    private val customerService = StripeCustomerService()

    @Test
    fun createCustomer() {
        val request = createCustomerRequest()
        val customer = customerService.createCustomer(request)
        println(customer)
        customerService.delete(customer.id)

    }
}