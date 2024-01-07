package com.dpap.bookingapp.reviews;

import com.dpap.bookingapp.users.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserService userService;

    public ReviewService(ReviewRepository reviewRepository, UserService userService) {
        this.reviewRepository = reviewRepository;
        this.userService = userService;
    }

    public void createReview(String content, Long placeId, Long userId, int rating) {
        Review review = new Review(content, rating, placeId, userId, LocalDateTime.now());
        reviewRepository.save(review);
    }

    public List<ReviewDto> getReviewDtosForPlace(Long placeId) {
        return reviewRepository.findAllByPlaceId(placeId)
                .stream().map(review -> new ReviewDto(
                        review.getRating(),
                        getEmailFromUser(review.getUserId()),
                        review.getContent(),
                        review.getPlaceId(),
                        review.getAt().toString()
                ))
                .toList();
    }

    private String getEmailFromUser(Long userId) {
        return userService.fetchEmailByUserId(userId);
    }

    @Transactional
    public void deleteReview(Long userId, Long reviewId) {
        if (reviewBelongsToUser(reviewId, userId))
            reviewRepository.deleteById(reviewId);
    }

    private boolean reviewBelongsToUser(Long reviewId, Long userId) {
        return findByIdAndUserId(reviewId, userId) != null;
    }

    private Review findByIdAndUserId(Long reviewId, Long userId) {
        return reviewRepository.findByIdAndUserId(reviewId, userId).orElseThrow(() -> new RuntimeException("Not found review with id:" + reviewId));
    }

}
