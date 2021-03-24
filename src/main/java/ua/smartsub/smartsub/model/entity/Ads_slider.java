package ua.smartsub.smartsub.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@Entity
@NoArgsConstructor
@Data
public class Ads_slider {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id", updatable=false, nullable=false)
    private Long id;

    @NotBlank
    private String web_url;

    @NotBlank
    private String image_url;
}
