package ru.mfa.controller;

import ru.mfa.entity.Fine;
import ru.mfa.entity.Reader;
import ru.mfa.repository.FineRepository;
import ru.mfa.repository.ReaderRepository;
import ru.mfa.service.BookLoanService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import ru.mfa.dto.FineRequest;

@RestController
@RequestMapping("/api/fines")
@RequiredArgsConstructor
public class FineController {

    private final FineRepository fineRepository;
    private final ReaderRepository readerRepository;
    private final BookLoanService bookLoanService;

    @PostMapping
    public Fine createFine(@RequestBody FineRequest request) {
        Reader reader = readerRepository.findById(request.getReaderId())
                .orElseThrow(() -> new RuntimeException("Читатель не найден"));

        Fine fine = Fine.builder()
                .reader(reader)
                .amount(request.getAmount())
                .reason(request.getReason())
                .status("UNPAID")
                .build();
        fine = fineRepository.save(fine);

        double totalUnpaid = fineRepository.findByReaderIdAndStatus(reader.getId(), "UNPAID")
                .stream().mapToDouble(Fine::getAmount).sum();

        if (totalUnpaid > 0) {
            reader.setBlocked(true);
            readerRepository.save(reader);
        }

        return fine;
    }

    @GetMapping
    public List<Fine> getAllFines() {
        return fineRepository.findAll();
    }

    @GetMapping("/{id}")
    public Fine getFineById(@PathVariable Long id) {
        return fineRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Штраф не найден"));
    }

    @PutMapping("/{id}")
    public Fine updateFine(@PathVariable Long id, @RequestBody Fine fineUpdate) {
        Fine existingFine = fineRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Штраф не найден"));

        if (fineUpdate.getAmount() != null) {
            existingFine.setAmount(fineUpdate.getAmount());
        }
        if (fineUpdate.getReason() != null) {
            existingFine.setReason(fineUpdate.getReason());
        }

        return fineRepository.save(existingFine);
    }

    @DeleteMapping("/{id}")
    public String deleteFine(@PathVariable Long id) {
        if (!fineRepository.existsById(id)) {
            throw new RuntimeException("Штраф не найден");
        }
        fineRepository.deleteById(id);
        return "Штраф удалён";
    }

    @PostMapping("/{fineId}/pay")
    public Fine payFine(@PathVariable Long fineId) {
        return bookLoanService.payFine(fineId);
    }

    @GetMapping("/reader/{readerId}")
    public List<Fine> getFinesByReader(@PathVariable Long readerId) {
        return fineRepository.findByReaderId(readerId);
    }

    @GetMapping("/unpaid")
    public List<Fine> getUnpaidFines() {
        return fineRepository.findByStatus("UNPAID");
    }
}