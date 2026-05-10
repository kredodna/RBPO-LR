package ru.mfa.model;

import java.time.LocalDate;
import java.util.Objects;

public class BookLoan {
    private Long id;
    private Long bookId;
    private Long readerId;
    private LocalDate loanDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private String status;

    public BookLoan() {
        this.loanDate = LocalDate.now();
        this.dueDate = loanDate.plusDays(14);
        this.status = "ACTIVE";
    }

    public BookLoan(Long id, Long bookId, Long readerId) {
        this.id = id;
        this.bookId = bookId;
        this.readerId = readerId;
        this.loanDate = LocalDate.now();
        this.dueDate = loanDate.plusDays(14);
        this.status = "ACTIVE";
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getBookId() { return bookId; }
    public void setBookId(Long bookId) { this.bookId = bookId; }

    public Long getReaderId() { return readerId; }
    public void setReaderId(Long readerId) { this.readerId = readerId; }

    public LocalDate getLoanDate() { return loanDate; }
    public void setLoanDate(LocalDate loanDate) { this.loanDate = loanDate; }

    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }

    public LocalDate getReturnDate() { return returnDate; }
    public void setReturnDate(LocalDate returnDate) { this.returnDate = returnDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public boolean isOverdue() {
        return returnDate == null && LocalDate.now().isAfter(dueDate);
    }

    public long getDaysOverdue() {
        if (isOverdue()) {
            return LocalDate.now().toEpochDay() - dueDate.toEpochDay();
        }
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookLoan bookLoan = (BookLoan) o;
        return Objects.equals(id, bookLoan.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}