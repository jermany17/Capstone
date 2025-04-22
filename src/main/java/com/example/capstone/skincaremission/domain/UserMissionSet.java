package com.example.capstone.skincaremission.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_mission_set")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserMissionSet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "mission1_id", nullable = false)
    private Long mission1Id;

    @Column(name = "mission2_id", nullable = false)
    private Long mission2Id;

    @Column(name = "mission3_id", nullable = false)
    private Long mission3Id;

    @Column(name = "mission4_id", nullable = false)
    private Long mission4Id;

    @Column(name = "mission5_id", nullable = false)
    private Long mission5Id;
}
