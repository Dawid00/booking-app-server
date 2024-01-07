package com.dpap.bookingapp.reviews;

public record CreateReviewDto(Long placeId, String content, int rating) {
}
