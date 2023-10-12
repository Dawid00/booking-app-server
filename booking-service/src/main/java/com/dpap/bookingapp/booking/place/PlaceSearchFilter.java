package com.dpap.bookingapp.booking.place;


import java.util.Optional;

public class PlaceSearchFilter {

    private String voivodeship;
    private String city;
    private PlaceCategory category;
    private Long userId;



    private PlaceSearchFilter(Builder builder) {
        voivodeship = builder.voivodeship;
        city = builder.city;
        category = builder.category;
        userId = builder.userId;

    }

    public boolean isEmpty() {
        return getCity().isEmpty() && getCategory().isEmpty() && getVoivodeship().isEmpty() && getUserId().isEmpty();
    }

    public Optional<String> getVoivodeship() {
        return Optional.ofNullable(voivodeship);
    }

    public Optional<Long> getUserId() {
        return Optional.ofNullable(userId);
    }

    public Optional<String> getCity() {
        return Optional.ofNullable(city);
    }

    public Optional<PlaceCategory> getCategory() {
        return Optional.ofNullable(category);
    }

    public static final class Builder {
        private String voivodeship;
        private String city;
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

        public PlaceSearchFilter build() {
            return new PlaceSearchFilter(this);
        }
    }
}