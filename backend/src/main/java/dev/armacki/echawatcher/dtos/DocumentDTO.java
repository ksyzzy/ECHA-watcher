package dev.armacki.echawatcher.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class DocumentDTO implements Serializable {

    private String name;
    private String md5Checksum;
    private long version;
}
