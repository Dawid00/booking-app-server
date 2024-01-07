package com.dpap.bookingapp.reviews;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "reviews")
public class Review {

    @Id
    @SequenceGenerator(name = "reviews_id_seq", sequenceName = "seq_reviews", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "reviews_id_seq")
    private Long id;
    private String content;
    private Long placeId;
    private Long userId;
    private int rating;
    private LocalDateTime at;

    public LocalDateTime getAt() {
        return at;
    }

    public void setAt(LocalDateTime at) {
        this.at = at;
    }

    public Review(String content, int rating, Long placeId, Long userId, LocalDateTime at) {
        this.content = content;
        this.at = at;
        this.placeId = placeId;
        this.rating = rating;
        this.userId = userId;
    }

    public Review() {
    }

    public Long getId() {
        return id;
    }


    public int getRating() {
        return rating;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getPlaceId() {
        return placeId;
    }

    public void setPlaceId(Long placeId) {
        this.placeId = placeId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
