package com.tecacet.stripe.dto

import java.time.LocalDate
import javax.validation.constraints.NotEmpty

class StripeAccountRequest {
     var firstName: @NotEmpty String? = null
     var lastName: @NotEmpty String? = null
     var phone: @NotEmpty String? = null
     var email: @NotEmpty String? = null
     var address: @NotEmpty Address? = null
     var dateOfBirth: @NotEmpty LocalDate? = null
     var ssn: @NotEmpty String? = null

}

fun accountRequest(init: StripeAccountRequest.() -> Unit) : StripeAccountRequest {
     val request = StripeAccountRequest()
     request.init()

     return request
}

fun StripeAccountRequest.address(init: Address.() -> Unit) {
     address = Address().apply(init)
}