package dev.armacki.echawatcher.entities;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.sql.Timestamp;

@Getter @Setter
@Entity
public class SubstanceHazard implements Serializable {

    @EmbeddedId
    private SubstanceHazardKey id = new SubstanceHazardKey();

    @ManyToOne
    @MapsId("substanceId")
    @JoinColumn(name = "substance_id")
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
