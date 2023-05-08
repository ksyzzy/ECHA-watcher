package dev.armacki.echawatcher.entities;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@Getter @Setter
@Entity
public class Substance implements Serializable {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private @Id Long id;

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

    @OneToMany(mappedBy = "substance")
    private Set<SubstanceHazard> substanceHazards = new HashSet<>();
}
