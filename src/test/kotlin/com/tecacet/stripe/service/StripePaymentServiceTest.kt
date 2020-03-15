package com.tecacet.stripe.service

import com.stripe.model.Account
import com.stripe.model.Customer
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import java.math.BigDecimal

@SpringBootTest
@ActiveProfiles("local")
internal class StripePaymentServiceTest {

    private val accountService = StripeAccountService()
    private val customerService = StripeCustomerService()
    private val paymentService = StripePaymentService()

    @Test
    fun createCreditCardSource() {
        val creditCard = createCreditCard()
        val token = paymentService.createCreditCardToken(creditCard)
        val source = paymentService.createCreditCardPaymentSource(token.id)
        assertEquals("chargeable", source.status)
    }

    @Test
    fun endToEndPaymentTest() {
        val customer = createCustomer()
        val token = paymentService.createAchToken(routingNumber = "110000000",
                accountNumber = "000123456789")
        val source = paymentService.createAchdPaymentSource(token.id, customer)
        assertEquals("pending", source.status)
        paymentService.attachSource(customerId = customer.id, sourceId = source.id)

        val account = createAccount()

        val charge = paymentService.createDestinationCharge(
                sourceId = source.id,
                customerId = customer.id,
                amount = BigDecimal(8.88),
                destinationAccountId = account.id,
                description = "Payment of 8.88")
        println(charge.status)

        val transactions = paymentService.findBalanceTransactionsByChargeId(charge.id)
        assertEquals(1, transactions.size)

        accountService.delete(account.id)
        customerService.delete(customer.id)
    }

    private fun createCustomer(): Customer {
        val request = createCustomerRequest()
        return customerService.createCustomer(request)
    }

    private fun createAccount(): Account {
        val request = createAccountRequest()
        val account = accountService.createIndividualAccount(request)
        assertFalse(account.chargesEnabled)

        accountService.acceptTermsOfService(account.id, "127.0.0.1")
                ?: throw Exception("Failed to find account")

        accountService.linkBankAccount(accountId = account.id,
                routingNumber = "110000000",
                accountNumber = "000123456789")
        val completedAccount = accountService.findAccount(account.id)
                ?: throw Exception("Failed to find account")
        assertTrue(completedAccount.requirements.currentlyDue.isEmpty())
        assertTrue(completedAccount.chargesEnabled)
        return completedAccount
    }
}