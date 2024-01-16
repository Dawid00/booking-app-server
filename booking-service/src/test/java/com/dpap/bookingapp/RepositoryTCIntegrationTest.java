package com.dpap.bookingapp;

import com.dpap.bookingapp.place.PlaceCategory;
import com.dpap.bookingapp.place.dataaccess.Address;
import com.dpap.bookingapp.place.dataaccess.PlaceEntity;
import com.dpap.bookingapp.place.dataaccess.PlaceRepository;
import com.dpap.bookingapp.users.UserEntity;
import com.dpap.bookingapp.users.UserJpaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest
public class RepositoryTCIntegrationTest {

    @Container
    public static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres:11.1")
            .withDatabaseName("integration-tests-db")
            .withUsername("sa")
            .withPassword("sa");

    @DynamicPropertySource
    static void registerPgProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", () -> "postgres");
        registry.add("spring.datasource.password", () -> "pass");
    }

    @Autowired
    private PlaceRepository placeRepository;
    @Autowired
    private UserJpaRepository userRepository;

    @Test
    public void whenFindByAddressCityThenReturnPlace() {
        // given
        var user = new UserEntity();
        userRepository.save(user);
        PlaceEntity place = new PlaceEntity();
        place.setName("Hotelik");
        place.setCategory(PlaceCategory.HOTEL);
        place.setAddress(new Address("Magdaleny Brzeskiej", "Lublin", "Poland", "42", "lubelskie"));
        place.setDescription("Description");
        place.setId(1L);
        place.setUser(user);
        placeRepository.save(place);
        // when
        List<PlaceEntity> found = placeRepository.findAllByCity("Lublin");
        // then
        assertThat(found.size()).isEqualTo(1);
        assertThat(found.get(0).getCategory()).isEqualTo(PlaceCategory.HOTEL);
    }

    @Test
    @Transactional
    public void whenDeletedByIdAndUserIDThenReturnEmptyList() {
        // given
        var user = new UserEntity();
        var savedUserId = userRepository.save(user).getId();
        PlaceEntity place = new PlaceEntity();
        place.setName("Hotelik");
        place.setCategory(PlaceCategory.HOTEL);
        place.setAddress(new Address("Magdaleny Brzeskiej", "Lublin", "Poland", "42", "lubelskie"));
        place.setDescription("Description");
        place.setId(1L);
        place.setUser(user);
        var savedId = placeRepository.save(place).getId();
        // when
        placeRepository.deleteAllByIdAndUserId(savedId, savedUserId);
        var result = placeRepository.findAll();
        // then
        assertThat(result.size()).isEqualTo(0);
    }

}
