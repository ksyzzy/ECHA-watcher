package dev.armacki.echawatcher.dtos;

import dev.armacki.echawatcher.entities.Hazard;
import dev.armacki.echawatcher.entities.Substance;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

@Getter @Setter
@NoArgsConstructor
public class SubstanceHazardDTO implements Serializable {

    private String substanceIndex;
    private long hazardId;
    private Date creationDate;
    private Date updateDate;

    public SubstanceHazardDTO(Substance substance, Hazard hazard, Timestamp creationDate, Timestamp updateDate) {
        this.substanceIndex = substance != null ? substance.getIndex() : null;
        this.hazardId = hazard != null ? hazard.getId() : null;
        this.creationDate = creationDate != null ? new Date(creationDate.getTime()) : null;
        this.updateDate = updateDate != null ? new Date(updateDate.getTime()) : null;
    }
}
