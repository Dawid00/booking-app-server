package com.dpap.bookingapp.availability.timeslot;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TimeSlot {

    private final LocalDateTime start;
    private final LocalDateTime end;

    public TimeSlot(LocalDateTime start, LocalDateTime end) {
        if (start.isAfter(end)) {
            throw new RuntimeException("[" + start + "] is after end date[" + end + "].");
        }
        this.start = start;
        this.end = end;
    }

    public List<TimeSlot> allTimeSlotsWithDurationBetweenEach(Integer hoursDuration) {
        List<TimeSlot> periods = new ArrayList<>();
        var startDate = this.start;
        while (!startDate.plusHours(hoursDuration).isAfter(end)) {
            periods.add(new TimeSlot(startDate, startDate.plusHours(hoursDuration)));
            startDate = startDate.plusDays(1);
        }
        return periods;
    }

    public boolean intersect(TimeSlot other) {
        return this.end.isAfter(other.start) && this.start.isBefore(other.end);
    }

    public TimeSlot overlap(TimeSlot other) {
        if (areOverlapped(other)) {
            return new TimeSlot(this.start, other.end);
        }
        throw new RuntimeException("Can not overlap");
    }

    private boolean areOverlapped(TimeSlot other) {
        return this.end.equals(other.start);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TimeSlot that = (TimeSlot) o;
        return Objects.equals(start, that.start) && Objects.equals(end, that.end);
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, end);
    }

    @Override
    public String toString() {
        return "TimeSlot{" +
                "start=" + start +
                ", end=" + end +
                '}';
    }

    public LocalDateTime getStart() {
        return start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public boolean intersect(TimeSlot one, TimeSlot another) {
        return one.getEnd().isEqual(another.getStart()) || one.getEnd().isAfter(another.getStart());
    }

    public TimeSlot adjustHours(int startHour, int endHour) {
        var startDate = LocalDateTime.of(
                this.start.getYear(),
                this.start.getMonth(),
                this.start.getDayOfMonth(),
                startHour, 0, 0
        );
        var endDate = LocalDateTime.of(
                this.end.getYear(),
                this.end.getMonth(),
                this.end.getDayOfMonth(),
                endHour, 0, 0
        );
        return new TimeSlot(startDate, endDate);
    }

    public Long durationInDays() {
        LocalDateTime start = this.start.toLocalDate().atStartOfDay();
        LocalDateTime end = this.end.toLocalDate().atStartOfDay();
        return ChronoUnit.DAYS.between(start, end);
    }
}
