package com.tecacet.stripe.service

import com.stripe.exception.StripeException
import com.stripe.model.Charge
import com.stripe.model.Customer
import com.stripe.model.PaymentSource
import com.stripe.model.Source
import com.stripe.param.ChargeCreateParams
import com.stripe.param.SourceCreateParams
import java.math.BigDecimal

class StripePaymentService {

    @Throws(StripeException::class)
    fun createPaymentSource(tokenId: String, paymentType: String): Source {
        val sourceCreateParams = SourceCreateParams.builder()
                .setType(paymentType)
                .setToken(tokenId)
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
        val longAmount = (amount * BigDecimal(100)).toLong()
        val chargeCreateParams = ChargeCreateParams.builder()
                .setAmount(longAmount)
                .setCurrency(CURRENCY)
                .setDescription(description)
                .setSource(sourceId)
                .setCustomer(customerId)
                .build()
        return Charge.create(chargeCreateParams)
    }
}