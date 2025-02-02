package org.noteam.be.image.repository;

import org.noteam.be.image.domain.CanvasImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CanvasImageRepository extends JpaRepository<CanvasImage, Long> {
    Optional<CanvasImage> getCanvasImageByFileName(String fileName);
}
