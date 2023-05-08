package dev.armacki.echawatcher.entities;

import jakarta.annotation.Nonnull;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Data
@Embeddable
public class SubstanceHazardKey implements Serializable {

    @Column(name = "substance_id")
    private Long substanceId;

    @Column(name = "hazard_id")
    private Long hazardId;
}
