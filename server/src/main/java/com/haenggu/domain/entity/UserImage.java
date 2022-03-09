package com.haenggu.domain.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.minidev.json.annotate.JsonIgnore;

import javax.persistence.*;
import java.util.UUID;

@Table
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserImage {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "image_id")
    private UUID imageId;

    private String mimetype;

    @Column(name = "original_name")
    private String originalName;

    private Long size;

    @JsonIgnore
    private byte[] data;

    @OneToOne(mappedBy = "image")
    private Users user;
}
