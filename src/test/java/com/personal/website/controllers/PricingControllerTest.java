package com.personal.website.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.personal.website.services.ContactService;

class PricingControllerTest {

    private ContactService contactService;
    private PricingController pricingController;

    @BeforeEach
    void setUp() {
        contactService = mock(ContactService.class);
        pricingController = new PricingController(contactService);
    }

    @Test
    void submitsOrderToContactServiceUsingTrustedPackagePrice() {
        ResponseEntity<?> response = pricingController.submitOrder(
            new PricingController.OrderRequest("Alex Morgan", "alex@example.com", "silver", "I would like this package.")
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(contactService).sendOrderRequest("Alex Morgan", "alex@example.com", "Silver", 750,         "I would like this package.");
    }

    @Test
    void rejectsMissingDescriptionWithoutSendingOrder() {
        ResponseEntity<?> response = pricingController.submitOrder(
            new PricingController.OrderRequest("Alex Morgan", "alex@example.com", "silver", "   ")
        );

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verifyNoInteractions(contactService);
    }

    @Test
    void rejectsUnknownPackageWithoutSendingOrder() {
        ResponseEntity<?> response = pricingController.submitOrder(
            new PricingController.OrderRequest("Alex Morgan", "alex@example.com", "custom", "I would like a custom package.")
        );

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verifyNoInteractions(contactService);
    }
}
