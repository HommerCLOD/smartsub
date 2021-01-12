package ua.smartsub.smartsub.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Entity
public class Subscribe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotBlank
    private String bot_url;
    @NotNull
    private int price;
    @NotBlank
    private String description;
    private String bot_image;
    @NotBlank
    private String bot_name;

    @ManyToOne(fetch = FetchType.LAZY)
    private Subs_tokens Subs_tokens;

}
