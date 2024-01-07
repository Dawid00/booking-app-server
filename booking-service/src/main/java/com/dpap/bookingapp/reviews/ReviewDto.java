package com.dpap.bookingapp.reviews;

public record ReviewDto(int rating, String author, String content, Long placeId, String at) {
}
