package ru.mfa.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Reader {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private LocalDate registrationDate;
    private List<Long> bookLoanIds;
    private List<Long> fineIds;
    private boolean isBlocked;

    public Reader() {
        this.bookLoanIds = new ArrayList<>();
        this.fineIds = new ArrayList<>();
        this.registrationDate = LocalDate.now();
        this.isBlocked = false;
    }

    public Reader(Long id, String firstName, String lastName, String email, String phone) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.bookLoanIds = new ArrayList<>();
        this.fineIds = new ArrayList<>();
        this.registrationDate = LocalDate.now();
        this.isBlocked = false;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public LocalDate getRegistrationDate() { return registrationDate; }

    public List<Long> getBookLoanIds() { return bookLoanIds; }
    public void setBookLoanIds(List<Long> bookLoanIds) { this.bookLoanIds = bookLoanIds; }

    public List<Long> getFineIds() { return fineIds; }
    public void setFineIds(List<Long> fineIds) { this.fineIds = fineIds; }

    public boolean isBlocked() { return isBlocked; }
    public void setBlocked(boolean blocked) { isBlocked = blocked; }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public void addBookLoan(Long loanId) {
        bookLoanIds.add(loanId);
    }

    public void addFine(Long fineId) {
        fineIds.add(fineId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reader reader = (Reader) o;
        return Objects.equals(id, reader.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}