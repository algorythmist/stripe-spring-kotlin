package com.tecacet.stripe.dto

import javax.validation.constraints.NotEmpty

class Address {

    var city: @NotEmpty String? = null
    var state: @NotEmpty String? = null
    var zip: @NotEmpty String? = null
    var country: @NotEmpty String? = null
    var streetAddress1: @NotEmpty String? = null
    var streetAddress2: String? = null
}

fun address(init: Address.() -> Unit) : Address {
    val address = Address()
    address.init()
    return address
}