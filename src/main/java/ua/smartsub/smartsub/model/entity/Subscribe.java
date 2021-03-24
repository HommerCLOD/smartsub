package ua.smartsub.smartsub.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Set;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Entity
public class Subscribe  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private long id;

    @NotBlank
    @Column(name = "BOT_URL")
    private String url;

    @Column(name = "PRICE")
    private Double price;

    @NotBlank
    @Column(name = "DESCRIPTION")
    private String description;

    @NotBlank
    @Column(name = "BOT_IMAGE")
    private String image;

    @NotBlank
    @Column(name = "BOT_NAME")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    private Subs_tokens Subs_tokens;

    @ManyToMany(fetch = FetchType.LAZY)
    private Set<Comments> comments;

}
