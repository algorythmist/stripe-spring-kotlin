package com.tecacet.stripe.dto

class StripeCustomerRequest {

    var firstName: String? = null
    var lastName: String? = null
    var email: String? = null
    var phoneNumber: String? = null
    var address: Address? = null

    fun getName(): String? {
        return java.lang.String.join(" ", firstName, lastName)
    }
}

fun customerRequest(init: StripeCustomerRequest.() -> Unit) : StripeCustomerRequest {
    val request = StripeCustomerRequest()
    request.init()
    return request
}

fun StripeCustomerRequest.address(init: Address.() -> Unit) {
    address = Address().apply(init)
}