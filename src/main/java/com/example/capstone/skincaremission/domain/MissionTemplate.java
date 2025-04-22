package com.example.capstone.skincaremission.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "mission_template")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MissionTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 10)
    private String category;

    @Column(nullable = false, length = 10)
    private String score;

    @Column(nullable = false, length = 50)
    private String mission;
}
