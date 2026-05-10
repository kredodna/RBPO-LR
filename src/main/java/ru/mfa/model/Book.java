package ru.mfa.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Book {
    private Long id;
    private String title;
    private Integer publicationYear;
    private List<Long> authorIds;
    private Integer totalCopies;
    private Integer availableCopies;

    public Book() {
        this.authorIds = new ArrayList<>();
    }

    public Book(Long id, String title, Integer publicationYear, Integer totalCopies) {
        this.id = id;
        this.title = title;
        this.publicationYear = publicationYear;
        this.totalCopies = totalCopies;
        this.availableCopies = totalCopies;
        this.authorIds = new ArrayList<>();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public Integer getPublicationYear() { return publicationYear; }
    public void setPublicationYear(Integer publicationYear) { this.publicationYear = publicationYear; }

    public List<Long> getAuthorIds() { return authorIds; }
    public void setAuthorIds(List<Long> authorIds) { this.authorIds = authorIds; }

    public Integer getTotalCopies() { return totalCopies; }
    public void setTotalCopies(Integer totalCopies) {
        this.totalCopies = totalCopies;
        this.availableCopies = totalCopies;
    }

    public Integer getAvailableCopies() { return availableCopies; }
    public void setAvailableCopies(Integer availableCopies) { this.availableCopies = availableCopies; }

    public boolean isAvailable() {
        return availableCopies > 0;
    }

    public void borrowCopy() {
        if (availableCopies > 0) {
            availableCopies--;
        }
    }

    public void returnCopy() {
        if (availableCopies < totalCopies) {
            availableCopies++;
        }
    }

    public void addAuthor(Long authorId) {
        if (!authorIds.contains(authorId)) {
            authorIds.add(authorId);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Objects.equals(id, book.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}