package com.tecacet.stripe.service

import com.tecacet.stripe.dto.accountRequest
import com.tecacet.stripe.dto.address
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import java.time.LocalDate

@SpringBootTest
@ActiveProfiles("local")
internal class StripeAccountServiceTest {

    private val stripeAccountService = StripeAccountService()

    @Test
    fun createIndividualAccount() {
        val request = accountRequest {
            firstName = "Tony"
            lastName = "Montana"
            ssn = "123456789"
            dateOfBirth = LocalDate.of(2000, 4, 1)
            address {
                streetAddress1 = "1 Maple St"
                city = "Sacramento"
                state = "CA"
                zip = "12345"
            }
        }
        val account = stripeAccountService.createIndividualAccount(request)
        //TODO: System.out.println(account)
        stripeAccountService.delete(account.id)
    }
}