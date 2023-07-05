package com.denger.rba.entity.card;

import com.denger.rba.entity.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@Table(name = "cards")
public class CardInformation implements Serializable {


    public CardInformation() {
        this.status = true;
        this.createdAt = LocalDateTime.now();
        this.lastModified = LocalDateTime.now();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "card_seq_id")
    @SequenceGenerator(name = "card_seq_id", sequenceName = "card_seq", allocationSize = 1)
    private Long id;

    @Column(name ="status")
    @NotNull
    private boolean status;

    @Column(name = "oib")
    @NotNull
    private String oib;
    @Column(name = "created_at")
    @NotNull
    private LocalDateTime createdAt;

    @Column(name = "last_modified")
    @NotNull
    private LocalDateTime lastModified;


    @OneToOne(mappedBy = "cardInformation", cascade = {CascadeType.DETACH})
    private User user;

}
