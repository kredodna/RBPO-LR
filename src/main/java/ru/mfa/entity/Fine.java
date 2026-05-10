package ru.mfa.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "fines")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Fine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "reader_id", nullable = false)
    @JsonIgnore
    private Reader reader;

    @OneToOne
    @JoinColumn(name = "book_loan_id")
    @JsonIgnore
    private BookLoan bookLoan;

    @Column(nullable = false)
    private Double amount;

    @Column(nullable = false, length = 50)
    private String reason;

    @Column(name = "issued_date", nullable = false)
    private LocalDate issuedDate;

    @Column(name = "paid_date")
    private LocalDate paidDate;

    @Column(length = 20)
    private String status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        issuedDate = LocalDate.now();
        status = "UNPAID";
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public boolean isPaid() {
        return "PAID".equals(status);
    }

    public void pay() {
        this.status = "PAID";
        this.paidDate = LocalDate.now();
    }
}