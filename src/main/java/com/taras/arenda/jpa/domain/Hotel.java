package com.taras.arenda.jpa.domain;


import lombok.*;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "HOTEL")
@Getter
@Setter
@EqualsAndHashCode
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Where(clause = "deleted = false")
public class Hotel implements Serializable, Comparable<Hotel> {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "CITY", referencedColumnName = "ID")
    private City city;

    @Column(name = "NAME")
    private String name;

    @Column(name = "ABOUT")
    private String about;

    @Column(name = "PRICE")
    private int price;

    @Column(name = "CREATE_DATE")
    @Builder.Default
    private LocalDateTime createDate = LocalDateTime.now();

    @Column(name="DELETED")
    private boolean deleted;

    @Override
    public int compareTo(Hotel o) {
        if (getId() == null || o == null || o.getId() == null) return 0;
        return getId().compareTo(o.getId());
    }
}
