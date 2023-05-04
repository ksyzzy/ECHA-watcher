package dev.armacki.echawatcher.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class SubstanceHazardKey implements Serializable {

    @Column(name = "substance_index")
    private long substanceIndex;

    @Column(name = "hazard_id")
    private long hazardId;
}
