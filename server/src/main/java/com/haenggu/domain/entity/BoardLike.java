package com.haenggu.domain.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

@Schema(description = "동행글 좋아요")
@Getter
@Entity
@Table(name = "board_like", uniqueConstraints = {@UniqueConstraint(name = "board_user_uk", columnNames = {"user_id", "board_id"})})
@NoArgsConstructor
public class BoardLike {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "board_like_id")
    @Schema(description = "동행글 좋아요 아이디", nullable = false)
    private UUID boardLikeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users user;

    @Builder
    public BoardLike(Board board, Users user) {
        this.board = board;
        this.user = user;
    }
}
