package ru.mfa.controller;

import ru.mfa.entity.Book;
import ru.mfa.entity.Author;
import ru.mfa.repository.BookRepository;
import ru.mfa.repository.AuthorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    @PostMapping
    public Book createBook(@RequestBody Book book) {
        return bookRepository.save(book);
    }

    @GetMapping
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @GetMapping("/{id}")
    public Book getBookById(@PathVariable Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Книга не найдена"));
    }

    @PutMapping("/{id}")
    public Book updateBook(@PathVariable Long id, @RequestBody Book bookUpdate) {
        Book existingBook = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Книга не найдена"));

        existingBook.setTitle(bookUpdate.getTitle());
        existingBook.setPublicationYear(bookUpdate.getPublicationYear());
        if (bookUpdate.getTotalCopies() != null) {
            int oldTotal = existingBook.getTotalCopies();
            int newTotal = bookUpdate.getTotalCopies();
            int diff = newTotal - oldTotal;

            existingBook.setTotalCopies(newTotal);
            existingBook.setAvailableCopies(existingBook.getAvailableCopies() + diff);

            if (existingBook.getAvailableCopies() < 0) {
                throw new RuntimeException("Ошибка: доступных копий не может быть меньше 0");
            }
            if (existingBook.getAvailableCopies() > newTotal) {
                existingBook.setAvailableCopies(newTotal);
            }
        }

        return bookRepository.save(existingBook);
    }

    @DeleteMapping("/{id}")
    public String deleteBook(@PathVariable Long id) {
        if (!bookRepository.existsById(id)) {
            throw new RuntimeException("Книга не найдена");
        }
        bookRepository.deleteById(id);
        return "Книга удалена";
    }

    @GetMapping("/available")
    public List<Book> getAvailableBooks() {
        return bookRepository.findByAvailableCopiesGreaterThan(0);
    }

    @PostMapping("/{bookId}/author/{authorId}")
    public String addAuthorToBook(@PathVariable Long bookId, @PathVariable Long authorId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Книга не найдена"));
        Author author = authorRepository.findById(authorId)
                .orElseThrow(() -> new RuntimeException("Автор не найден"));

        book.getAuthors().add(author);
        bookRepository.save(book);

        return "Автор добавлен к книге";
    }

    @DeleteMapping("/{bookId}/author/{authorId}")
    public String removeAuthorFromBook(@PathVariable Long bookId, @PathVariable Long authorId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Книга не найдена"));

        book.getAuthors().removeIf(author -> author.getId().equals(authorId));
        bookRepository.save(book);

        return "Автор удалён из книги";
    }

    @GetMapping("/{bookId}/authors")
    public List<Author> getBookAuthors(@PathVariable Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Книга не найдена"));
        return book.getAuthors();
    }
}