package com.example.capstone.aidisease.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "disease_treatment")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class DiseaseTreatment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 45, nullable = false)
    private String disease;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String treatment;

    @Column(length = 10, nullable = false)
    private String source;

    @Column(name = "image_url", length = 1000, nullable = false)
    private String imageUrl;
}
