package ua.smartsub.smartsub.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Entity
public class Subscribe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false)
    private String bot_url;
    @Column(nullable = false)
    private int price;
    @Column(nullable = false)
    private String description;
    private String bot_image;
    @Column(nullable = false)
    private String bot_name;
    @Column(unique=true)
    private int id_Subs_tokens;

}
