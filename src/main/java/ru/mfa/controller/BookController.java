package ru.mfa.controller;

import ru.mfa.model.Book;
import ru.mfa.repository.InMemoryStorage;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {
    private final InMemoryStorage storage = InMemoryStorage.getInstance();

    @PostMapping
    public Book createBook(@RequestBody Book book) {
        return storage.saveBook(book);
    }

    @GetMapping
    public List<Book> getAllBooks() {
        return storage.findAllBooks();
    }

    @GetMapping("/{id}")
    public Book getBookById(@PathVariable Long id) {
        return storage.findBookById(id)
                .orElseThrow(() -> new RuntimeException("Книга не найдена"));
    }

    @PutMapping("/{id}")
    public Book updateBook(@PathVariable Long id, @RequestBody Book book) {
        if (!storage.existsBookById(id)) {
            throw new RuntimeException("Книга не найдена");
        }
        book.setId(id);
        return storage.saveBook(book);
    }

    @DeleteMapping("/{id}")
    public String deleteBook(@PathVariable Long id) {
        if (!storage.existsBookById(id)) {
            throw new RuntimeException("Книга не найдена");
        }
        storage.deleteBookById(id);
        return "Книга с ID " + id + " удалена";
    }
}