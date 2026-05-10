package ru.mfa.controller;

import ru.mfa.entity.Author;
import ru.mfa.repository.AuthorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/authors")
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorRepository authorRepository;

    @PostMapping
    public Author createAuthor(@RequestBody Author author) {
        return authorRepository.save(author);
    }

    @GetMapping
    public List<Author> getAllAuthors() {
        return authorRepository.findAll();
    }

    @GetMapping("/{id}")
    public Author getAuthorById(@PathVariable Long id) {
        return authorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Автор не найден"));
    }

    @PutMapping("/{id}")
    public Author updateAuthor(@PathVariable Long id, @RequestBody Author author) {
        if (!authorRepository.existsById(id)) {
            throw new RuntimeException("Автор не найден");
        }
        author.setId(id);
        return authorRepository.save(author);
    }

    @DeleteMapping("/{id}")
    public String deleteAuthor(@PathVariable Long id) {
        if (!authorRepository.existsById(id)) {
            throw new RuntimeException("Автор не найден");
        }
        authorRepository.deleteById(id);
        return "Автор удалён";
    }
}