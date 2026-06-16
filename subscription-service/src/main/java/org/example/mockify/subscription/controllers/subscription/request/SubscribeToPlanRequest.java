package org.example.mockify.subscription.controllers.subscription.request;

import jakarta.validation.constraints.NotBlank;

public record SubscribeToPlanRequest(@NotBlank String plan) {}
