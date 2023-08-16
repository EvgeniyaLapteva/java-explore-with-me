package ru.practicum.ewm.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "locations")
@Getter
@Setter
@ToString
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "lat", nullable = false)
    private Float lat;

    @Column(name = "lon", nullable = false)
    private Float lon;
}
