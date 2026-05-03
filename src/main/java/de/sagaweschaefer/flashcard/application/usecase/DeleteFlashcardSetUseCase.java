package de.sagaweschaefer.flashcard.application.usecase;

import de.sagaweschaefer.flashcard.domain.repository.FlashcardSetRepository;
import de.sagaweschaefer.flashcard.model.FlashcardSet;

import java.util.List;

public class DeleteFlashcardSetUseCase {

    private final FlashcardSetRepository repository;

    public DeleteFlashcardSetUseCase(FlashcardSetRepository repository) {
        this.repository = repository;
    }

    public String execute(int index) {
        List<FlashcardSet> sets = repository.findAll();
        if (index < 0 || index >= sets.size()) {
            throw new IndexOutOfBoundsException(
                    "Index " + index + " außerhalb des Bereichs (0.." + (sets.size() - 1) + ")");
        }
        String name = sets.get(index).getName();
        sets.remove(index);
        repository.saveAll(sets);
        return name;
    }
}

