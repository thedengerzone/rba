package com.denger.rba.entity.user;

import com.denger.rba.entity.card.CardInformation;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
@EqualsAndHashCode
@Builder
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq_id")
    @SequenceGenerator(name = "user_seq_id", sequenceName = "user_seq", allocationSize = 1)
    @JsonIgnore
    private Long id;

    @Column(name = "name")
    @NotNull
    private String name;

    @Column(name = "surname")
    @NotNull
    private String surname;

    @Column(name = "oib", length = 11, unique = true, nullable = false)
    private String oib;

    @JsonIgnore
    @OneToOne(cascade = {CascadeType.DETACH})
    @JoinColumn(name = "card_id", referencedColumnName = "id")
    private CardInformation cardInformation;

}
