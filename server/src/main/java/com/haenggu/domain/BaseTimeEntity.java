package com.haenggu.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

/**
 * BaseTimeEntity class is automatically managing
 * createdDate and modifiedDate of all entities.
 * @author Jiyoon Bak
 */
@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseTimeEntity {

    @CreatedDate
    @Column(name = "created_dt")
    @JsonProperty(value = "created_date")
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(name = "modified_dt")
    @JsonProperty(value = "modified_date")
    private LocalDateTime modifiedDate;
}
