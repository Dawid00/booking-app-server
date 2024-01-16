package com.dpap.bookingapp.place.room;

import com.dpap.bookingapp.common.JsonToCollectionMapper;
import com.dpap.bookingapp.common.FacilityType;
import com.dpap.bookingapp.place.room.dto.AddRoomRequest;
import com.dpap.bookingapp.place.room.dto.RoomId;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Room {

    private RoomId id;
    private Integer capacity;
    private RoomState state;
    private Long placeId;
    private Long pricePerNight;
    private String description;
    private String name;
    private String facilities;

    void disableRoom() {
        this.state = RoomState.UNAVAILABLE;
    }

    void enableRoom() {
        this.state = RoomState.AVAILABLE;
    }

    void changePricePerNight(Long price) {
        if (price > 0)
            this.pricePerNight = price;
    }

    void changeCapacity(Integer number) {
        if (number > 0 && number < 20)
            this.capacity = number;
    }

    public Room(RoomId id, Integer capacity, RoomState state, Long placeId, Long pricePerNight, String description, String name, String facilities) {
        this.id = id;
        this.capacity = capacity;
        this.state = state;
        this.placeId = placeId;
        this.pricePerNight = pricePerNight;
        this.description = description;
        this.name = name;
        this.facilities = facilities;
    }

    public Room(Integer capacity, RoomState state, Long placeId, Long pricePerNight, String description, String name, String facilities) {
        this.capacity = capacity;
        this.state = state;
        this.placeId = placeId;
        this.pricePerNight = pricePerNight;
        this.description = description;
        this.name = name;
        this.facilities = facilities;
    }

    public void addFacilities(Set<FacilityType> facilities) {
        facilities.forEach(this::addFacility);
    }

    public void addFacility(FacilityType facilityType) {
        var set = JsonToCollectionMapper.deserialize(facilities);
        set.add(facilityType);
        this.facilities = JsonToCollectionMapper.serialize(set);
    }

    public List<FacilityType> getFacilityTypes() {
        return JsonToCollectionMapper
                .deserialize(facilities)
                .stream()
                .toList();
    }

    public static Room fromRequest(AddRoomRequest request) {
        return new Room(
                request.capacity(),
                request.state(),
                request.placeId(),
                request.pricePerNight(),
                request.description(),
                request.name(),
                JsonToCollectionMapper.serialize(new HashSet<>(request.facilities()))
        );
    }

    public RoomId getId() {
        return id;
    }

    public Long getPricePerNight() {
        return pricePerNight;
    }

    public Integer getCapacity() {
        return capacity;
    }


    public RoomState getState() {
        return state;
    }

    public Long getPlaceId() {
        return placeId;
    }

    public boolean canBeReserved() {
        return RoomState.AVAILABLE.equals(state);
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    @JsonIgnore
    public String getFacilities() {
        return facilities;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
