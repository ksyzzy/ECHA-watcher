package dev.armacki.echawatcher.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

@Getter @Setter
@NoArgsConstructor
public class SubstanceDTO implements Serializable {

    private String index;
    private String name;
    private String ecNo;
    private String casNo;
    private Date creationDate;
    private Date updateDate;

    public SubstanceDTO(String index, String name, String ecNo, String casNo,
                        Timestamp creationDate, Timestamp updateDate)
    {
        this.index = index;
        this.name = name;
        this.ecNo = ecNo;
        this.casNo = casNo;
        this.creationDate = creationDate != null ? new Date(creationDate.getTime()) : null;
        this.updateDate = updateDate != null ? new Date(updateDate.getTime()) : null;
    }
}
