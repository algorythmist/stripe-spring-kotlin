package com.tecacet.stripe.service

import com.stripe.exception.StripeException
import com.stripe.model.BalanceTransaction
import com.stripe.model.Payout
import com.stripe.param.BalanceTransactionListParams
import com.stripe.param.PayoutListParams

class StripePayoutService {

    fun findPayout(payoutId : String): Payout? {
        return Payout.retrieve(payoutId)
    }

    @Throws(StripeException::class)
    fun findAllPayouts(): List<Payout> {
        val payoutCollection = Payout.list(PayoutListParams.builder().build())
        return payoutCollection.data ?: emptyList()
    }

    @Throws(StripeException::class)
    fun findBalanceTransactionsByPayoutId(payoutId: String): List<BalanceTransaction?> {
        val transactions = BalanceTransaction.list(BalanceTransactionListParams.builder()
                .setPayout(payoutId)
                .setType("charge")
                .build())
        return transactions.data ?: emptyList()
    }
}