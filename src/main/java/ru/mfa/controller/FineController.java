package ru.mfa.controller;

import ru.mfa.model.Fine;
import ru.mfa.repository.InMemoryStorage;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/fines")
public class FineController {

    private final InMemoryStorage storage = InMemoryStorage.getInstance();

    @PostMapping
    public Fine createFine(@RequestBody Fine fine) {
        return storage.saveFine(fine);
    }

    @GetMapping
    public List<Fine> getAllFines() {
        return storage.findAllFines();
    }

    @GetMapping("/{id}")
    public Fine getFineById(@PathVariable Long id) {
        return storage.findFineById(id)
                .orElseThrow(() -> new RuntimeException("Штраф не найден"));
    }

    @PutMapping("/{id}")
    public Fine updateFine(@PathVariable Long id, @RequestBody Fine fine) {
        if (!storage.existsFineById(id)) {
            throw new RuntimeException("Штраф не найден");
        }
        fine.setId(id);
        return storage.saveFine(fine);
    }

    @DeleteMapping("/{id}")
    public String deleteFine(@PathVariable Long id) {
        if (!storage.existsFineById(id)) {
            throw new RuntimeException("Штраф не найден");
        }
        storage.deleteFineById(id);
        return "Штраф удалён";
    }

    @GetMapping("/reader/{readerId}")
    public List<Fine> getFinesByReader(@PathVariable Long readerId) {
        return storage.findFinesByReaderId(readerId);
    }

    @GetMapping("/reader/{readerId}/unpaid")
    public List<Fine> getUnpaidFinesByReader(@PathVariable Long readerId) {
        return storage.findUnpaidFinesByReaderId(readerId);
    }

    @PostMapping("/{fineId}/pay")
    public String payFine(@PathVariable Long fineId) {
        Fine fine = storage.findFineById(fineId)
                .orElseThrow(() -> new RuntimeException("Штраф не найден"));

        if (fine.isPaid()) {
            return "Штраф уже оплачен";
        }

        fine.pay();
        storage.saveFine(fine);
        return "Штраф оплачен";
    }
}