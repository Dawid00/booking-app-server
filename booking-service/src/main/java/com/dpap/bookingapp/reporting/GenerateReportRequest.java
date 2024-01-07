package com.dpap.bookingapp.reporting;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;

class GenerateReportRequest {

    private final Long placeId;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private final LocalDateTime from;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private final LocalDateTime to;

    public GenerateReportRequest(Long placeId, LocalDateTime from, LocalDateTime to) {
        this.placeId = placeId;
        this.from = from;
        this.to = to;
    }

    public Long getPlaceId() {
        return placeId;
    }
    public LocalDateTime getFrom() {
        return from;
    }
    public LocalDateTime getTo() {
        return to;
    }

}
