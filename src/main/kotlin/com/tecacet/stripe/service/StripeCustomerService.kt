package com.tecacet.stripe.service

import com.stripe.exception.StripeException
import com.stripe.model.Customer
import com.stripe.model.Token
import com.stripe.param.CustomerCreateParams
import com.stripe.param.TokenCreateParams
import com.tecacet.stripe.dto.Address
import com.tecacet.stripe.dto.CreditCardRequest
import com.tecacet.stripe.dto.StripeCustomerRequest

class StripeCustomerService {

    @Throws(StripeException::class)
    fun createCustomer(request: StripeCustomerRequest): Customer {
        val customerCreateParams = CustomerCreateParams.builder()
                .setName(request.getName())
                .setPhone(request.phoneNumber)
                .setEmail(request.email)
                .setAddress(getCustomerAddress(request.address))
                .build()
        return Customer.create(customerCreateParams)
    }

    private fun getCustomerAddress(address: Address?): CustomerCreateParams.Address? {
        if (address == null) {
            return null
        }
        return CustomerCreateParams.Address.builder()
                .setCity(address.city)
                .setState(address.state)
                .setCountry(COUNTRY)
                .setLine1(address.streetAddress1)
                .setPostalCode(address.zip)
                .build()
    }

    @Throws(StripeException::class)
    fun findCustomer(id: String) : Customer? = Customer.retrieve(id)

    fun delete(customerId : String) : Boolean {
        val customer = findCustomer(customerId) ?: return false
        customer.delete()
        return true
    }
}