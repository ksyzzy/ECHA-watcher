package dev.armacki.echawatcher.entities;

import jakarta.annotation.Nonnull;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class SubstanceHazardKey implements Serializable {

    @Column(name = "substance_index", nullable = false)
    private long substanceIndex;

    @Column(name = "hazard_id", nullable = false)
    private long hazardId;
}
