package com.dpap.bookingapp;

import com.dpap.bookingapp.availability.usage.UsageService;
import com.dpap.bookingapp.place.PlaceCategory;
import com.dpap.bookingapp.place.PlaceService;
import com.dpap.bookingapp.place.dataaccess.Address;
import com.dpap.bookingapp.place.dataaccess.PlaceEntity;
import com.dpap.bookingapp.place.dataaccess.PlaceRepository;
import com.dpap.bookingapp.place.room.RoomService;
import com.dpap.bookingapp.users.UserEntity;
import com.dpap.bookingapp.users.UserRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;


@SpringBootTest
public class PlaceServiceTest {


    @Mock
    UsageService usageService;

    @Mock
    PlaceRepository placeRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    RoomService roomService;

    @Mock
    EntityManager entityManager;

    @InjectMocks
    private PlaceService placeService;

    @Test
    public void shouldReturnPlaceById() {
        var place = new PlaceEntity();
        place.setId(1L);
        place.setName("Hotelik");
        place.setCategory(PlaceCategory.HOTEL);
        place.setAddress(new Address("Magdaleny Brzeskiej", "Lublin", "Poland", "42", "lubelskie"));
        place.setDescription("Description");
        place.setId(1L);
        place.setUser(new UserEntity());
        placeRepository.save(place);
        when(placeRepository.findById(1L)).thenReturn(Optional.of(place));

        var result = placeService.findPlaceResponseById(1L);

        Assertions.assertEquals(1L, result.id());
        Assertions.assertEquals("Hotelik", result.name());
    }

    @Test
    public void shouldThrowExceptionWhenPlaceDoesNotExist() {
        when(placeRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> placeService.findPlaceById(1L));
    }



}
