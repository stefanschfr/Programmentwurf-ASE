package de.sagaweschaefer.flashcard.application.usecase;

import de.sagaweschaefer.flashcard.domain.repository.FlashcardSetRepository;
import de.sagaweschaefer.flashcard.model.FlashcardSet;

import java.util.List;

/**
 * UseCase: Ein FlashcardSet löschen.
 *
 * <p>Sucht das Set per Index und entfernt es aus der Persistenz.
 * Hängt nur vom Domain-Interface ab (Dependency Rule, DIP).</p>
 */
public class DeleteFlashcardSetUseCase {

    private final FlashcardSetRepository repository;

    public DeleteFlashcardSetUseCase(FlashcardSetRepository repository) {
        this.repository = repository;
    }

    /**
     * Löscht das Set am gegebenen Index.
     *
     * @param index 0-basierter Index
     * @return Name des gelöschten Sets
     * @throws IndexOutOfBoundsException bei ungültigem Index
     */
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

