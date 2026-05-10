package ru.mfa.service;

import ru.mfa.model.*;
import ru.mfa.repository.InMemoryStorage;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class BookLoanService {
    private final InMemoryStorage storage = InMemoryStorage.getInstance();
    private static final double FINE_PER_DAY = 10.0;

    public BookLoan borrowBook(Long bookId, Long readerId) {
        if (!storage.existsBookById(bookId)) {
            throw new RuntimeException("Книга не найдена");
        }

        Optional<Reader> readerOpt = storage.findReaderById(readerId);
        if (readerOpt.isEmpty()) {
            throw new RuntimeException("Читатель не найден");
        }

        Reader reader = readerOpt.get();

        if (reader.isBlocked()) {
            throw new RuntimeException("Читатель заблокирован. Оплатите штрафы.");
        }

        double unpaidFines = storage.getTotalUnpaidFinesByReader(readerId);
        if (unpaidFines > 0) {
            throw new RuntimeException("У читателя есть непогашенные штрафы: " + unpaidFines + " руб.");
        }

        Optional<Book> bookOpt = storage.findBookById(bookId);
        Book book = bookOpt.get();

        if (!book.isAvailable()) {
            throw new RuntimeException("Нет доступных экземпляров книги");
        }

        long activeLoans = storage.findBookLoansByReaderId(readerId).stream()
                .filter(loan -> loan.getReturnDate() == null)
                .count();

        if (activeLoans >= 5) {
            throw new RuntimeException("Читатель уже имеет 5 активных книг. Верните хотя бы одну.");
        }

        BookLoan loan = new BookLoan(null, bookId, readerId);
        storage.saveBookLoan(loan);

        book.borrowCopy();
        storage.saveBook(book);

        reader.addBookLoan(loan.getId());
        storage.saveReader(reader);

        return loan;
    }

    public BookLoan returnBook(Long loanId) {
        Optional<BookLoan> loanOpt = storage.findBookLoanById(loanId);
        if (loanOpt.isEmpty()) {
            throw new RuntimeException("Выдача не найдена");
        }

        BookLoan loan = loanOpt.get();

        if (loan.getReturnDate() != null) {
            throw new RuntimeException("Книга уже возвращена");
        }

        loan.setReturnDate(LocalDate.now());

        if (loan.isOverdue()) {
            long daysOverdue = loan.getDaysOverdue();
            double fineAmount = daysOverdue * FINE_PER_DAY;

            Fine fine = new Fine(null, loan.getReaderId(), loanId, fineAmount, "OVERDUE");
            storage.saveFine(fine);

            Optional<Reader> readerOpt = storage.findReaderById(loan.getReaderId());
            readerOpt.ifPresent(reader -> {
                reader.addFine(fine.getId());
                storage.saveReader(reader);
            });
        }

        loan.setStatus("RETURNED");
        storage.saveBookLoan(loan);

        Optional<Book> bookOpt = storage.findBookById(loan.getBookId());
        bookOpt.ifPresent(book -> {
            book.returnCopy();
            storage.saveBook(book);
        });

        return loan;
    }

    public void payFine(Long fineId) {
        Optional<Fine> fineOpt = storage.findFineById(fineId);
        if (fineOpt.isEmpty()) {
            throw new RuntimeException("Штраф не найден");
        }

        Fine fine = fineOpt.get();

        if (fine.isPaid()) {
            throw new RuntimeException("Штраф уже оплачен");
        }

        fine.pay();
        storage.saveFine(fine);

        Optional<Reader> readerOpt = storage.findReaderById(fine.getReaderId());
        readerOpt.ifPresent(reader -> {
            double unpaid = storage.getTotalUnpaidFinesByReader(reader.getId());
            if (unpaid == 0 && reader.isBlocked()) {
                reader.setBlocked(false);
                storage.saveReader(reader);
            }
        });
    }
}