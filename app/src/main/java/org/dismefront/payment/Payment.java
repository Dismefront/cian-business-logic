package org.dismefront.payment;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.dismefront.publicatoin.Publication;

import java.util.Date;

@Data
@Entity
@Table(name = "blps_payment")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @Column(name = "amount", nullable = false)
    private Double amount;

    @Column(name = "subscription_type", nullable = false)
    private String subscriptionType;

    @Column(name = "due_date", nullable = false)
    private Date dueDate;

    @Column(name = "start_date", nullable = false)
    private Date startDate;

    @ManyToOne
    @JoinColumn(name = "publication_id")
    @JsonBackReference
    private Publication publication;
}
