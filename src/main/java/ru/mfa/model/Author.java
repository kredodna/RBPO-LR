package ru.mfa.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Author {
    private Long id;
    private String firstName;
    private String lastName;
    private List<Long> bookIds;

    public Author() {
        this.bookIds = new ArrayList<>();
    }

    public Author(Long id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.bookIds = new ArrayList<>();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public List<Long> getBookIds() { return bookIds; }
    public void setBookIds(List<Long> bookIds) { this.bookIds = bookIds; }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public void addBook(Long bookId) {
        if (!bookIds.contains(bookId)) {
            bookIds.add(bookId);
        }
    }

    public void removeBook(Long bookId) {
        bookIds.remove(bookId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Author author = (Author) o;
        return Objects.equals(id, author.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
