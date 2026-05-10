package ru.mfa.controller;

import ru.mfa.model.Author;
import ru.mfa.repository.InMemoryStorage;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/authors")
public class AuthorController {

    private final InMemoryStorage storage = InMemoryStorage.getInstance();

    @PostMapping
    public Author createAuthor(@RequestBody Author author) {
        return storage.saveAuthor(author);
    }

    @GetMapping
    public List<Author> getAllAuthors() {
        return storage.findAllAuthors();
    }

    @GetMapping("/{id}")
    public Author getAuthorById(@PathVariable Long id) {
        return storage.findAuthorById(id)
                .orElseThrow(() -> new RuntimeException("Автор не найден"));
    }

    @PutMapping("/{id}")
    public Author updateAuthor(@PathVariable Long id, @RequestBody Author author) {
        if (!storage.existsAuthorById(id)) {
            throw new RuntimeException("Автор не найден");
        }
        author.setId(id);
        return storage.saveAuthor(author);
    }

    @DeleteMapping("/{id}")
    public String deleteAuthor(@PathVariable Long id) {
        if (!storage.existsAuthorById(id)) {
            throw new RuntimeException("Автор не найден");
        }
        storage.deleteAuthorById(id);
        return "Автор удалён";
    }
}