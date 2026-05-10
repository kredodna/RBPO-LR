package ru.mfa.service;

import ru.mfa.entity.*;
import ru.mfa.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookLoanService {

    private final BookRepository bookRepository;
    private final ReaderRepository readerRepository;
    private final BookLoanRepository bookLoanRepository;
    private final FineRepository fineRepository;

    private static final double FINE_PER_DAY = 10.0;
    private static final int MAX_ACTIVE_LOANS = 5;

    @Value("${library.fine.block-threshold:100}")
    private double blockThreshold;

    @Transactional
    public BookLoan borrowBook(Long bookId, Long readerId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Книга не найдена"));
        Reader reader = readerRepository.findById(readerId)
                .orElseThrow(() -> new RuntimeException("Читатель не найден"));

        if (reader.isBlocked()) {
            throw new RuntimeException("Читатель заблокирован");
        }

        double unpaidFines = fineRepository.findByReaderIdAndStatus(readerId, "UNPAID")
                .stream().mapToDouble(Fine::getAmount).sum();
        if (unpaidFines > 0) {
            throw new RuntimeException("Есть непогашенные штрафы: " + unpaidFines + " руб.");
        }

        if (!book.isAvailable()) {
            throw new RuntimeException("Нет доступных экземпляров");
        }

        long activeLoans = bookLoanRepository.findActiveLoansByReaderId(readerId).size();
        if (activeLoans >= MAX_ACTIVE_LOANS) {
            throw new RuntimeException("Максимум " + MAX_ACTIVE_LOANS + " книг");
        }

        book.borrowCopy();
        bookRepository.save(book);

        BookLoan loan = BookLoan.builder()
                .book(book)
                .reader(reader)
                .loanDate(LocalDate.now())
                .dueDate(LocalDate.now().plusDays(14))
                .status("ACTIVE")
                .build();

        return bookLoanRepository.save(loan);
    }

    @Transactional
    public BookLoan returnBook(Long loanId) {
        BookLoan loan = bookLoanRepository.findById(loanId)
                .orElseThrow(() -> new RuntimeException("Выдача не найдена"));

        if (loan.getReturnDate() != null) {
            throw new RuntimeException("Книга уже возвращена");
        }

        loan.setReturnDate(LocalDate.now());

        if (loan.isOverdue()) {
            long daysOverdue = loan.getDaysOverdue();
            double fineAmount = daysOverdue * FINE_PER_DAY;

            Fine fine = Fine.builder()
                    .reader(loan.getReader())
                    .bookLoan(loan)
                    .amount(fineAmount)
                    .reason("OVERDUE")
                    .status("UNPAID")
                    .build();
            fineRepository.save(fine);

            Reader reader = loan.getReader();
            double totalUnpaid = fineRepository.findByReaderIdAndStatus(reader.getId(), "UNPAID")
                    .stream().mapToDouble(Fine::getAmount).sum();

            if (totalUnpaid > blockThreshold) {
                reader.setBlocked(true);
                readerRepository.save(reader);
            }
        }

        Book book = loan.getBook();
        book.returnCopy();
        bookRepository.save(book);

        return bookLoanRepository.save(loan);
    }

    @Transactional
    public Fine payFine(Long fineId) {
        Fine fine = fineRepository.findById(fineId)
                .orElseThrow(() -> new RuntimeException("Штраф не найден"));

        if (fine.isPaid()) {
            throw new RuntimeException("Штраф уже оплачен");
        }

        fine.pay();
        fine = fineRepository.save(fine);

        Reader reader = fine.getReader();
        double unpaidFines = fineRepository.findByReaderIdAndStatus(reader.getId(), "UNPAID")
                .stream().mapToDouble(Fine::getAmount).sum();

        if (unpaidFines == 0 && reader.isBlocked()) {
            reader.setBlocked(false);
            readerRepository.save(reader);
        }

        return fine;
    }

    @Transactional
    public BookLoan renewBook(Long loanId) {
        BookLoan loan = bookLoanRepository.findById(loanId)
                .orElseThrow(() -> new RuntimeException("Выдача не найдена"));

        if (loan.getReturnDate() != null) {
            throw new RuntimeException("Книга уже возвращена");
        }

        if (loan.isOverdue()) {
            throw new RuntimeException("Нельзя продлить просроченную книгу");
        }

        LocalDate newDueDate = loan.getDueDate().plusDays(14);
        loan.setDueDate(newDueDate);

        return bookLoanRepository.save(loan);
    }

    @Transactional(readOnly = true)
    public List<Reader> getDebtors() {
        List<BookLoan> overdueLoans = bookLoanRepository.findByStatus("OVERDUE");
        return overdueLoans.stream()
                .map(BookLoan::getReader)
                .distinct()
                .toList();
    }
}