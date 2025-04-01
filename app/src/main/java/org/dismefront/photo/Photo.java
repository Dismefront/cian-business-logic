package org.dismefront.photo;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.dismefront.publicatoin.Publication;

@Data
@Entity
@Table(name = "blps_photo")
public class Photo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "s3_key", nullable = false)
    private String s3Key;

    @ManyToOne
    @JoinColumn(name = "publication_id")
    @JsonBackReference
    private Publication publication;
}