package com.tecacet.stripe.service

import com.stripe.exception.StripeException
import com.stripe.model.Account
import com.stripe.model.BankAccount
import com.stripe.model.ExternalAccount
import com.stripe.param.AccountCreateParams
import com.tecacet.stripe.dto.Address
import com.tecacet.stripe.dto.StripeAccountRequest
import java.time.LocalDate

const val BUSINESS_TYPE = "Aquariums"
const val MCC = "7998"
const val INDIVIDUAL_ACCOUNT_TYPE = "individual"

class StripeAccountService {

    @Throws(StripeException::class)
    fun createIndividualAccount(request: StripeAccountRequest): Account {
        val individual = AccountCreateParams.Individual.builder().setFirstName(request.firstName)
                .setLastName(request.lastName)
                .setIdNumber(request.ssn)
                .setDob(getDateOfBirth(request.dateOfBirth))
                .setEmail(request.email)
                .setPhone(request.phone)
                .setAddress(createIndividualAddress(request.address!!))
                .build()
        val accountCreateParams = AccountCreateParams.builder()
                .setCountry(COUNTRY)
                .setDefaultCurrency(CURRENCY)
                .setType(AccountCreateParams.Type.CUSTOM)
                .setBusinessType(INDIVIDUAL_ACCOUNT_TYPE)
                .setBusinessProfile(createBusinessProfile())
                .setIndividual(individual)
                .addRequestedCapability(AccountCreateParams.RequestedCapability.TRANSFERS)
                .build()
        return Account.create(accountCreateParams)
    }

    @Throws(StripeException::class)
    fun findAccount(accountId: String): Account? = Account.retrieve(accountId)

    @Throws(StripeException::class)
    fun delete(accountId: String): Boolean {
        val account = findAccount(accountId) ?: return false
        account.delete()
        return true
    }

    @Throws(StripeException::class)
    fun acceptTermsOfService(accountId: String?,
                             ipAddress: String): Account? {
        val account = Account.retrieve(accountId) ?: return null
        val tosAcceptanceParams = mapOf(
                "date" to System.currentTimeMillis() / 1000L,
                "ip" to ipAddress,
                "tos_acceptance" to mapOf<String, String>()
        )
        val params = mapOf("tos_acceptance" to tosAcceptanceParams)
        return account.update(params)
    }

    @Throws(StripeException::class)
    fun linkBankAccount(accountId: String,
                        accountNumber: String,
                        routingNumber: String): ExternalAccount? {
        val account = Account.retrieve(accountId)
        val details = mapOf(
                "object" to "bank_account",
                "country" to COUNTRY,
                "currency" to CURRENCY,
                "account_number" to accountNumber,
                "routing_number" to routingNumber)
        val params = mapOf("external_account" to details)
        return account.externalAccounts.create(params)
    }

    private fun createIndividualAddress(address: Address): AccountCreateParams.Individual.Address {
        return AccountCreateParams.Individual.Address.builder()
                .setCity(address.city)
                .setLine1(address.streetAddress1)
                .setState(address.state)
                .setPostalCode(address.zip)
                .setCountry(COUNTRY)
                .build()
    }

    private fun getDateOfBirth(date: LocalDate?): AccountCreateParams.Individual.Dob? {
        return if (date == null) {
            null
        } else AccountCreateParams.Individual.Dob.builder()
                .setDay(date.dayOfMonth.toLong())
                .setMonth(date.monthValue.toLong())
                .setYear(date.year.toLong()).build()
    }

    private fun createBusinessProfile(): AccountCreateParams.BusinessProfile {
        return AccountCreateParams.BusinessProfile.builder()
                .setProductDescription(BUSINESS_TYPE)
                .setMcc(MCC)
                .build()
    }
}