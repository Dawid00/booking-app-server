package com.dpap.bookingapp.reporting;

import com.dpap.bookingapp.reservation.ReservationService;
import com.dpap.bookingapp.reservation.dto.SearchReservationFilter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/reports")
@Tag(name = "Reports")
@CrossOrigin("http://localhost:4200")

public class ReportController {

    private final ReservationService reservationService;
    private final ReportService reportService;

    public ReportController(ReservationService reservationService) {
        this.reservationService = reservationService;
        this.reportService = new ReportService();
    }

    @PostMapping
    public ResponseEntity<Resource> createReports(@RequestBody GenerateReportRequest generateReportRequest) throws MalformedURLException {
        var reservations = reservationService
                .findReservations(SearchReservationFilter.Builder.newBuilder()
                        .placeId(generateReportRequest.getPlaceId())
                        .from(generateReportRequest.getFrom()).to(generateReportRequest.getTo()).build());
        var fileName = createFileName(generateReportRequest.getPlaceId());
        reportService.createReport(reservations, fileName);
        Path path = Paths.get(fileName);
        Resource resource = new org.springframework.core.io.FileUrlResource(path + ".json");

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .body(resource);
    }
    private String createFileName(Long placeId) {
        return "place_" + placeId.toString() + "_reservations";
    }
}
