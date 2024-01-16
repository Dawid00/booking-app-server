package com.dpap.bookingapp.reservation.dto;

import java.math.BigDecimal;

public record FeeRequest(BigDecimal value, String reason) {
}
