package com.example.capstone.aidisease.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "hospital")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Hospital {

    @Id
    @Column(length = 10)
    private String location;

    @Column(name = "hospital1", nullable = false)
    private String hospital1;

    @Column(name = "hospital1_link", nullable = false)
    private String hospital1Link;

    @Column(name = "hospital2", nullable = false)
    private String hospital2;

    @Column(name = "hospital2_link", nullable = false)
    private String hospital2Link;

    @Column(name = "hospital3", nullable = false)
    private String hospital3;

    @Column(name = "hospital3_link", nullable = false)
    private String hospital3Link;

    @Builder
    public Hospital(String location, String hospital1, String hospital1Link,
                    String hospital2, String hospital2Link,
                    String hospital3, String hospital3Link) {
        this.location = location;
        this.hospital1 = hospital1;
        this.hospital1Link = hospital1Link;
        this.hospital2 = hospital2;
        this.hospital2Link = hospital2Link;
        this.hospital3 = hospital3;
        this.hospital3Link = hospital3Link;
    }
}
