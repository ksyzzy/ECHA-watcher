package dev.armacki.echawatcher.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.Set;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Entity
@Table(name = "substance", indexes = @Index(name = "idx_substance_index", columnList = "index"))
public class Substance {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private @Id long id;

    private String index;
    private String name;

    @Column(name = "ec_no")
    private String ecNo;

    @Column(name = "cas_no")
    private String casNo;

    @Column(name = "creation_date")
    private Timestamp creationDate;

    @Column(name = "update_date")
    private Timestamp updateDate;

    @OneToMany
    private Set<SubstanceHazard> substanceHazardList;
}
