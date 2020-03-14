package com.tecacet.stripe.service

import com.tecacet.stripe.dto.address
import com.tecacet.stripe.dto.customerRequest

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("local")
internal class StripeCustomerServiceTest {

    private val customerService = StripeCustomerService()

    @Test
    fun createCustomer() {
        val request = customerRequest {
            firstName = "James"
            lastName = "Dean"
            email = "james.dean@nowhere.net"
            address {
                streetAddress1 = "1 Maple St"
                city = "Sacramento"
                state = "CA"
                zip = "12345"
            }
        }
        val customer = customerService.createCustomer(request)
        //TODO: System.out.println(customer)
        customerService.delete(customer.id)

    }
}