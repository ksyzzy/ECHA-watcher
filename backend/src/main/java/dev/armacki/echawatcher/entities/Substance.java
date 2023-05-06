package dev.armacki.echawatcher.entities;

import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Set;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "substance")
public class Substance implements Serializable {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private @Id long id;

    @Column(nullable = false)
    private String index;

    @Column(nullable = false)
    private String name;

    @Column(name = "ec_no", nullable = false)
    private String ecNo;

    @Column(name = "cas_no", nullable = false)
    private String casNo;

    @Column(name = "creation_date")
    private Timestamp creationDate;

    @Column(name = "update_date")
    private Timestamp updateDate;

    @OneToMany
    private Set<SubstanceHazard> substanceHazardList;
}
