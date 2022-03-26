package com.taras.arenda.jpa.domain;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "ROOM")
@Getter
@Setter
@EqualsAndHashCode
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Room implements Serializable, Comparable<Room> {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "HOTEL", referencedColumnName = "ID")
    private Hotel hotel;

    @Column(name = "NAME")
    private String name;

    @Column(name = "NUMBER_PEOPLE")
    private int numberPeople;

    @Column(name = "NUMBER_BEDS")
    private int numberBeds;

    @Column(name = "NUMBER_ROOMS")
    private int numberRooms;

    @Column(name = "PRICE")
    private int price;

    @Column(name = "CREATE_DATE")
    @Builder.Default
    private LocalDateTime createDate = LocalDateTime.now();


    @Override
    public int compareTo(Room o) {
        if (getId() == null || o == null || o.getId() == null) return 0;
        return getId().compareTo(o.getId());
    }
}
