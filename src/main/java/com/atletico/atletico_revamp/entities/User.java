package com.atletico.atletico_revamp.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter // Lombok will generate getters for all fields
@Setter // Lombok will generate setters for all fields
@Builder // Lombok automatically generates the builder pattern
@NoArgsConstructor
@AllArgsConstructor
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false, unique = true, length = 255)
    private String email;

    @Column(nullable = false, length = 60)
    private String password;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private int age;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;

    @Column(nullable = false)
    private int height;

    @Column(name = "initial_weight", nullable = false)
    private BigDecimal initialWeight;

    @Column(nullable = false, length = 50)
    private String firstName;

    @Column(nullable = false, length = 50)
    private String lastName;

    @Enumerated(EnumType.STRING)
    private Lifestyle lifestyle;

    @Enumerated(EnumType.STRING)
    private WeightGoal weightGoal;

    public enum Gender {
        MALE, FEMALE
    }

    public enum Lifestyle {
        SEDENTARY, LIGHTLY_ACTIVE, ACTIVE, VERY_ACTIVE
    }

    public enum WeightGoal {
        MAINTAIN, LOSE, GAIN
    }

    public enum PreferredUnit {
        METRIC, IMPERIAL
    }
}
