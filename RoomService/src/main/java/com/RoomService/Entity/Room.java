package com.RoomService.Entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.List;

@Data
@Entity
@Table(name = "Rooms")
public class Room {

    @Id
    private String roomId;

    @Column(nullable = false)
    private String roomName;

    private String roomDescription;

    @Column(nullable = false)
    private String creatorName;

    @Enumerated(EnumType.STRING)
    private RoomStatus status; // ACTIVE, CLOSED

    private LocalDateTime createdAt;

    @ElementCollection
    private List<String> participants;

    public enum RoomStatus {
        ACTIVE, CLOSED
    }

    @PrePersist
    public void generateId() {
        this.roomId = UUID.randomUUID().toString().replace("-", "").substring(0, 10);
        this.createdAt = LocalDateTime.now(); // also set created time
        if (this.status == null) {
            this.status = RoomStatus.ACTIVE;
        }
    }
}
