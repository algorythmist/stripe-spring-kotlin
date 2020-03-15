package com.tecacet.stripe.service

import com.stripe.exception.StripeException
import com.stripe.model.*
import com.stripe.param.*
import com.tecacet.stripe.dto.CreditCardRequest
import java.math.BigDecimal
import java.util.*

class StripePaymentService {

    @Throws(StripeException::class)
    fun createCreditCardToken(creditCard: CreditCardRequest): Token {
        val card = TokenCreateParams.Card.builder()
                .setCvc(creditCard.ccv)
                .setNumber(creditCard.creditCardNumber)
                .setExpYear(Integer.toString(creditCard.expirationYear))
                .setExpMonth(Integer.toString(creditCard.expirationMonth))
                .build()
        val tokenCreateParams = TokenCreateParams.builder().setCard(card).build()
        return Token.create(tokenCreateParams)
    }

    @Throws(StripeException::class)
    fun createAchToken(accountNumber: String, routingNumber: String): Token {
        val bankAccount = TokenCreateParams.BankAccount.builder()
                .setAccountNumber(accountNumber)
                .setRoutingNumber(routingNumber)
                .setCountry(COUNTRY)
                .setCurrency(CURRENCY)
                .build()
        val tokenCreateParams = TokenCreateParams.builder().setBankAccount(bankAccount).build()
        return Token.create(tokenCreateParams)
    }

    @Throws(StripeException::class)
    fun createCreditCardPaymentSource(tokenId: String): Source {
        val sourceCreateParams = SourceCreateParams.builder()
                .setType(PAYMENT_SOURCE_CREDIT_CARD)
                .setToken(tokenId)
                .build()
        return Source.create(sourceCreateParams)
    }

    @Throws(StripeException::class)
    fun createAchdPaymentSource(tokenId: String, customer: Customer): Source {

        val owner = SourceCreateParams.Owner.builder()
                .setName(customer.getName())
                .setEmail(customer.getEmail())
                .build()
        val sourceCreateParams = SourceCreateParams.builder()
                .setType(PAYMENT_SOURCE_ACH_CREDIT)
                .setToken(tokenId)
                .setCurrency(CURRENCY)
                .setOwner(owner)
                .build()
        return Source.create(sourceCreateParams)
    }

    @Throws(StripeException::class)
    fun attachSource(customerId: String, sourceId: String): PaymentSource? {
        val params = mapOf("source" to sourceId)
        return Customer.retrieve(customerId).sources.create(params)
    }

    @Throws(StripeException::class)
    fun detachSource(sourceId: String): Boolean {
        val source = Source.retrieve(sourceId) ?: return false
        source.detach()
        return true
    }

    @Throws(StripeException::class)
    fun createCharge(sourceId: String, amount: BigDecimal,
                     customerId: String, description: String?): Charge {
        val chargeCreateParams = chargeBuilder(sourceId, amount, customerId, description).build()
        return Charge.create(chargeCreateParams)
    }

    @Throws(StripeException::class)
    fun createDestinationCharge(sourceId: String, amount: BigDecimal,
                                customerId: String, destinationAccountId: String,
                                description: String?): Charge {
        val destination = ChargeCreateParams.Destination.builder()
                .setAccount(destinationAccountId)
                .build()
        val chargeCreateParams = chargeBuilder(sourceId, amount, customerId, description)
                .setDestination(destination)
                .build()
        return Charge.create(chargeCreateParams)
    }

    @Throws(StripeException::class)
    fun findBalanceTransactionsByChargeId(chargeId: String): List<BalanceTransaction> {
        val transactionCollection = BalanceTransaction.list(BalanceTransactionListParams.builder()
                .setSource(chargeId).build())
        return transactionCollection.data
    }

    private fun chargeBuilder(sourceId: String, amount: BigDecimal,
                              customerId: String, description: String?): ChargeCreateParams.Builder {
        val longAmount = (amount * BigDecimal(100)).toLong()
        return ChargeCreateParams.builder()
                .setAmount(longAmount)
                .setCurrency(CURRENCY)
                .setDescription(description)
                .setSource(sourceId)
                .setCustomer(customerId)
    }
}