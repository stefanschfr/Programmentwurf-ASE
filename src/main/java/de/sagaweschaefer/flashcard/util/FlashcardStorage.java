package de.sagaweschaefer.flashcard.util;

import de.sagaweschaefer.flashcard.model.FlashcardSet;
import de.sagaweschaefer.flashcard.model.FlashcardStatistics;
import de.sagaweschaefer.flashcard.model.SessionResult;

import java.util.List;
import java.util.Map;

public interface FlashcardStorage {
    void saveFlashcardSets(List<FlashcardSet> flashcardSets);
    List<FlashcardSet> loadFlashcardSets();

    void saveStatistics(Map<String, FlashcardStatistics> statistics);
    Map<String, FlashcardStatistics> loadStatistics();

    void saveSessionResults(List<SessionResult> results);
    List<SessionResult> loadSessionResults();

    void saveExamResults(List<SessionResult> results);
    List<SessionResult> loadExamResults();
}
