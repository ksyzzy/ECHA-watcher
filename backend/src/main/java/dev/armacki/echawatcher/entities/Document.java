package dev.armacki.echawatcher.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Entity
@Table(name = "document_file")
public class Document implements Serializable {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private @Id long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "md5_checksum", nullable = false)
    private String md5Checksum;

    @Column(nullable = false)
    private long version;
}
