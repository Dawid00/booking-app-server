package com.dpap.bookingapp.booking.place.room.dataaccess;


import com.dpap.bookingapp.booking.place.dataaccess.PlaceEntity;
import com.dpap.bookingapp.booking.place.room.RoomState;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import org.hibernate.annotations.Type;

@Entity()
@Table(name = "rooms")
public class RoomEntity {

    @Id
    @SequenceGenerator(name = "rooms_id_seq", sequenceName = "seq_rooms", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rooms_id_seq")
    private Long id;
    private Integer capacity;
    @Enumerated(EnumType.STRING)
    private RoomState state;
    private Long pricePerNight;
    private String description;
    private String name;
    @ManyToOne
    @JoinColumn(name="place_id", nullable=false)
    private PlaceEntity place;
    @Type(JsonType.class)
    @Column(columnDefinition = "jsonb")
    private String facilities;

    public void assignPlace(PlaceEntity place) {
        this.place = place;
    }

    public RoomEntity() {
    }

    public PlaceEntity getPlace() {
        return place;
    }

    public Long getId() {
        return id;
    }

    public RoomEntity(Integer capacity, RoomState state, Long pricePerNight, String description, String name, String facilities, PlaceEntity place) {
        this.capacity = capacity;
        this.state = state;
        this.pricePerNight = pricePerNight;
        this.description = description;
        this.name = name;
        this.facilities = facilities;
        this.place = place;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public RoomState getState() {
        return state;
    }

    public void setState(RoomState state) {
        this.state = state;
    }


    public Long getPricePerNight() {
        return pricePerNight;
    }

    public void setPricePerNight(Long pricePerNight) {
        this.pricePerNight = pricePerNight;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFacilities() {
        return facilities;
    }

    public void setFacilities(String facilities) {
        this.facilities = facilities;
    }

    public boolean canBeReserved() {
        return this.state.equals(RoomState.AVAILABLE);
    }
}
