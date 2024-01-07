package com.dpap.bookingapp.booking.place;


import com.dpap.bookingapp.booking.facility.FacilityType;
import com.dpap.bookingapp.booking.place.dataaccess.Address;
import com.dpap.bookingapp.booking.place.room.Room;

import java.util.List;

class Place {
    private Long id;
    private String name;
    private String description;
    private Address address;
    private PlaceCategory category;
    private Long landlordId;
    private List<FacilityType> facilities;
    private List<Room> rooms;

    public Place() {
    }

    public Place(Long id, String name, String description, Address address, PlaceCategory category, Long landlordId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.address = address;
        this.category = category;
        this.landlordId = landlordId;
    }

    public Place(String name, String description, Address address, PlaceCategory category, Long landlordId) {
        this.name = name;
        this.description = description;
        this.address = address;
        this.category = category;
        this.landlordId = landlordId;
    }

    public static Place fromRequest(AddPlaceRequest request, Long userId) {
        return new Place(
                request.name(),
                request.description(),
                request.address(),
                request.category(),
                userId
        );
    }

    public PlaceCategory getCategory() {
        return category;
    }


    public Long getLandlordId() {
        return landlordId;
    }


    public Long getId() {
        return id;
    }


    public String getName() {
        return name;
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public String getDescription() {
        return description;
    }


    public Address getAddress() {
        return address;
    }

    public List<FacilityType> getFacilities() {
        return facilities;
    }

    public void removeRoom(Room room) {
        this.rooms.remove(room);
    }

    public void addRoom(Room room) {
        this.rooms.add(room);
    }
}
