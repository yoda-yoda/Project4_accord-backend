package org.noteam.be.image.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CanvasImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "canvas_image_id", nullable = false)
    private Long id;

    @Column(nullable = false)
    private String canvasId;
    @Column(nullable = false, unique = true)
    private String fileName;
    @Column(nullable = false, unique = true)
    private String imageUrl;
    @Column(nullable = false)
    private LocalDateTime createdAt;
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    @Builder
    public CanvasImage(String canvasId, String fileName, String imageUrl) {
        this.canvasId = canvasId;
        this.fileName = fileName;
        this.imageUrl = imageUrl;
    }
}
