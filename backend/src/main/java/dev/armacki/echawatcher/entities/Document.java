package dev.armacki.echawatcher.entities;

import jakarta.persistence.*;
import lombok.*;

@Getter @Setter
@Entity
@Table(name = "document_file")
public class Document {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private @Id long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "md5_checksum", nullable = false)
    private String md5Checksum;

    @Column(nullable = false)
    private long version;

    @Column(name = "changeset_name")
    private String changesetName;

    @Column(name = "is_actual")
    private boolean isActual;

    public Document(String name, String md5Checksum, long version) {
        this.name = name;
        this.md5Checksum = md5Checksum;
        this.version = version;
    }
}
