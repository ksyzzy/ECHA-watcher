package dev.armacki.echawatcher.entities;

import jakarta.annotation.Nonnull;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class SubstanceHazardKey implements Serializable {

    @Nonnull
    @Column(name = "substance_index", nullable = false)
    private long substanceIndex;

    @Nonnull
    @Column(name = "hazard_id", nullable = false)
    private long hazardId;
}
