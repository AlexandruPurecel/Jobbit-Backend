package com.backend.jobbit.persistence.model;

import com.backend.jobbit.enums.Cities;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "locations")
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "location_id")
    private Long locationId;

    @Enumerated(EnumType.STRING)
    @Column(name = "city")
    private Cities city;

    @Column(name = "street_name")
    private String streetName;

    @Column(name = "street_number")
    private Long streetNumber;

    @OneToMany(mappedBy = "location")
    private List<Job> jobs = new ArrayList<>();



}
