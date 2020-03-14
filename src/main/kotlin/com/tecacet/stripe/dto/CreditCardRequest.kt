package com.tecacet.stripe.dto

import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

class CreditCardRequest {

    @NotEmpty
    var creditCardNumber : String? = null  //encrypted
    @NotEmpty
    var ccv : String? = null //encrypted
    @NotEmpty
    var zipCode: String? = null
    @NotNull
    var expirationMonth : Int = 0
    @NotNull
    var expirationYear : Int = 0
}

fun creditCard(init : CreditCardRequest.() -> Unit) :CreditCardRequest {
    val request = CreditCardRequest()
    request.init()
    return request
}