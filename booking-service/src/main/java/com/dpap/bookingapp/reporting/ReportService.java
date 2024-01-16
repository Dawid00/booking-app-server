package com.dpap.bookingapp.reporting;

import com.dpap.bookingapp.reservation.Reservation;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileWriter;
import java.io.Writer;
import java.util.List;

class ReportService {

    final Logger logger = LoggerFactory.getLogger(ReportService.class);

    public void createReport(List<Reservation> reservations, String reportName) {
        if (!reservations.isEmpty()) {
            try (Writer writer = new FileWriter(reportName + ".json")) {
                Gson gson = new GsonBuilder().create();
                gson.toJson(reservations, writer);
                logger.info("Report has been successfully produced");
                writer.flush();
            } catch (Exception ex) {
                logger.error("Generating report has finished with errors. Your report is not generated.");
            }
        } else {
            throw new RuntimeException("Chosen place has no reservations in that period.");
        }
    }
}