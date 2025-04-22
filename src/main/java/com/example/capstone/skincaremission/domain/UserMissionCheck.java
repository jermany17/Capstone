package com.example.capstone.skincaremission.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "user_mission_check")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserMissionCheck {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(nullable = false)
    private LocalDate date;

    @Column(name = "mission1_done")
    private Boolean mission1Done;

    @Column(name = "mission2_done")
    private Boolean mission2Done;

    @Column(name = "mission3_done")
    private Boolean mission3Done;

    @Column(name = "mission4_done")
    private Boolean mission4Done;

    @Column(name = "mission5_done")
    private Boolean mission5Done;

    @Column(name = "done_count")
    private Integer doneCount;
}
