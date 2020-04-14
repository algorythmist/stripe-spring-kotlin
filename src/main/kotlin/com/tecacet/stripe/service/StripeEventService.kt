package com.tecacet.stripe.service

import com.stripe.model.Event
import com.stripe.model.WebhookEndpoint
import com.stripe.net.Webhook
import com.stripe.param.EventListParams
import com.stripe.param.WebhookEndpointListParams

class StripeEventService {

    fun findEvent(eventId : String): Event? {
        return Event.retrieve(eventId)
    }

    fun findEventsByType(eventType : String) : List<Event> {
        val params = EventListParams.builder().setType(eventType).build()
        return Event.list(params).data ?: listOf()
    }

    fun findWebhooks() : List<WebhookEndpoint> {
        return WebhookEndpoint.list(WebhookEndpointListParams.builder().build()).data ?: listOf()
    }
}