package ua.smartsub.smartsub.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;


@AllArgsConstructor
@Data
@NoArgsConstructor
@Entity
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ROLE_ID", updatable = false, nullable = false)
    private Long id;

    @NotBlank
    @Column(name = "ROLE_NAME", unique = true, nullable = false)
    private String name;
}
