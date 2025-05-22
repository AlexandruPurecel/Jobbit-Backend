package com.backend.jobbit.persistence;

import com.backend.jobbit.persistence.model.Job;
import com.backend.jobbit.persistence.model.User;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "image_data")
public class ImageData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String type;

    @Lob
    @Column(name = "imagedata", columnDefinition = "LONGBLOB")
    private byte[] imageData;

    @OneToOne(mappedBy = "profileImage")
    private User user;

    @ManyToOne
    @JoinColumn(name = "job_id")
    private Job job;

}
