package com.dpap.bookingapp.users.dto;

import jakarta.validation.constraints.Email;

public record EmailRequest(@Email String email) {
}
