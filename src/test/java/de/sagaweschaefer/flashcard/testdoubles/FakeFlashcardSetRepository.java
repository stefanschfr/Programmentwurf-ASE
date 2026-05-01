package de.sagaweschaefer.flashcard.testdoubles;

import de.sagaweschaefer.flashcard.domain.repository.FlashcardSetRepository;
import de.sagaweschaefer.flashcard.model.FlashcardSet;

import java.util.ArrayList;
import java.util.List;

public class FakeFlashcardSetRepository implements FlashcardSetRepository {
    private List<FlashcardSet> data = new ArrayList<>();

    @Override
    public List<FlashcardSet> findAll() {
        return new ArrayList<>(data);
    }

    @Override
    public void saveAll(List<FlashcardSet> sets) {
        this.data = new ArrayList<>(sets);
    }

    public int getStoredCount() {
        return data.size();
    }
}

