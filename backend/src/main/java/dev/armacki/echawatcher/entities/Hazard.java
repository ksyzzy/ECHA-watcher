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
@Table(name = "hazard")
@Entity
public class Hazard implements Serializable {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private @Id long id;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(unique = true)
    private String code;

    @Column(name = "creation_date")
    private Timestamp creationDate;

    @Column(name = "update_date")
    private Timestamp updateDate;

    @OneToMany(mappedBy = "hazard")
    private Set<SubstanceHazard> substanceHazardList;

}
