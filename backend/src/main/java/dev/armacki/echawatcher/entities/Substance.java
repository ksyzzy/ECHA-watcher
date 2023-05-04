package dev.armacki.echawatcher.entities;

import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.Set;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "substance")
public class Substance {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private @Id long id;

    @Nonnull
    @Column(nullable = false)
    private String index;

    @Nonnull
    @Column(nullable = false)
    private String name;

    @Nonnull
    @Column(name = "ec_no", nullable = false)
    private String ecNo;

    @Nonnull
    @Column(name = "cas_no", nullable = false)
    private String casNo;

    @Column(name = "creation_date")
    private Timestamp creationDate;

    @Column(name = "update_date")
    private Timestamp updateDate;

    @OneToMany
    private Set<SubstanceHazard> substanceHazardList;
}
