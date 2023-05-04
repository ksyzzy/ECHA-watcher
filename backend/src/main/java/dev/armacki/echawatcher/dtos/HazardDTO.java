package dev.armacki.echawatcher.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

@Getter @Setter
@NoArgsConstructor
public class HazardDTO implements Serializable {

    private String name;
    private String code;
    private Date creationDate;
    private Date updateDate;

    public HazardDTO(String name, String code, Timestamp creationDate, Timestamp updateDate) {
        this.name = name;
        this.code = code;
        this.creationDate = creationDate != null ? new Date(creationDate.getTime()) : null;
        this.updateDate = updateDate != null ? new Date(updateDate.getTime()) : null;
    }
}
