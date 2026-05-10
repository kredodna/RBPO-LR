package ru.mfa.controller;

import ru.mfa.model.Reader;
import ru.mfa.repository.InMemoryStorage;
import ru.mfa.service.BookLoanService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/readers")
public class ReaderController {
    private final InMemoryStorage storage = InMemoryStorage.getInstance();
    private final BookLoanService loanService = new BookLoanService();

    @PostMapping
    public Reader createReader(@RequestBody Reader reader) {
        return storage.saveReader(reader);
    }

    @GetMapping
    public List<Reader> getAllReaders() {
        return storage.findAllReaders();
    }

    @GetMapping("/{id}")
    public Reader getReaderById(@PathVariable Long id) {
        return storage.findReaderById(id)
                .orElseThrow(() -> new RuntimeException("Читатель не найден"));
    }

    @PutMapping("/{id}")
    public Reader updateReader(@PathVariable Long id, @RequestBody Reader reader) {
        if (!storage.existsReaderById(id)) {
            throw new RuntimeException("Читатель не найден");
        }
        reader.setId(id);
        return storage.saveReader(reader);
    }

    @DeleteMapping("/{id}")
    public String deleteReader(@PathVariable Long id) {
        if (!storage.existsReaderById(id)) {
            throw new RuntimeException("Читатель не найден");
        }
        storage.deleteReaderById(id);
        return "Читатель с ID " + id + " удалён";
    }

    @PostMapping("/{readerId}/borrow/{bookId}")
    public String borrowBook(@PathVariable Long readerId, @PathVariable Long bookId) {
        loanService.borrowBook(bookId, readerId);
        return "Книга выдана читателю";
    }

    @PostMapping("/{readerId}/return/{loanId}")
    public String returnBook(@PathVariable Long readerId, @PathVariable Long loanId) {
        loanService.returnBook(loanId);
        return "Книга возвращена";
    }

    @GetMapping("/{readerId}/fines")
    public List<?> getReaderFines(@PathVariable Long readerId) {
        return storage.findFinesByReaderId(readerId);
    }
}