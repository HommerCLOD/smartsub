package ua.smartsub.smartsub.model.entity;


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
    @Column(name="id", updatable=false, nullable=false)
    private Long id;

    @NotBlank
    @Column(name="text", nullable=false)
    private String text;

    @Min(0)
    @Max(5)
    @Column(name="rating")
    private int rating;

    @Column(name = "IS_COMMENT_VERIFIED", nullable = false)
    private Boolean commentVerified;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Subscribe subscribe;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private User user;

}
