# Projektübersicht – Flashcard Learning Application

## Inhaltsverzeichnis

1. [Projektbeschreibung](#1-projektbeschreibung)
2. [Build-System & Abhängigkeiten](#2-build-system--abhängigkeiten)
3. [Architektur](#3-architektur)
   - [Schichtenmodell](#31-schichtenmodell)
   - [Paketstruktur](#32-paketstruktur)
   - [Komponentendiagramm](#33-komponentendiagramm)
   - [Datenfluss](#34-datenfluss)
   - [Design Patterns](#35-design-patterns)
   - [Spaced-Repetition-Algorithmus](#36-spaced-repetition-algorithmus)
4. [Alle Klassen im Detail](#4-alle-klassen-im-detail)
   - [Paket: menu](#41-paket-menu)
   - [Paket: menu.flashcardsetmanager](#42-paket-menuflashcardsetmanager)
   - [Paket: menu.flashcardmanager](#43-paket-menuflashcardmanager)
   - [Paket: menu.flashcardcreation](#44-paket-menuflashcardcreation)
   - [Paket: menu.flashcardsession](#45-paket-menuflashcardsession)
   - [Paket: model](#46-paket-model)
   - [Paket: util](#47-paket-util)
5. [Kernfunktionen](#5-kernfunktionen)

---

## 1. Projektbeschreibung

**Flashcard Learning Application** ist eine Java-Konsolenapplikation zum Lernen mit Karteikarten. Die Applikation unterstützt vier verschiedene Fragetypen, ein Spaced-Repetition-System (6 Stufen), einen Prüfungsmodus sowie die persistente Speicherung von Karten und Lernstatistiken im JSON-Format.

- **Gruppe:** `de.sagaweschaefer`
- **Version:** `1.0-SNAPSHOT`
- **Einstiegspunkt:** `de.sagaweschaefer.flashcard.menu.MainMenu`

---

## 2. Build-System & Abhängigkeiten

**Build-System:** Gradle (Java)

| Abhängigkeit | Version | Zweck |
|---|---|---|
| Jackson Databind | 2.15.2 | JSON-Serialisierung / -Deserialisierung |
| Jackson Datatype JSR310 | 2.15.2 | `LocalDateTime`-Unterstützung für Jackson |
| JUnit 5 | – | Unit Tests |

**Persistenz-Dateien (automatisch erzeugt):**
- `data/flashcard-sets.json` – Alle Kartensets mit Karten
- `data/flashcard-statistics.json` – Lernstatistiken pro Karte

---

## 3. Architektur

### 3.1 Schichtenmodell

Die Applikation ist in drei logische Schichten aufgeteilt:

```
┌──────────────────────────────────────────────────────┐
│                  PRESENTATION LAYER                   │
│  (menu, flashcardsetmanager, flashcardmanager,        │
│   flashcardcreation, flashcardsession)                │
│  Verantwortlich für Benutzerinteraktion & Navigation  │
├──────────────────────────────────────────────────────┤
│                    DOMAIN LAYER                       │
│  (model: Flashcard, FlashcardSet,                     │
│   FlashcardStatistics, QuestionType)                  │
│  Enthält die Geschäftslogik & Datenmodelle            │
├──────────────────────────────────────────────────────┤
│               INFRASTRUCTURE LAYER                    │
│  (util: JsonStorage, AppScanner, MenuUtils)           │
│  Persistenz, Ein-/Ausgabe, technische Hilfsmittel     │
└──────────────────────────────────────────────────────┘
```

### 3.2 Paketstruktur

```
de.sagaweschaefer.flashcard/
│
├── menu/                          ← Hauptmenü & Navigation
│   ├── MainMenu.java              ← Einstiegspunkt (main)
│   ├── Menu.java                  ← Generisches Menü-Framework
│   ├── MenuItem.java              ← Einzelner Menüeintrag
│   │
│   ├── flashcardsetmanager/       ← Set-Verwaltung
│   │   ├── FlashcardSetManagerMenu.java
│   │   └── FlashcardSetManagerMenuHelper.java
│   │
│   ├── flashcardmanager/          ← Karten-Verwaltung innerhalb eines Sets
│   │   ├── FlashcardManagerMenu.java
│   │   └── FlashcardManagerMenuHelper.java
│   │
│   ├── flashcardcreation/         ← Karten-Erstellung
│   │   ├── FlashcardCreationMenu.java
│   │   └── FlashcardCreationMenuHelper.java
│   │
│   └── flashcardsession/          ← Lernsitzungen & Prüfung
│       ├── FlashcardSessionMenu.java
│       ├── FlashcardSessionMenuHelper.java
│       ├── FlashcardSessionEngine.java
│       ├── FlashcardQuestionHelper.java
│       └── FlashcardSessionStatistics.java
│
├── model/                         ← Datenmodelle (Domain Layer)
│   ├── Flashcard.java
│   ├── FlashcardSet.java
│   ├── FlashcardStatistics.java
│   └── QuestionType.java          ← Enum
│
└── util/                          ← Technische Hilfsmittel
    ├── AppScanner.java            ← Singleton Scanner
    ├── JsonStorage.java           ← JSON-Persistenz
    └── MenuUtils.java             ← UI-Hilfsmethoden
```

### 3.3 Komponentendiagramm

```
┌─────────────────────────────────────────────────────────────────┐
│                          MainMenu                               │
│                    (Einstiegspunkt / main)                       │
└───────────────────────┬─────────────────────┬───────────────────┘
                        │                     │
           ┌────────────▼──────────┐  ┌───────▼─────────────────┐
           │ FlashcardSetManager   │  │  FlashcardSessionMenu   │
           │      Menu             │  │                         │
           └────────────┬──────────┘  └───────┬─────────────────┘
                        │                     │
           ┌────────────▼──────────┐  ┌───────▼──────────────────┐
           │ FlashcardSetManager   │  │ FlashcardSessionMenu     │
           │    MenuHelper         │  │      Helper              │
           │                       │  │                          │
           │ ・addFlashcardSet()   │  │ ・startSession()         │
           │ ・listFlashcardSets() │  │ ・startDueCardsSession() │
           │ ・deleteFlashcardSet()│  │ ・startWrongAnswers-     │
           │ ・editFlashcardSet()  │  │   Session()              │
           └────────────┬──────────┘  │ ・startExamMode()        │
                        │             └───────┬──────────────────┘
           ┌────────────▼──────────┐          │
           │  FlashcardManager     │  ┌───────▼──────────────────┐
           │      Menu             │  │ FlashcardSessionEngine   │
           └────────────┬──────────┘  │                          │
                        │             │ ・runSession()            │
           ┌────────────▼──────────┐  │ ・runExamSession()        │
           │  FlashcardManager     │  └───────┬──────────────────┘
           │    MenuHelper         │          │
           │                       │  ┌───────▼──────────────────┐
           │ ・addFlashcard()      │  │ FlashcardQuestionHelper  │
           │ ・listFlashcards()    │  │ (static)                 │
           │ ・deleteFlashcard()   │  │ ・askQuestion()           │
           └────────────┬──────────┘  │ ・getCorrectAnswer-      │
                        │             │   Display()              │
           ┌────────────▼──────────┐  └──────────────────────────┘
           │ FlashcardCreation     │
           │      Menu             │          ┌─────────────────┐
           └────────────┬──────────┘          │  JsonStorage    │
                        │              ◄──────┤ (alle Helper    │
           ┌────────────▼──────────┐          │  nutzen dies)   │
           │ FlashcardCreation     │          │                 │
           │    MenuHelper         │          │ ・loadSets()    │
           │                       │          │ ・saveSets()    │
           │ ・addFreeText()       │          │ ・loadStats()   │
           │ ・addMultipleChoice() │          │ ・saveStats()   │
           │ ・addTrueFalse()      │          └─────────────────┘
           │ ・addNumeric()        │
           └───────────────────────┘
```

### 3.4 Datenfluss

#### Karte erstellen:

```
Nutzer
  │ Eingabe (Frage, Antwort, Typ)
  ▼
FlashcardCreationMenuHelper
  │ new Flashcard(...)
  ▼
FlashcardSet.add(flashcard)
  │
  ▼
JsonStorage.saveFlashcardSets(allSets)
  │
  ▼
data/flashcard-sets.json
```

#### Lernsitzung:

```
Nutzer
  │ Set auswählen
  ▼
FlashcardSessionMenuHelper
  │ loadStatistics() + Karten filtern (fällig / falsch / alle)
  ▼
FlashcardSessionEngine.runSession(cards, name)
  │ shuffle(cards)
  │ für jede Karte:
  │   FlashcardQuestionHelper.askQuestion(card)
  │     → Antwort prüfen
  │   stats.incrementCorrect(wasDue) / stats.incrementWrong()
  ▼
JsonStorage.saveStatistics(statisticsMap)
  │
  ▼
FlashcardSessionStatistics.displaySessionResult(correct, total, time)
  │ → Prozentsatz + Note (1,0–6,0)
```

### 3.5 Design Patterns

| Pattern | Wo angewendet | Beschreibung |
|---|---|---|
| **Singleton** | `AppScanner` | Nur eine `Scanner`-Instanz für die gesamte Applikation |
| **Helper Pattern** | `*MenuHelper`-Klassen | Trennung von Menü-Darstellung und Geschäftslogik |
| **Strategy Pattern** | `FlashcardQuestionHelper` | Fragetyp-spezifische Behandlung per `switch` auf `QuestionType` |
| **Template Method** | `Menu.start()` | Generischer Menü-Loop mit abstrakten Schritten |
| **Factory (informal)** | `FlashcardCreationMenuHelper` | Verschiedene `Flashcard`-Konstruktoren je nach Typ |
| **Repository** | `JsonStorage` | Kapselung der gesamten Datenpersistenz |

### 3.6 Spaced-Repetition-Algorithmus

`FlashcardStatistics` implementiert ein 6-stufiges Spaced-Repetition-System:

| Stufe | Beschreibung | Wiederholung nach |
|---|---|---|
| 0 | Falsch beantwortet / noch nicht gesehen | sofort fällig |
| 1 | Erstmals richtig beantwortet | 1 Minute |
| 2 | Zweimal richtig | 10 Minuten |
| 3 | Dreimal richtig | 5 Stunden |
| 4 | Viermal richtig | 1 Tag |
| 5 | Fünfmal richtig | 14 Tage |
| 6 | Vollständig gelernt | 1 Monat |

**Regelwerk:**
- Richtige Antwort (wenn Karte fällig war) → Stufe + 1 (max. 6)
- Falsche Antwort → Stufe zurück auf 0
- `isDue()` → prüft ob `lastCorrectAt + Wartezeit ≤ jetzt`

---

## 4. Alle Klassen im Detail

### 4.1 Paket: `menu`

#### `MainMenu`
- **Zweck:** Einstiegspunkt der Applikation. Enthält `main()`, initialisiert alle Hauptkomponenten und zeigt das Hauptmenü.
- **Felder:** `menu: Menu`, `flashcardSetManagerMenu`, `flashcardSessionMenu`
- **Methoden:** `main(String[])`, `start()`, `setupMenu()`

#### `Menu`
- **Zweck:** Generisches, wiederverwendbares Menü-Framework. Zeigt nummerierte Einträge, liest Auswahl und führt Aktionen aus.
- **Felder:** `title: String`, `items: Map<Integer, MenuItem>`
- **Methoden:** `addItem(int, MenuItem)`, `start()`, `showMenu()`, `handleSelection(int)`

#### `MenuItem`
- **Zweck:** Einzelner Menüeintrag mit Label, Aktion (`Runnable`) und optionalem Exit-Flag.
- **Felder:** `label: String`, `action: Runnable`, `exitAfter: boolean`
- **Methoden:** `getLabel()`, `execute()`, `shouldExit()`

---

### 4.2 Paket: `menu.flashcardsetmanager`

#### `FlashcardSetManagerMenu`
- **Zweck:** Zeigt das Menü zur Verwaltung von Kartensets (erstellen, auflisten, bearbeiten, löschen).
- **Felder:** `menu: Menu`, `flashcardSetManagerMenuHelper`
- **Methoden:** `start()`, `setupMenu()`

#### `FlashcardSetManagerMenuHelper`
- **Zweck:** Implementiert die CRUD-Logik für `FlashcardSet`-Objekte. Lädt bei jedem Aufruf frische Daten aus dem JSON.
- **Felder:** `flashcardSets: List<FlashcardSet>`, `storage: JsonStorage`
- **Methoden:** `addFlashcardSet()`, `listFlashcardSets()`, `deleteFlashcardSet()`, `editFlashcardSet()`, `validateAndPerformDelete(int)`
- **Besonderheit:** Beim Löschen eines Sets werden auch verwaiste Statistikeinträge der enthaltenen Karten bereinigt.

---

### 4.3 Paket: `menu.flashcardmanager`

#### `FlashcardManagerMenu`
- **Zweck:** Zeigt das Menü zur Verwaltung von Karten innerhalb eines konkreten Sets.
- **Felder:** `menu: Menu`, `flashcardManagerMenuHelper`
- **Methoden:** `start()`, `setupMenu()`

#### `FlashcardManagerMenuHelper`
- **Zweck:** CRUD-Operationen auf Karten eines Sets. Öffnet `FlashcardCreationMenu` zum Hinzufügen neuer Karten.
- **Felder:** `flashcardSet: FlashcardSet`, `allSets: List<FlashcardSet>`, `storage: JsonStorage`
- **Methoden:** `addFlashcard()`, `listFlashcards()`, `deleteFlashcard()`, `save()`, `getFlashcardSet()`
- **Besonderheit:** Beim Löschen werden auch Statistikeinträge der gelöschten Karte entfernt.

---

### 4.4 Paket: `menu.flashcardcreation`

#### `FlashcardCreationMenu`
- **Zweck:** Menü zur Auswahl des gewünschten Karteikarten-Typs.
- **Felder:** `menu: Menu`, `helper: FlashcardCreationMenuHelper`
- **Methoden:** `start()`, `setupMenu()`

#### `FlashcardCreationMenuHelper`
- **Zweck:** Erstellt neue Flashcards aller vier Typen auf Basis von Nutzereingaben und speichert sie im Set.
- **Felder:** `flashcardSet: FlashcardSet`, `allSets: List<FlashcardSet>`, `storage: JsonStorage`
- **Methoden:** `addFreeTextFlashcard()`, `addMultipleChoiceFlashcard()`, `addTrueFalseFlashcard()`, `addNumericFlashcard()`, `addFlashcard(Flashcard)`, `save()`

---

### 4.5 Paket: `menu.flashcardsession`

#### `FlashcardSessionMenu`
- **Zweck:** Zeigt das Menü zur Auswahl des Lernmodus.
- **Felder:** `menu: Menu`, `helper: FlashcardSessionMenuHelper`
- **Methoden:** `start()`, `setupMenu()`

#### `FlashcardSessionMenuHelper`
- **Zweck:** Lädt Sets, filtert Karten nach Modus (fällig, falsch, alle) und startet die Session-Engine.
- **Felder:** `flashcardSets: List<FlashcardSet>`, `storage: JsonStorage`, `engine: FlashcardSessionEngine`
- **Methoden:** `startSession()`, `startWrongAnswersSession()`, `startDueCardsSession()`, `startExamMode()`, `refreshFlashcardSets()`

#### `FlashcardSessionEngine`
- **Zweck:** Führt Lern- und Prüfungssitzungen aus. Mischt Karten, stellt Fragen via `FlashcardQuestionHelper`, aktualisiert Statistiken und speichert sie.
- **Felder:** `storage: JsonStorage`
- **Methoden:**
  - `runSession(List<Flashcard>, String)` – Reguläre Sitzung, alle übergebenen Karten
  - `runExamSession(List<Flashcard>, String)` – Prüfungssitzung, 10 Minuten Zeitlimit

#### `FlashcardQuestionHelper` *(statisch)*
- **Zweck:** Stellt Fragen im passenden Format je Fragetyp und prüft die Nutzereingabe.
- **Methoden:**
  - `askQuestion(Flashcard): boolean` – Fragt eine Karte ab, gibt `true` zurück wenn richtig
  - `getCorrectAnswerDisplay(Flashcard): String` – Liefert die korrekte Antwort als formatierten String

#### `FlashcardSessionStatistics` *(statisch)*
- **Zweck:** Berechnet und zeigt Ergebnisse einer Sitzung an (Prozentsatz, deutsche Schulnote, Zeit).
- **Methoden:**
  - `displaySessionResult(int, int, long)` – Ausgabe Ergebnis + Note
  - `calculateGrade(double): double` – Prozentsatz → Note (1,0–6,0)
  - `formatTime(long): String` – Millisekunden → `"Xm Ys"`

**Notenberechnung:**

| Prozentsatz | Note |
|---|---|
| ≥ 95 % | 1,0 |
| 50–94 % | 1,0–4,0 (linear) |
| 30–49 % | 4,0–5,0 (linear) |
| 0–29 % | 5,0–6,0 (linear) |

---

### 4.6 Paket: `model`

#### `Flashcard`
- **Zweck:** Repräsentiert eine einzelne Karteikarte. Unterstützt alle vier Fragetypen.
- **Implements:** `Serializable`
- **Felder:**

| Feld | Typ | Beschreibung |
|---|---|---|
| `id` | `String` | UUID, automatisch generiert |
| `question` | `String` | Fragetext |
| `questionType` | `QuestionType` | Fragetyp (Enum) |
| `answerText` | `String` | Textantwort (FREE_TEXT, MULTIPLE_CHOICE, TRUE_FALSE) |
| `answerNum` | `Double` | Numerische Antwort (NUMERIC) |
| `options` | `List<String>` | Antwortoptionen (nur MULTIPLE_CHOICE) |

- **Konstruktoren:** je ein spezialisierter Konstruktor pro Fragetyp
- **Besonderheit:** `equals()` und `hashCode()` basieren auf Frageinhalt, nicht auf der ID

#### `FlashcardSet`
- **Zweck:** Benannter Container für eine Sammlung von `Flashcard`-Objekten.
- **Implements:** `Serializable`
- **Felder:** `name: String`, `flashcardSet: List<Flashcard>`

#### `FlashcardStatistics`
- **Zweck:** Verfolgt den Lernfortschritt einer Karte. Enthält Stufe, Zeitstempel und Zähler.
- **Implements:** `Serializable`
- **Annotation:** `@JsonIgnoreProperties(ignoreUnknown = true)`
- **Felder:**

| Feld | Typ | Beschreibung |
|---|---|---|
| `flashcardId` | `String` | ID der zugehörigen Karte |
| `lastCorrectAt` | `LocalDateTime` | Zeitpunkt der letzten richtigen Antwort |
| `correctCount` | `int` | Gesamtanzahl richtiger Antworten |
| `wrongCount` | `int` | Gesamtanzahl falscher Antworten |
| `level` | `int` | Spaced-Repetition-Stufe (0–6) |

- **Schlüsselmethoden:** `isDue()`, `incrementCorrect(boolean wasDue)`, `incrementWrong()`

#### `QuestionType` *(Enum)*
- **Werte:** `FREE_TEXT`, `MULTIPLE_CHOICE`, `TRUE_FALSE`, `NUMERIC`

---

### 4.7 Paket: `util`

#### `AppScanner`
- **Zweck:** Stellt einen applikationsweiten Singleton-`Scanner` auf `System.in` bereit.
- **Felder:** `SCANNER: static final Scanner`

#### `MenuUtils`
- **Zweck:** Hilfsmethoden für Nutzereingaben und formatierte Listenausgabe.
- **Methoden:** `readMenuSelection()`, `promptForString(String)`, `promptForInt(String)`, `displayFlashcardSets(List, String)`, `displayFlashcards(List, String)`

#### `JsonStorage`
- **Zweck:** Kapselt die gesamte JSON-Persistenz via Jackson. Erstellt fehlende Verzeichnisse automatisch.
- **Felder:** `objectMapper: ObjectMapper` (konfiguriert mit `JavaTimeModule`, Pretty-Print)
- **Methoden:** `saveFlashcardSets(List)`, `loadFlashcardSets()`, `saveStatistics(Map)`, `loadStatistics()`, `saveToFile(String, Object)`

---

## 5. Kernfunktionen

| Funktion | Beschreibung |
|---|---|
| **Set-Verwaltung** | Sets erstellen, umbenennen (via Bearbeiten), löschen |
| **Karten-Verwaltung** | Karten hinzufügen, auflisten, löschen – 4 Fragetypen |
| **Reguläre Lernsitzung** | Alle nicht vollständig gelernten Karten (Stufe < 6) eines Sets |
| **Fällige Karten** | Nur Karten, deren Wiederholungsintervall abgelaufen ist |
| **Falsch beantwortete Karten** | Nur Karten auf Stufe 0 (setzt-übergreifend) |
| **Prüfungsmodus** | 10 zufällige Karten, max. 10 Minuten, mit Benotung |
| **Spaced Repetition** | 6-Stufen-System mit Intervallen von 1 Min. bis 1 Monat |
| **Persistenz** | JSON-Speicherung in `data/` – automatisch beim Speichern/Löschen |
