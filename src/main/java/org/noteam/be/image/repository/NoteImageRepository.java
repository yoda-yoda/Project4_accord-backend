package org.noteam.be.image.repository;

import org.noteam.be.image.domain.NoteImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NoteImageRepository extends JpaRepository<NoteImage, Long> {
    Optional<NoteImage> getNoteImageByFileName(String fileName);
}
