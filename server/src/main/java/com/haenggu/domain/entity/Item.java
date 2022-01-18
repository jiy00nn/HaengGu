package com.haenggu.domain.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@NoArgsConstructor
@Entity
@Table
public class Item implements Serializable {
    @javax.persistence.Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long Id;

    @Column
    private String name;

    @Column
    private int price;

    @Builder
    public Item(String name, int price) {
        this.name = name;
        this.price = price;
    }

    public void update(Item item) {
        this.name = item.name;
        this.price = item.price;
    }
}
