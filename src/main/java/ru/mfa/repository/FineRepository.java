package ru.mfa.repository;

import ru.mfa.entity.Fine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FineRepository extends JpaRepository<Fine, Long> {
    List<Fine> findByReaderId(Long readerId);
    List<Fine> findByReaderIdAndStatus(Long readerId, String status);
    List<Fine> findByStatus(String status);
}