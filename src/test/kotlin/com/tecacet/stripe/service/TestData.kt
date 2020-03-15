package com.tecacet.stripe.service

import com.tecacet.stripe.dto.*
import java.time.LocalDate

fun createAccountRequest() : StripeAccountRequest {

    return accountRequest {
        firstName = "Tony"
        lastName = "Montana"
        ssn = "123456789"
        email = "tony.montana@scarface.net"
        dateOfBirth = LocalDate.of(2000, 4, 1)
        address {
            streetAddress1 = "1 Maple St"
            city = "Sacramento"
            state = "CA"
            zip = "12345"
        }
    }
}


fun createCustomerRequest() : StripeCustomerRequest {
    return customerRequest {
        firstName = "Inigo"
        lastName = "Montoya"
        email = "inigo@revenge.net"
        address {
            streetAddress1 = "2 Oak St."
            city = "Modesto"
            state = "CA"
            zip = "12345"
        }
    }
}

fun createCreditCard() : CreditCardRequest {
    return creditCard {
        creditCardNumber = "4242424242424242"
        ccv = "123"
        expirationMonth = 10
        expirationYear = 2030
    }
}