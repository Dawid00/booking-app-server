package com.dpap.bookingapp.booking.place.room;

import com.dpap.bookingapp.booking.place.dataaccess.PlaceEntity;
import com.dpap.bookingapp.booking.place.room.dataaccess.RoomEntity;
import com.dpap.bookingapp.booking.place.room.dataaccess.RoomRepository;
import com.dpap.bookingapp.booking.place.room.dto.AddRoomRequest;
import com.dpap.bookingapp.booking.place.room.dto.RoomId;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class RoomService {

    private final RoomRepository roomDatabase;
    public RoomService(RoomRepository roomRepository) {
        this.roomDatabase = roomRepository;
    }

    public RoomEntity addRoom(AddRoomRequest request, PlaceEntity placeEntity) {
        var room = Room.fromRequest(request);

        RoomEntity roomEntity = new RoomEntity(
                room.getCapacity(),
                room.getState(),
                room.getPricePerNight(), room.getDescription(), room.getName(), room.getFacilities(),
                placeEntity
        );
        return roomDatabase.save(roomEntity);
    }

    public Optional<RoomEntity> findRoomById(RoomId roomId) {
        return roomDatabase.findById(roomId.getId());
    }


}
