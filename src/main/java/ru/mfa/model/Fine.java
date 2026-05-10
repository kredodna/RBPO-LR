package ru.mfa.model;

import java.time.LocalDate;
import java.util.Objects;

public class Fine {
    private Long id;
    private Long readerId;
    private Long bookLoanId;
    private double amount;
    private String reason;
    private LocalDate issuedDate;
    private LocalDate paidDate;
    private String status;

    public Fine() {
        this.issuedDate = LocalDate.now();
        this.status = "UNPAID";
    }

    public Fine(Long id, Long readerId, Long bookLoanId, double amount, String reason) {
        this.id = id;
        this.readerId = readerId;
        this.bookLoanId = bookLoanId;
        this.amount = amount;
        this.reason = reason;
        this.issuedDate = LocalDate.now();
        this.status = "UNPAID";
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getReaderId() { return readerId; }
    public void setReaderId(Long readerId) { this.readerId = readerId; }

    public Long getBookLoanId() { return bookLoanId; }
    public void setBookLoanId(Long bookLoanId) { this.bookLoanId = bookLoanId; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public LocalDate getIssuedDate() { return issuedDate; }

    public LocalDate getPaidDate() { return paidDate; }
    public void setPaidDate(LocalDate paidDate) { this.paidDate = paidDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public boolean isPaid() {
        return "PAID".equals(status);
    }

    public void pay() {
        this.status = "PAID";
        this.paidDate = LocalDate.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Fine fine = (Fine) o;
        return Objects.equals(id, fine.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}