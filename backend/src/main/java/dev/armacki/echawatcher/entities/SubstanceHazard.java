package dev.armacki.echawatcher.entities;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@EqualsAndHashCode
@Entity
public class SubstanceHazard {

    @EmbeddedId
    private SubstanceHazardKey id;

    @ManyToOne
    @MapsId("substanceIndex")
    @JoinColumn(name = "substance_index")
    private Substance substance;

    @ManyToOne
    @MapsId("hazardId")
    @JoinColumn(name = "hazard_id")
    private Hazard hazard;

    @Column(name = "creation_date")
    private Timestamp creationDate;

    @Column(name = "update_date")
    private Timestamp updateDate;
}
