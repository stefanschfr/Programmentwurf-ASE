package de.sagaweschaefer.flashcard.application.usecase;

import de.sagaweschaefer.flashcard.domain.repository.SessionResultRepository;
import de.sagaweschaefer.flashcard.model.SessionResult;

import java.util.Collections;
import java.util.List;

/**
 * UseCase: Vergangene Sitzungs-/Prüfungsergebnisse abrufen.
 *
 * <p>Hängt nur vom Domain-Interface {@link SessionResultRepository} ab.
 * Liefert die letzten N Ergebnisse in absteigender Reihenfolge (neueste zuerst).</p>
 */
public class GetSessionResultsUseCase {

    private final SessionResultRepository repository;

    public GetSessionResultsUseCase(SessionResultRepository repository) {
        this.repository = repository;
    }

    /**
     * Liefert die letzten {@code limit} Ergebnisse (neueste zuerst).
     *
     * @param limit maximale Anzahl (0 = alle)
     * @return Liste in absteigender zeitlicher Reihenfolge
     */
    public List<SessionResult> execute(int limit) {
        List<SessionResult> all = repository.findAll();
        if (all.isEmpty()) {
            return List.of();
        }
        // Neueste zuerst: umgekehrt durch die Liste iterieren
        int size = all.size();
        int count = (limit > 0) ? Math.min(limit, size) : size;
        List<SessionResult> result = new java.util.ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            result.add(all.get(size - 1 - i));
        }
        return Collections.unmodifiableList(result);
    }
}

