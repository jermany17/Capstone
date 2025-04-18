package com.example.capstone.aidisease.domain;

import com.example.capstone.community.domain.StringListConverter;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "disease_detail")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class DiseaseDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 45, nullable = false, unique = true)
    private String disease;

    @Convert(converter = StringListConverter.class)
    @Column(name = "image_urls", columnDefinition = "TEXT", nullable = false)
    private List<String> imageUrls = new ArrayList<>();

    @Column(columnDefinition = "TEXT", nullable = false)
    private String definition;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String cause;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String symptom;

    @Column(length = 10, nullable = false)
    private String source;

    @Builder
    public DiseaseDetail(String disease, List<String> imageUrls, String definition, String cause, String symptom, String source) {
        this.disease = disease;
        this.imageUrls = imageUrls;
        this.definition = definition;
        this.cause = cause;
        this.symptom = symptom;
        this.source = source;
    }
}
