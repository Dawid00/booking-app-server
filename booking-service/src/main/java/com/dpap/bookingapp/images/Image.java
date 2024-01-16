package com.dpap.bookingapp.images;

import jakarta.persistence.*;

@Entity
@Table(name = "images")
public class Image {

    @Id
    @SequenceGenerator(name = "images_id_seq", sequenceName = "seq_images", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "images_id_seq")
    private Long id;
    private String url;
    private Long placeId;

    public Image() {
    }

    public Image(String url, Long placeId) {
        this.url = url;
        this.placeId = placeId;
    }

    public Long getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public Long getPlaceId() {
        return placeId;
    }

}
