package org.noteam.be.system.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TimeAgoUtil {

    public static String formatElapsedTime(LocalDateTime updatedAt) {
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(updatedAt, now);

        if (duration.toMinutes() < 1) {
            return "Just now";
        } else if (duration.toHours() < 1) {
            long minutes = duration.toMinutes();
            return minutes + " minutes ago";
        } else if (duration.toDays() < 1) {
            long hours = duration.toHours();
            return hours + " hours ago";
        } else {
            long days = duration.toDays();
            return days + " days ago";
        }
    }

}