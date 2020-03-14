package com.tecacet.stripe

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class StripeSpringKotlinApplication

fun main(args: Array<String>) {
	runApplication<StripeSpringKotlinApplication>(*args)
}
