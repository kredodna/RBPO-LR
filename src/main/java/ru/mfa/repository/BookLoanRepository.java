package ru.mfa.repository;

import ru.mfa.entity.BookLoan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BookLoanRepository extends JpaRepository<BookLoan, Long> {
    List<BookLoan> findByReaderId(Long readerId);
    List<BookLoan> findByBookId(Long bookId);
    List<BookLoan> findByReaderIdAndReturnDateIsNull(Long readerId);
    List<BookLoan> findByStatus(String status);

    @Query("SELECT bl FROM BookLoan bl WHERE bl.reader.id = :readerId AND bl.returnDate IS NULL")
    List<BookLoan> findActiveLoansByReaderId(@Param("readerId") Long readerId);
}