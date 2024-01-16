package com.dpap.bookingapp.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class JsonToCollectionMapper {
    private static ObjectMapper objectMapper;

    static {
        objectMapper = new ObjectMapper();
    }

    public static Set<FacilityType> deserialize(String json) {
        if (json == null) {
            return new HashSet<>();
        }
        try {
            Set<String> set =
                    objectMapper
                            .readValue(
                                    json,
                                    objectMapper
                                            .getTypeFactory()
                                            .constructCollectionType(Set.class, String.class)
                            );
            return set.stream()
                    .map(FacilityType::valueOf)
                    .collect(Collectors.toSet());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String serialize(Set<FacilityType> facilityTypes) {
        try {
            Set<String> facilities = facilityTypes.stream()
                    .map(Enum::name)
                    .collect(Collectors.toSet());
            return objectMapper.writeValueAsString(facilities);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
