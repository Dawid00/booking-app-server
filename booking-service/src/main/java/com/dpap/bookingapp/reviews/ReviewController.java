package com.dpap.bookingapp.reviews;

import com.dpap.bookingapp.auth.AuthenticationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reviews")
@Tag(name = "Reviews")
@CrossOrigin("http://localhost:4200")

public class ReviewController {

    private final ReviewService reviewService;
    private final AuthenticationService authenticationService;

    public ReviewController(ReviewService reviewService, AuthenticationService authenticationService) {
        this.reviewService = reviewService;
        this.authenticationService = authenticationService;
    }

    @PostMapping
    public ResponseEntity<?> createReview(@RequestBody CreateReviewDto createReviewDto) {
        reviewService.createReview(
                createReviewDto.content(),
                createReviewDto.placeId(),
                authenticationService.getLoggedUser().id(),
                createReviewDto.rating()
        );
        return ResponseEntity.status(201).build();
    }

    @GetMapping("/places/{placeId}")
    public ResponseEntity<List<ReviewDto>> getReviews(@PathVariable Long placeId) {
        return ResponseEntity.ok(reviewService.getReviewDtosForPlace(placeId));
    }

    @DeleteMapping
    public ResponseEntity<?> deleteReview(@RequestParam Long reviewId) {
        reviewService.deleteReview(authenticationService.getLoggedUser().id(), reviewId);
        return ResponseEntity.noContent().build();
    }
}
