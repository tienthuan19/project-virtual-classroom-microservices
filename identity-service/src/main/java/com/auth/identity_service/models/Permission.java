package com.auth.identity_service.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "permissions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Permission extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Hình để int, nên dùng Identity
    private Integer id;

    @Column(nullable = false, unique = true)
    private String name; // Ví dụ: "CREATE_CLASS", "DELETE_USER"

    private String description;
}
