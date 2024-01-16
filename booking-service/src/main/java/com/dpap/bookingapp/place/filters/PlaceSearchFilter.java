package com.dpap.bookingapp.place.filters;


import com.dpap.bookingapp.place.PlaceCategory;

import java.util.Optional;

public class PlaceSearchFilter {

    private String voivodeship;
    private String city;
    private PlaceCategory category;
    private Long userId;
    private Long placeId;
    private String street;


    public PlaceSearchFilter(Builder builder) {
        voivodeship = builder.voivodeship;
        city = builder.city;
        category = builder.category;
        placeId = builder.placeId;
        userId = builder.userId;
        street = builder.street;
    }

    public boolean isEmpty() {
        return getCity().isEmpty() && getStreet().isEmpty() && getPlaceId().isEmpty() &&
                getCategory().isEmpty() &&
                getVoivodeship().isEmpty() &&
                getUserId().isEmpty();
    }

    public Optional<String> getVoivodeship() {
        return Optional.ofNullable(voivodeship);
    }

    public Optional<Long> getUserId() {
        return Optional.ofNullable(userId);
    }

    public Optional<Long> getPlaceId() {
        return Optional.ofNullable(placeId);
    }

    public Optional<String> getCity() {
        return Optional.ofNullable(city);
    }

    public Optional<String> getStreet() {
        return Optional.ofNullable(street);
    }

    public Optional<PlaceCategory> getCategory() {
        return Optional.ofNullable(category);
    }

    public static final class Builder {
        public Long placeId;
        private String voivodeship;
        private String city;
        private String street;
        private PlaceCategory category;
        private Long userId;

        private Builder() {
        }

        public static Builder newBuilder() {
            return new Builder();
        }

        public Builder voivodeship(String val) {
            voivodeship = val;
            return this;
        }

        public Builder city(String val) {
            city = val;
            return this;
        }

        public Builder category(PlaceCategory val) {
            category = val;
            return this;
        }

        public Builder userId(Long val) {
            userId = val;
            return this;
        }

        public Builder placeId(Long val) {
            placeId = val;
            return this;
        }

        public PlaceSearchFilter build() {
            return new PlaceSearchFilter(this);
        }

        public Builder street(String val) {
            street = val;
            return this;
        }
    }
}
