package ua.smartsub.smartsub.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Entity
public class Comments {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String text;
    @Min(0)
    @Max(5)
    private int rating;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Subscribe subscribe;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private User user;

}
