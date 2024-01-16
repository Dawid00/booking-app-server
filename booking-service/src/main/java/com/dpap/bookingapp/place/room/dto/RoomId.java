package com.dpap.bookingapp.place.room.dto;

import java.util.Objects;

public class RoomId {
    private final Long id;

    private RoomId(Long id) {
        this.id = id;
    }

    public static RoomId fromLong(long id) {
        return new RoomId(id);
    }

    public Long getId() {
        return this.id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoomId roomId = (RoomId) o;
        return Objects.equals(id, roomId.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
