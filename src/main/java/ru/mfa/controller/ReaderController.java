package ru.mfa.controller;

import ru.mfa.entity.BookLoan;
import ru.mfa.entity.Fine;
import ru.mfa.entity.Reader;
import ru.mfa.repository.ReaderRepository;
import ru.mfa.repository.FineRepository;
import ru.mfa.service.BookLoanService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/readers")
@RequiredArgsConstructor
public class ReaderController {

    private final ReaderRepository readerRepository;
    private final FineRepository fineRepository;
    private final BookLoanService bookLoanService;

    @PostMapping
    public Reader createReader(@RequestBody Reader reader) {
        return readerRepository.save(reader);
    }

    @GetMapping
    public List<Reader> getAllReaders() {
        return readerRepository.findAll();
    }

    @GetMapping("/{id}")
    public Reader getReaderById(@PathVariable Long id) {
        return readerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Читатель не найден"));
    }

    @PutMapping("/{id}")
    public Reader updateReader(@PathVariable Long id, @RequestBody Reader reader) {
        if (!readerRepository.existsById(id)) {
            throw new RuntimeException("Читатель не найден");
        }
        reader.setId(id);
        return readerRepository.save(reader);
    }

    @DeleteMapping("/{id}")
    public String deleteReader(@PathVariable Long id) {
        if (!readerRepository.existsById(id)) {
            throw new RuntimeException("Читатель не найден");
        }
        readerRepository.deleteById(id);
        return "Читатель удалён";
    }

    @PostMapping("/{readerId}/borrow/{bookId}")
    public BookLoan borrowBook(@PathVariable Long readerId, @PathVariable Long bookId) {
        return bookLoanService.borrowBook(bookId, readerId);
    }

    @PostMapping("/return/{loanId}")
    public BookLoan returnBook(@PathVariable Long loanId) {
        return bookLoanService.returnBook(loanId);
    }

    @PostMapping("/renew/{loanId}")
    public BookLoan renewBook(@PathVariable Long loanId) {
        return bookLoanService.renewBook(loanId);
    }

    @GetMapping("/{readerId}/fines")
    public List<Fine> getReaderFines(@PathVariable Long readerId) {
        return fineRepository.findByReaderId(readerId);
    }

    @GetMapping("/debtors")
    public List<Reader> getDebtors() {
        return bookLoanService.getDebtors();
    }
}