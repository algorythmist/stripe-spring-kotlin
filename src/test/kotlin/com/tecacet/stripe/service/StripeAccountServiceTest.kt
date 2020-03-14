package com.tecacet.stripe.service

import com.tecacet.stripe.dto.accountRequest
import com.tecacet.stripe.dto.address
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import java.time.LocalDate

@SpringBootTest
@ActiveProfiles("local")
internal class StripeAccountServiceTest {

    private val accountService = StripeAccountService()

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
        val account = accountService.createIndividualAccount(request)
        assertFalse(account.chargesEnabled)
        val currentlyDue = account.requirements.currentlyDue
        assertEquals("[external_account, tos_acceptance.date, tos_acceptance.ip]", currentlyDue.toString())

        val updated = accountService.acceptTermsOfService(account.id, "127.0.0.1")
                ?: throw Exception("Failed to find account")
        assertEquals("[external_account]", updated.requirements.currentlyDue.toString())

        val bankAccount = accountService.linkBankAccount(accountId =  account.id,
                routingNumber = "110000000",
                accountNumber = "000123456789")
        val completedAccount = accountService.findAccount(account.id)
                ?: throw Exception("Failed to find account")
        assertTrue(completedAccount.requirements.currentlyDue.isEmpty())
        assertTrue(completedAccount.chargesEnabled)

        accountService.delete(account.id)
    }
}