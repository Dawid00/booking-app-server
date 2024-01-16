package com.dpap.bookingapp.reservation;

import com.dpap.bookingapp.reservation.dto.SearchReservationFilter;
import com.dpap.bookingapp.place.room.dto.RoomId;
import io.vavr.control.Option;
import io.vavr.control.Try;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static io.vavr.control.Option.none;
import static io.vavr.control.Option.of;

@Component
public class ReservationRepository implements ReservationDatabase {

    private static final String FIND_BY_ID =
            """
                    SELECT r.id,r.place_id, r.start, r.finish, rs.name AS state, r.user_id, r.room_id, r.at, r.value, r.free_cancellation_days
                    FROM reservations r JOIN reservation_states rs on r.state_id = rs.id
                    WHERE r.id = ?
                    """;
    private static final String FIND_ALL_BY_HOST =
            """
                    SELECT r.place_id, r.id, r.start, r.finish, rs.name AS state, r.user_id, r.room_id, r.at, r.value, r.free_cancellation_days
                    FROM reservations r JOIN reservation_states rs on r.state_id = rs.id JOIN places p on p.id = r.place_id
                    AND p.user_id = ?
                    """;
    private static final String FIND_ALL =
            """
                    SELECT r.id, r.place_id,r.start, r.finish, rs.name AS state, r.user_id, r.room_id, r.at,r.value, r.free_cancellation_days
                    FROM reservations r JOIN reservation_states rs on r.state_id = rs.id
                    """;
    private static final String FIND_RESERVATION_BY_ROOM_ID_BETWEEN_DATES = """
            SELECT r.id, r.start, r.place_id, r.finish, rs.name AS state, r.user_id, r.room_id, r.at,r.value, r.free_cancellation_days
            FROM reservations r JOIN reservation_states rs on r.state_id = rs.id
            WHERE r.room_id = ?
             AND r.start <= ?
             AND r.finish >= ?
            """;
    private static final String RESERVE_ROOM = """
            INSERT INTO  reservations VALUES(
            nextval('seq_reservations'),
            ?,
            ?,
            ?,
            ?,
            ?,
            ?,
            (SELECT id FROM reservation_states WHERE name LIKE ?),
            ?,
            ?
            )
            """;
    private static final String UPDATE_STATE = """
            UPDATE  reservations SET state_id = (SELECT id FROM reservation_states WHERE name LIKE ?),
            value = ?
            WHERE id = ?
            """;
    private static final String FIND_BY_ID_AND_USER_ID =
            """
                                                        SELECT r.place_id, r.id, r.start, r.finish, rs.name AS state, r.user_id, r.room_id, r.at, r.value, r.free_cancellation_days
                            FROM reservations r JOIN reservation_states rs on r.state_id = rs.id
                            WHERE r.id = ? AND r.user_id = ?
                    """;

    private final JdbcTemplate jdbcTemplate;

    public ReservationRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Reservation> getByRoomId(RoomId roomId, LocalDateTime from, LocalDateTime to) {
        return this.jdbcTemplate.query(
                FIND_RESERVATION_BY_ROOM_ID_BETWEEN_DATES,
                reservationRowMapper(),
                roomId.getId(), to, from
        );
    }

    public List<Reservation> findAllByFilters(SearchReservationFilter filter) {

        if (filter.isEmpty()) {
            return findAll();
        }

        return jdbcTemplate.query(
                buildSearchWithFiltersQuery(filter),
                reservationRowMapper(),
                buildSearchWithFiltersParams(filter)
        );
    }

    public List<Reservation> findAllToAccept(Long hostId) {
        return jdbcTemplate.query(
                FIND_ALL_BY_HOST,
                reservationRowMapper(),
                hostId
        );
    }

    private Object[] buildSearchWithFiltersParams(SearchReservationFilter filter) {
        var args = new ArrayList<>();

        if (filter.getRoomId().isPresent()) {
            args.add(filter.getRoomId().get());
        }
        if (filter.getUserId().isPresent()) {
            args.add(filter.getUserId().get());
        }
        if (filter.getPlaceId().isPresent()) {
            args.add(filter.getPlaceId().get());
        }
        if (filter.getState().isPresent()) {
            args.add(filter.getState().get());
        }
        if (filter.getFrom().isPresent()) {
            args.add(filter.getFrom().get());
        }
        if (filter.getTo().isPresent()) {
            args.add(filter.getTo().get());
        }

        return args.toArray();
    }


    private String buildSearchWithFiltersQuery(SearchReservationFilter filter) {
        StringBuilder query = new StringBuilder(FIND_ALL).append(" WHERE ");
        query.append(filter.getRoomId().map(id -> "r.room_id= ? AND").orElse(""));
        query.append(filter.getUserId().map(id -> "r.user_id= ? AND ").orElse(""));
        query.append(filter.getPlaceId().map(id -> "r.place_id= ? AND ").orElse(""));
        query.append(filter.getState().map(state -> "rs.name= ? AND ").orElse(""));
        query.append(filter.getFrom().map(from -> "r.finish >= ? AND ").orElse(""));
        query.append(filter.getTo().map(to -> "r.start <= ? AND ").orElse(""));
        query.replace(query.lastIndexOf(" AND"), query.lastIndexOf(" AND") + 4, "");
        return query.toString();
    }

    public List<Reservation> findAll() {
        return this.jdbcTemplate.query(
                FIND_ALL,
                reservationRowMapper()
        );
    }

    public Option<Reservation> findById(Long id) {
        return Try
                .ofSupplier(
                        () -> of(
                                jdbcTemplate.query(
                                        FIND_BY_ID,
                                        reservationRowMapper(),
                                        id
                                ).get(0)
                        )
                )
                .getOrElse(none());
    }

    public Option<Reservation> findByIdAndUserId(Long id, Long userId) {
        return Try
                .ofSupplier(
                        () -> of(
                                jdbcTemplate.query(
                                        FIND_BY_ID_AND_USER_ID,
                                        reservationRowMapper(),
                                        id, userId
                                ).get(0)
                        )
                )
                .getOrElse(none());
    }

    public void save(Reservation reservation) {
        this.jdbcTemplate.update(
                RESERVE_ROOM,
                reservation.getRoomId().getId(),
                reservation.getPlaceId(),
                reservation.getUserId(),
                reservation.getCheckIn(),
                reservation.getCheckOut(),
                reservation.getAt(),
                reservation.getState().name(),
                reservation.getValue(),
                reservation.getFreeCancellationDays()
        );
    }

    @Override
    public void update(Long reservationId, BigDecimal value, ReservationState state) {
        this.jdbcTemplate.update(UPDATE_STATE, state.name(), value, reservationId);
    }

    private RowMapper<Reservation> reservationRowMapper() {
        return (rs, row) ->
                new Reservation(
                        rs.getLong("id"),
                        RoomId.fromLong(rs.getLong("room_id")),
                        rs.getLong("place_id"),
                        LocalDateTime.of(rs.getObject("start", LocalDate.class), LocalTime.NOON),
                        LocalDateTime.of(rs.getObject("finish", LocalDate.class), LocalTime.NOON),
                        rs.getObject("at", LocalDateTime.class),
                        rs.getLong("user_id"),
                        BigDecimal.valueOf(rs.getDouble("value")),
                        rs.getInt("free_cancellation_days"),
                        ReservationState.valueOf(rs.getString("state"))
                );
    }
}
