package ru.mfa.repository;

import ru.mfa.model.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryStorage {
    private final Map<Long, Book> books = new ConcurrentHashMap<>();
    private final Map<Long, Author> authors = new ConcurrentHashMap<>();
    private final Map<Long, Reader> readers = new ConcurrentHashMap<>();
    private final Map<Long, BookLoan> bookLoans = new ConcurrentHashMap<>();
    private final Map<Long, Fine> fines = new ConcurrentHashMap<>();

    private Long nextBookId = 1L;
    private Long nextAuthorId = 1L;
    private Long nextReaderId = 1L;
    private Long nextLoanId = 1L;
    private Long nextFineId = 1L;

    private static InMemoryStorage instance;

    private InMemoryStorage() {
        initTestData();
    }

    public static synchronized InMemoryStorage getInstance() {
        if (instance == null) {
            instance = new InMemoryStorage();
        }
        return instance;
    }

    private void initTestData() {
        Author author = new Author(nextAuthorId++, "Лев", "Толстой");
        authors.put(author.getId(), author);

        Book book = new Book(nextBookId++, "Война и мир", 1869, 3);
        book.addAuthor(author.getId());
        books.put(book.getId(), book);

        author.addBook(book.getId());

        Reader reader = new Reader(nextReaderId++, "Иван", "Петров", "ivan@mail.ru", "+7-999-123-4567");
        readers.put(reader.getId(), reader);

        System.out.println("Добавлены тестовые данные: " + authors.size() + " авторов, " + books.size() + " книг");
    }

    public Book saveBook(Book book) {
        if (book.getId() == null) {
            book.setId(nextBookId++);
        }
        books.put(book.getId(), book);
        return book;
    }

    public Optional<Book> findBookById(Long id) {
        return Optional.ofNullable(books.get(id));
    }

    public List<Book> findAllBooks() {
        return new ArrayList<>(books.values());
    }

    public void deleteBookById(Long id) {
        books.remove(id);
    }

    public boolean existsBookById(Long id) {
        return books.containsKey(id);
    }

    public Author saveAuthor(Author author) {
        if (author.getId() == null) {
            author.setId(nextAuthorId++);
        }
        authors.put(author.getId(), author);
        return author;
    }

    public Optional<Author> findAuthorById(Long id) {
        return Optional.ofNullable(authors.get(id));
    }

    public List<Author> findAllAuthors() {
        return new ArrayList<>(authors.values());
    }

    public void deleteAuthorById(Long id) {
        authors.remove(id);
    }

    public boolean existsAuthorById(Long id) {
        return authors.containsKey(id);
    }

    public Reader saveReader(Reader reader) {
        if (reader.getId() == null) {
            reader.setId(nextReaderId++);
        }
        readers.put(reader.getId(), reader);
        return reader;
    }

    public Optional<Reader> findReaderById(Long id) {
        return Optional.ofNullable(readers.get(id));
    }

    public List<Reader> findAllReaders() {
        return new ArrayList<>(readers.values());
    }

    public void deleteReaderById(Long id) {
        readers.remove(id);
    }

    public boolean existsReaderById(Long id) {
        return readers.containsKey(id);
    }

    public BookLoan saveBookLoan(BookLoan loan) {
        if (loan.getId() == null) {
            loan.setId(nextLoanId++);
        }
        bookLoans.put(loan.getId(), loan);
        return loan;
    }

    public Optional<BookLoan> findBookLoanById(Long id) {
        return Optional.ofNullable(bookLoans.get(id));
    }

    public List<BookLoan> findAllBookLoans() {
        return new ArrayList<>(bookLoans.values());
    }

    public List<BookLoan> findBookLoansByReaderId(Long readerId) {
        return bookLoans.values().stream()
                .filter(loan -> loan.getReaderId().equals(readerId))
                .toList();
    }

    public List<BookLoan> findActiveLoansByBookId(Long bookId) {
        return bookLoans.values().stream()
                .filter(loan -> loan.getBookId().equals(bookId) && loan.getReturnDate() == null)
                .toList();
    }

    public void deleteBookLoanById(Long id) {
        bookLoans.remove(id);
    }

    public Fine saveFine(Fine fine) {
        if (fine.getId() == null) {
            fine.setId(nextFineId++);
        }
        fines.put(fine.getId(), fine);
        return fine;
    }

    public Optional<Fine> findFineById(Long id) {
        return Optional.ofNullable(fines.get(id));
    }

    public List<Fine> findAllFines() {
        return new ArrayList<>(fines.values());
    }

    public List<Fine> findFinesByReaderId(Long readerId) {
        return fines.values().stream()
                .filter(fine -> fine.getReaderId().equals(readerId))
                .toList();
    }

    public List<Fine> findUnpaidFinesByReaderId(Long readerId) {
        return fines.values().stream()
                .filter(fine -> fine.getReaderId().equals(readerId) && "UNPAID".equals(fine.getStatus()))
                .toList();
    }

    public void deleteFineById(Long id) {
        fines.remove(id);
    }

    public boolean existsFineById(Long id) {return fines.containsKey(id);}

    public double getTotalUnpaidFinesByReader(Long readerId) {
        return findUnpaidFinesByReaderId(readerId).stream()
                .mapToDouble(Fine::getAmount)
                .sum();
    }
}