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
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotBlank
    @Column( unique=true)
    private String token;
    @NotBlank
    private String type;
    //    @NotBlank
    //    @Column( unique=true)
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;


}
