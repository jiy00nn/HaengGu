package com.haenggu.domain.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.minidev.json.annotate.JsonIgnore;

import javax.persistence.*;
import java.util.UUID;

@Getter
@Entity @Table
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

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "user_id")
    private Users user;

    @Builder
    public UserImage(String mimetype, String originalName, Long size, byte[] data, Users user) {
        this.mimetype = mimetype;
        this.originalName = originalName;
        this.size = size;
        this.data = data;
        this.user = user;
    }
}
