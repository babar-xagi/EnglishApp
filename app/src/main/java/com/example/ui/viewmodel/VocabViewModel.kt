package com.example.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.*
import com.example.data.api.GeminiClient
import com.example.data.database.AppDatabase
import com.example.data.database.GameResult
import com.example.data.database.SpeakingPractice
import com.example.data.database.UserProfile
import com.example.data.database.VocabularyWord
import com.example.data.database.WritingPractice
import com.example.data.repository.VocabRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import org.json.JSONObject
import java.util.UUID

class VocabViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: VocabRepository = VocabRepository(AppDatabase.getDatabase(application))

    // --- DATABASE FLOWS ---
    val userProfile: StateFlow<UserProfile?> = repository.userProfile
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val vocabWords: StateFlow<List<VocabularyWord>> = repository.allWords
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val wordCount: StateFlow<Int> = repository.wordCount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val masteredWordCount: StateFlow<Int> = repository.masteredWordCount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val speakingPractices: StateFlow<List<SpeakingPractice>> = repository.allSpeakingPractices
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val writingPractices: StateFlow<List<WritingPractice>> = repository.allWritingPractices
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val gameResults: StateFlow<List<GameResult>> = repository.allGameResults
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Direct fetch helper for reviews
    val wordsForReview: StateFlow<List<VocabularyWord>> = repository.getWordsForReview(System.currentTimeMillis())
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // --- GEOMETRIC DYNAMIC THEME & ALARM STATES ---
    val isDarkTheme = MutableStateFlow(false)
    val reminderHour = MutableStateFlow(18)
    val reminderMinute = MutableStateFlow(0)
    val reminderDays = MutableStateFlow(listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"))
    val isReminderEnabled = MutableStateFlow(false)
    val inAppNotificationMessage = MutableStateFlow<String?>(null)

    // --- DYNAMIC NOTEBOOK GAME FLOWS ---
    val dynamicFillBlanksQuestions = MutableStateFlow<List<FillInBlankQuestion>>(emptyList())
    val dynamicSynonymPairs = MutableStateFlow<List<SynonymPair>>(emptyList())

    init {
        // Sync and compile games dynamically whenever the vocabulary notebook updates!
        viewModelScope.launch {
            vocabWords.collect { savedList ->
                selectOrBuildFillBlanksQuestions(savedList)
                selectOrBuildSynonymPairs(savedList)
            }
        }
    }

    fun selectOrBuildFillBlanksQuestions(savedList: List<VocabularyWord>) {
        val questions = if (savedList.isNotEmpty()) {
            savedList.shuffled().take(6).mapIndexed { idx, item ->
                val cleanWord = item.word.trim()
                val blankSentence = if (item.exampleSentence.contains(cleanWord, ignoreCase = true)) {
                    item.exampleSentence.replace(Regex("(?i)\\b" + Regex.escape(cleanWord) + "\\b"), "_____")
                } else {
                    if (item.exampleSentence.isNotBlank()) {
                        item.exampleSentence.replace(cleanWord, "_____")
                    } else {
                        "The _____ is defined as: ${item.meaning}."
                    }
                }
                val distractors = (savedList.map { it.word } + MockData.wordTemplates.map { it.word })
                    .filter { it.lowercase().trim() != cleanWord.lowercase().trim() }
                    .distinct()
                    .shuffled()
                    .take(3)
                val options = (distractors + cleanWord).shuffled()
                val correctIdx = options.indexOfFirst { it.lowercase().trim() == cleanWord.lowercase().trim() }.coerceAtLeast(0)
                FillInBlankQuestion(
                    id = idx,
                    sentenceWithBlank = blankSentence,
                    options = options,
                    correctIndex = correctIdx,
                    explanation = "Word Type: ${item.wordType}. Meaning: ${item.meaning}. ${item.simpleExplanation}"
                )
            }
        } else {
            MockData.fillInTheBlanks
        }
        dynamicFillBlanksQuestions.value = questions
    }

    fun selectOrBuildSynonymPairs(savedList: List<VocabularyWord>) {
        val pairs = if (savedList.size >= 3) {
            savedList.shuffled().take(4).map { item ->
                val firstSyn = item.synonyms.split(";").firstOrNull { it.isNotBlank() } ?: "diligent"
                SynonymPair(
                    weak = firstSyn.trim().lowercase().replaceFirstChar { it.uppercase() },
                    strong = item.word.trim().lowercase().replaceFirstChar { it.uppercase() },
                    example = "Meaning: ${item.meaning}"
                )
            }
        } else {
            MockData.synonymPairs
        }
        dynamicSynonymPairs.value = pairs
    }

    // --- REPLAYABLE CONTROLS & TRIGGERS ---
    fun toggleTheme() {
        isDarkTheme.value = !isDarkTheme.value
    }

    fun saveReminder(hour: Int, minute: Int, days: List<String>) {
        reminderHour.value = hour
        reminderMinute.value = minute
        reminderDays.value = days
        isReminderEnabled.value = true
        triggerBriefNotification("Study Alarm Synced! We'll remind you on ${if (days.size == 7) "Every Day" else days.joinToString(", ")} at ${String.format("%02d:%02d", hour, minute)}")
    }

    fun triggerBriefNotification(message: String) {
        viewModelScope.launch {
            inAppNotificationMessage.value = message
            delay(5000)
            inAppNotificationMessage.value = null
        }
    }

    fun simulateNotificationTrigger() {
        viewModelScope.launch {
            val name = userProfile.value?.name ?: "Scholar"
            inAppNotificationMessage.value = "Hey $name! 🌟 It is time to study and build your vocabulary on VocabQuest! Let's complete today's daily mission! 🚀"
            delay(8000)
            inAppNotificationMessage.value = null
        }
    }


    // --- UI VIEW STATES ---
    
    // Onboarding Completed State
    var isOnboardingCompleted = MutableStateFlow(false)

    // Current Reading/Listening Daily Input
    val selectedDifficulty = MutableStateFlow("Intermediate")
    val selectedDailyInput = MutableStateFlow<DailyInput?>(null)

    // Context Tapped Word state
    val tappedWord = MutableStateFlow<WordTemplate?>(null)
    val guessedStatus = MutableStateFlow<Boolean?>(null) // true: correct, false: incorrect, null: not guessed yet
    val selectedGuessOption = MutableStateFlow<String?>(null)

    // Games states
    // A: Fill In Blanks
    val fillBlanksIndex = MutableStateFlow(0)
    val fillBlanksScore = MutableStateFlow(0)
    val fillBlanksShowResult = MutableStateFlow(false)
    val fillBlanksAnswers = MutableStateFlow<List<Boolean>>(emptyList()) // tracks result of each question

    // B: Synonym Matcher
    val synonymMatches = MutableStateFlow<Map<String, String>>(emptyMap())
    val selectedSynonymWeak = MutableStateFlow<String?>(null)
    val synonymScore = MutableStateFlow(0)
    val synonymMatchingCompleted = MutableStateFlow(false)

    // C: Don't Say Very
    val veryIndex = MutableStateFlow(0)
    val veryScore = MutableStateFlow(0)
    val veryShowResult = MutableStateFlow(false)
    val verySelectedOption = MutableStateFlow<String?>(null)

    // D: Scrambled Sentence Builder
    val scrambledIndex = MutableStateFlow(0)
    val scrambledSelectedWords = MutableStateFlow<List<String>>(emptyList())
    val scrambledScore = MutableStateFlow(0)
    val scrambledCompleted = MutableStateFlow(false)

    // E: Word Story Generator
    val selectedStoryWords = MutableStateFlow<List<String>>(emptyList())
    val storyTextInput = MutableStateFlow("")
    val storyResultFeedback = MutableStateFlow<String?>(null)
    val isAnalyzing = MutableStateFlow(false)

    // F: AI Notebook Quiz Game
    val aiQuizQuestions = MutableStateFlow<List<FillInBlankQuestion>>(emptyList())
    val aiQuizCurrentIndex = MutableStateFlow(0)
    val aiQuizScore = MutableStateFlow(0)
    val aiQuizUserAnswers = MutableStateFlow<List<Boolean>>(emptyList())
    val aiQuizCompleted = MutableStateFlow(false)
    val isAiQuizLoading = MutableStateFlow(false)
    val aiQuizError = MutableStateFlow<String?>(null)
    val aiQuizSelectedOption = MutableStateFlow<String?>(null)
    val aiQuizFeedbackMessage = MutableStateFlow<String?>(null)

    // Speak Recorder/Visualizer mock practice
    val activeInterviewQuestion = MutableStateFlow<InterviewTemplate?>(null)
    val isRecordingMock = MutableStateFlow(false)
    val recordingProgress = MutableStateFlow(0)
    val currentSpeechTranscript = MutableStateFlow("")
    val isSpeechAnalysisLoading = MutableStateFlow(false)
    val speechEvaluationResult = MutableStateFlow<SpeakingResultStructure?>(null)

    // Writing Practice
    val currentWritingPrompt = MutableStateFlow("Why is database security essential in modern AI development?")
    val writingInputText = MutableStateFlow("")
    val isWritingAnalysisLoading = MutableStateFlow(false)
    val writingEvaluationResult = MutableStateFlow<WritingResultStructure?>(null)

    // Active AI Coach chat state
    val aiCoachMessages = MutableStateFlow<List<CoachMessage>>(listOf(
        CoachMessage("coach", "Hello! I am your AI VocabQuest Coach. Let's work together to boost your English fluency, grammar, and USA mock interview skills. Ask me anything or suggest a practice topic!")
    ))
    val currentCoachInput = MutableStateFlow("")
    val isCoachResponding = MutableStateFlow(false)

    init {
        // Load default daily inputs
        selectedDailyInput.value = MockData.dailyInputs.first()
        
        // Check if user has profile, if database empty create default
        viewModelScope.launch {
            val existing = repository.getProfileDirect()
            if (existing != null) {
                isOnboardingCompleted.value = true
            } else {
                // Keep onboarding screen active
                isOnboardingCompleted.value = false
            }
        }
    }

    // --- CONTROLLER ACTIONS ---

    fun completeOnboarding(name: String, level: String, goals: List<String>, dailyTime: Int, topics: List<String>) {
        viewModelScope.launch {
            val prof = UserProfile(
                name = name.ifBlank { "Learner" },
                level = level,
                goals = goals.joinToString(","),
                dailyTimeLimit = dailyTime,
                favoriteTopics = topics.joinToString(","),
                streak = 1,
                lastActiveTimestamp = System.currentTimeMillis()
            )
            repository.saveProfile(prof)
            
            // Seed a few default vocab words so the notebook isn't empty and feels exciting!
            MockData.wordTemplates.take(4).forEach { template ->
                val vocab = VocabularyWord(
                    word = template.word,
                    meaning = template.meaning,
                    simpleExplanation = template.explanation,
                    wordType = template.wordType,
                    pronunciation = template.pronunciation,
                    exampleSentence = template.exampleSentence,
                    synonyms = template.synonyms.joinToString(";"),
                    antonyms = template.antonyms.joinToString(";"),
                    collocations = template.collocations.joinToString(";"),
                    topic = template.topic,
                    difficultyLevel = template.difficulty,
                    learnedAt = System.currentTimeMillis() - 86400000, // learned yesterday, due for review
                    nextReviewAt = System.currentTimeMillis() - 1000 // due for review
                )
                repository.insertVocabularyWord(vocab)
            }
            isOnboardingCompleted.value = true
        }
    }

    fun updateDailyDifficulty(difficulty: String) {
        selectedDifficulty.value = difficulty
        val found = MockData.dailyInputs.find { it.difficulty == difficulty }
        if (found != null) {
            selectedDailyInput.value = found
        } else {
            // Find fitting or fallback
            selectedDailyInput.value = MockData.dailyInputs.firstOrNull { it.topic == "Artificial Intelligence" }
        }
    }

    fun selectDailyInput(input: DailyInput) {
        selectedDailyInput.value = input
        selectedDifficulty.value = input.difficulty
    }

    // Context Tap word
    fun tapOnWordOfInput(wordText: String) {
        // Clean word text
        val cleaned = wordText.trim().lowercase().replace(Regex("[^a-zA-Z]"), "")
        // Find in our template set
        val template = MockData.wordTemplates.find { it.word.lowercase() == cleaned }
            ?: WordTemplate(
                word = cleaned,
                meaning = "able to adapt easily or use effectively",
                explanation = "Flexible or highly customizable component that handles shifting constraints.",
                wordType = "adjective",
                pronunciation = "flek-suh-buhl",
                topic = "General Education",
                difficulty = "B1",
                synonyms = listOf("adaptable", "adjustable"),
                antonyms = listOf("uncompromising", "narrow"),
                collocations = listOf("flexible workspace", "flexible performance"),
                exampleSentence = "The university library offers flexible study areas for global students."
            )
        tappedWord.value = template
        guessedStatus.value = null
        selectedGuessOption.value = null
    }

    fun submitContextGuess(selectedOption: String) {
        selectedGuessOption.value = selectedOption
        val current = tappedWord.value ?: return
        
        // Correct is flexible matching option, for simulation let's say option containing first 5 chars of meaning is correct
        val wordCl = current.meaning.lowercase()
        val correct = selectedOption.lowercase().contains(wordCl.take(6)) || 
                     wordCl.contains(selectedOption.lowercase().take(6)) ||
                     (selectedOption.lowercase().contains("change") && current.word == "flexible") ||
                     (selectedOption.lowercase().contains("automatically") && current.word == "automation") ||
                     (selectedOption.lowercase().contains("aid") && current.word == "scholarship") ||
                     (selectedOption.lowercase().contains("sure") && current.word == "confident") ||
                     (selectedOption.lowercase().contains("producers") && current.word == "obsolete") ||
                     (selectedOption.lowercase().contains("understand") && current.word == "intuitive")

        guessedStatus.value = correct
        
        // Reward review count XP
        if (correct) {
            addXPReward(10)
        } else {
            addXPReward(2)
        }
    }

    fun saveTappedWordToNotebook(userSentence: String = "") {
        val template = tappedWord.value ?: return
        viewModelScope.launch {
            val vocab = VocabularyWord(
                word = template.word,
                meaning = template.meaning,
                simpleExplanation = template.explanation,
                wordType = template.wordType,
                pronunciation = template.pronunciation,
                exampleSentence = template.exampleSentence,
                userSentence = userSentence,
                synonyms = template.synonyms.joinToString(";"),
                antonyms = template.antonyms.joinToString(";"),
                collocations = template.collocations.joinToString(";"),
                topic = template.topic,
                difficultyLevel = template.difficulty,
                learnedAt = System.currentTimeMillis(),
                nextReviewAt = System.currentTimeMillis() + 86400000 // schedule review tomorrow (1 day)
            )
            repository.insertVocabularyWord(vocab)
            addXPReward(15)
        }
    }

    fun deleteWordFromNotebook(word: VocabularyWord) {
        viewModelScope.launch {
            repository.deleteVocabularyWord(word)
        }
    }

    // Add manual custom word to dictionary notebook
    fun addWordManually(
        word: String,
        type: String,
        meaning: String,
        sentence: String,
        nativeWord: String = "",
        explanation: String = "",
        pronunciation: String = "",
        synonyms: String = "",
        antonyms: String = "",
        syllables: String = "",
        imageUrl: String = ""
    ) {
        viewModelScope.launch {
            val vocab = VocabularyWord(
                word = word.trim().lowercase(),
                meaning = meaning.trim(),
                simpleExplanation = explanation.trim().ifBlank { "Manually entered custom vocabulary." },
                nativeMeaning = nativeWord.trim(),
                wordType = type,
                pronunciation = pronunciation.trim().ifBlank { "custom pronunciation" },
                exampleSentence = sentence.trim().ifBlank { "No initial sentence provided." },
                synonyms = synonyms.trim(),
                antonyms = antonyms.trim(),
                topic = "Personal Custom",
                difficultyLevel = "B2",
                learnedAt = System.currentTimeMillis(),
                nextReviewAt = System.currentTimeMillis() + 86400000,
                syllables = syllables.trim(),
                imageUrl = imageUrl.trim()
            )
            repository.insertVocabularyWord(vocab)
            addXPReward(10)
        }
    }

    // Spaced Repetition Review Core Handler
    fun performReviewWord(word: VocabularyWord, isCorrect: Boolean) {
        viewModelScope.launch {
            // Calculate next schedule based on mastery
            val newMistakes = if (isCorrect) word.mistakes else word.mistakes + 1
            val newMastery = if (isCorrect) (word.masteryLevel + 1).coerceAtMost(5) else (word.masteryLevel - 1).coerceAtLeast(1)
            
            // SRS timing intervals: Mastery 1 = 1 day, 2 = 3 days, 3 = 7 days, 4 = 14 days, 5 = 30 days
            val multiplierSeed = when (newMastery) {
                1 -> 1
                2 -> 3
                3 -> 7
                4 -> 14
                5 -> 30
                else -> 1
            }
            val intervalMs = multiplierSeed.toLong() * 24L * 60L * 60L * 1000L
            val nextTime = System.currentTimeMillis() + intervalMs

            val updated = word.copy(
                mistakes = newMistakes,
                masteryLevel = newMastery,
                reviewCount = word.reviewCount + 1,
                nextReviewAt = nextTime
            )
            repository.insertVocabularyWord(updated)
            addXPReward(if (isCorrect) 12 else 4)
        }
    }


    // --- GAME ACTIONS ---

    // A: Fill In Blanks
    fun submitFillBlankAnswer(selectedIndex: Int) {
        val q = dynamicFillBlanksQuestions.value.getOrNull(fillBlanksIndex.value) ?: return
        val isCorrect = selectedIndex == q.correctIndex
        
        val newList = fillBlanksAnswers.value.toMutableList()
        newList.add(isCorrect)
        fillBlanksAnswers.value = newList

        if (isCorrect) {
            fillBlanksScore.value += 10
            addXPReward(10)
        } else {
            addXPReward(2)
        }

        if (fillBlanksIndex.value < dynamicFillBlanksQuestions.value.size - 1) {
            fillBlanksIndex.value += 1
        } else {
            fillBlanksShowResult.value = true
            // Save to DB
            saveGameResultToDB("FillBlanks", fillBlanksScore.value, "Mistakes: ${newList.count { !it }} of ${newList.size}")
        }
    }

    fun restartFillBlanks() {
        fillBlanksIndex.value = 0
        fillBlanksScore.value = 0
        fillBlanksShowResult.value = false
        fillBlanksAnswers.value = emptyList()
    }


    // B: Synonym Matcher
    fun selectSynonymMatchLeft(weakWord: String) {
        selectedSynonymWeak.value = weakWord
    }

    fun connectSynonymMatchRight(strongWord: String) {
        val weak = selectedSynonymWeak.value ?: return
        val currentPairs = dynamicSynonymPairs.value + MockData.synonymPairs + MockData.dontSayVery
        
        val correctPair = currentPairs.any { it.weak.lowercase() == weak.lowercase() && it.strong.lowercase() == strongWord.lowercase() }
        
        if (correctPair) {
            val updated = synonymMatches.value.toMutableMap()
            updated[weak] = strongWord
            synonymMatches.value = updated
            synonymScore.value += 15
            addXPReward(15)

            // Check if completed (has matched everything or 4 items)
            if (updated.size >= 4 || updated.size >= dynamicSynonymPairs.value.size) {
                synonymMatchingCompleted.value = true
                saveGameResultToDB("SynonymMatching", synonymScore.value, "Finished successfully")
            }
        } else {
            addXPReward(3) // small XP for trial
        }
        selectedSynonymWeak.value = null
    }

    fun restartSynonymMatching() {
        synonymMatches.value = emptyMap()
        selectedSynonymWeak.value = null
        synonymScore.value = 0
        synonymMatchingCompleted.value = false
    }


    // C: Don't Say Very
    fun selectVeryOption(option: String) {
        verySelectedOption.value = option
        val q = MockData.dontSayVery.getOrNull(veryIndex.value) ?: return
        val isCorrect = option.lowercase().trim() == q.strong.lowercase().trim() || 
                        q.strong.lowercase().contains(option.lowercase().trim())

        if (isCorrect) {
            veryScore.value += 10
            addXPReward(10)
        } else {
            addXPReward(2)
        }

        if (veryIndex.value < MockData.dontSayVery.size - 1) {
            veryIndex.value += 1
            verySelectedOption.value = null
        } else {
            veryShowResult.value = true
            saveGameResultToDB("WeakUpgrade", veryScore.value, "Upgraded very phrases")
        }
    }

    fun restartDonationVery() {
        veryIndex.value = 0
        veryScore.value = 0
        veryShowResult.value = false
        verySelectedOption.value = null
    }


    // D: Scrambled Sentence Builder
    fun selectScrambledWord(word: String) {
        val current = scrambledSelectedWords.value.toMutableList()
        current.add(word)
        scrambledSelectedWords.value = current

        // Check if correct length
        val q = MockData.scrambledSentences.getOrNull(scrambledIndex.value) ?: return
        if (current.size == q.scrambled.size) {
            val sentenceStr = current.joinToString(" ").trim()
            val cleanCorrect = q.correct.replace(".", "").trim()
            val cleanSubmit = sentenceStr.replace(".", "").trim()
            val isCorrect = cleanCorrect.equals(cleanSubmit, ignoreCase = true)

            if (isCorrect) {
                scrambledScore.value += 15
                addXPReward(15)
            } else {
                addXPReward(2)
            }

            if (scrambledIndex.value < MockData.scrambledSentences.size - 1) {
                scrambledIndex.value += 1
                scrambledSelectedWords.value = emptyList()
            } else {
                scrambledCompleted.value = true
                saveGameResultToDB("WordSentenceBuilder", scrambledScore.value, "Assembled custom sentences")
            }
        }
    }

    fun popScrambledWord() {
        val current = scrambledSelectedWords.value.toMutableList()
        if (current.isNotEmpty()) {
            current.removeAt(current.size - 1)
            scrambledSelectedWords.value = current
        }
    }

    fun restartScrambled() {
        scrambledIndex.value = 0
        scrambledSelectedWords.value = emptyList()
        scrambledScore.value = 0
        scrambledCompleted.value = false
    }


    // E: Word Story AI Game (Interactive feedback with Gemini)
    fun selectStoryWordToggle(word: String) {
        val curr = selectedStoryWords.value.toMutableList()
        if (curr.contains(word)) {
            curr.remove(word)
        } else {
            curr.add(word)
        }
        selectedStoryWords.value = curr
    }

    fun submitWordStoryForEvaluation() {
        val words = selectedStoryWords.value
        val text = storyTextInput.value
        if (words.isEmpty() || text.isBlank()) return

        isAnalyzing.value = true
        storyResultFeedback.value = "AI Coach is reading your funny story..."

        viewModelScope.launch {
            val prompt = """
                Evaluation task: Word Story Game!
                Keywords used: ${words.joinToString(", ")}
                Story text: "$text"
                
                Please review the story:
                1. Did they use all keywords naturally?
                2. Are the sentences correct?
                3. Present spelling, grammar, and naturalness feedback.
                4. Give an improved, funnier, more robust version of the story using all keywords!
                
                Respond in friendly, confidence-building markdown.
            """.trimIndent()

            val coachInstruction = "You are a friendly, encouraging English coach that helps non-native learners improve writing and word choice."

            val result = GeminiClient.generateContent(prompt, coachInstruction)
            
            if (result.startsWith("ERROR_MISSING_API_KEY")) {
                // Fallback offline analysis
                storyResultFeedback.value = """
                    📢 **Offline Feedback (AI API key is missing from Secrets)**
                    
                    **Keywords Found:** ${words.count { text.contains(it, ignoreCase = true) }} of ${words.size} 
                    
                    **Structure:** Your grammar is simple but very readable! Excellent attempt!
                    
                    **Coach's Upgraded Story:**
                    "During a visit to the USA college **campus**, I had the **opportunity** to secure a **flexible** graduate program that gave me a fully funded **scholarship**. Feeling incredibly **confident**, I thanked my professors!"
                    
                    *Tip: Add your Gemini API Key in the AI Studio Secrets panel to get deep customized grammar breakdowns!*
                """.trimIndent()
            } else {
                storyResultFeedback.value = result
            }
            saveGameResultToDB("WordStory", 20, "Created story with words: ${words.joinToString(",")}")
            addXPReward(20)
            isAnalyzing.value = false
        }
    }

    fun clearWordStory() {
        selectedStoryWords.value = emptyList()
        storyTextInput.value = ""
        storyResultFeedback.value = null
    }

    // --- F: AI NOTEBOOK QUIZ GENERATOR METHOD ---
    fun generateAiQuizFromNotebook() {
        aiQuizQuestions.value = emptyList()
        aiQuizCurrentIndex.value = 0
        aiQuizScore.value = 0
        aiQuizUserAnswers.value = emptyList()
        aiQuizCompleted.value = false
        isAiQuizLoading.value = true
        aiQuizError.value = null
        aiQuizSelectedOption.value = null
        aiQuizFeedbackMessage.value = null

        val savedList = vocabWords.value
        viewModelScope.launch {
            if (savedList.isEmpty()) {
                // If notebook is empty, generate from fallback standard word templates mapped to VocabularyWord objects
                val fallbackWords = MockData.wordTemplates.shuffled().take(4).map { template ->
                    VocabularyWord(
                        word = template.word,
                        meaning = template.meaning,
                        simpleExplanation = template.explanation,
                        wordType = template.wordType,
                        pronunciation = template.pronunciation,
                        exampleSentence = template.exampleSentence,
                        synonyms = template.synonyms.joinToString(";"),
                        antonyms = template.antonyms.joinToString(";"),
                        collocations = template.collocations.joinToString(";"),
                        topic = template.topic,
                        difficultyLevel = template.difficulty,
                        learnedAt = System.currentTimeMillis()
                    )
                }
                generateQuizFromWordList(fallbackWords, isOffline = true)
                isAiQuizLoading.value = false
                return@launch
            }

            val targetWords = savedList.shuffled().take(4)
            val info = targetWords.joinToString("\n") {
                "- Word: ${it.word} (${it.wordType}), Meaning: ${it.meaning}, Context: ${it.userSentence.ifBlank { it.exampleSentence }}"
            }

            val prompt = """
                Generate an English vocabulary multiple-choice quiz based ONLY on these vocabulary words:
                ${"$"}{info}
                
                Generate exactly 4 distinct multiple-choice questions. Each question must have:
                1. "sentenceWithBlank": A sentence testing comprehension where the word itself is replaced by empty blank "_____".
                2. "options": Exactly 4 plausible options, containing the correct word and 3 wrong distractors.
                3. "correctIndex": The 0-based index of the correct word in the options.
                4. "explanation": A friendly, helpful explanation of why the word is correct.

                Return a strict JSON format with NO markdown wrapping, matching this structure:
                {
                  "questions": [
                    {
                      "sentenceWithBlank": "A sentence testing context where _____ is correct.",
                      "options": ["wordA", "wordB", "wordC", "wordD"],
                      "correctIndex": 0,
                      "explanation": "Brief context explanation."
                    }
                  ]
                }
            """.trimIndent()

            val sysInstruction = "You are an English admissions and IELTS instructor. Respond in raw JSON format strictly matching the requested schema."
            val response = GeminiClient.generateContent(prompt, sysInstruction, responseJson = true)

            if (response.startsWith("ERROR_MISSING_API_KEY") || response.startsWith("ERROR_API") || response.startsWith("ERROR_EXCEPTION")) {
                // Offline fallback
                generateQuizFromWordList(targetWords, isOffline = true)
            } else {
                try {
                    val cleanJson = response.trim()
                        .removePrefix("```json")
                        .removeSuffix("```")
                        .trim()
                    val obj = JSONObject(cleanJson)
                    val array = obj.getJSONArray("questions")
                    val parsed = mutableListOf<FillInBlankQuestion>()
                    for (i in 0 until array.length()) {
                        val qObj = array.getJSONObject(i)
                        val optArr = qObj.getJSONArray("options")
                        val opts = mutableListOf<String>()
                        for (j in 0 until optArr.length()) {
                            opts.add(optArr.getString(j))
                        }
                        parsed.add(
                            FillInBlankQuestion(
                                id = i + 1,
                                sentenceWithBlank = qObj.getString("sentenceWithBlank"),
                                options = opts,
                                correctIndex = qObj.getInt("correctIndex"),
                                explanation = qObj.getString("explanation")
                            )
                        )
                    }
                    if (parsed.isNotEmpty()) {
                        aiQuizQuestions.value = parsed
                        aiQuizFeedbackMessage.value = "✨ AI designed 4 highly personalized questions from your Saved Notebook!"
                    } else {
                        generateQuizFromWordList(targetWords, isOffline = true)
                    }
                } catch (e: Exception) {
                    Log.e("VocabVM", "Failed parsing Gemini AI Quiz, falling back to local generation", e)
                    generateQuizFromWordList(targetWords, isOffline = true)
                }
            }
            isAiQuizLoading.value = false
        }
    }

    private fun generateQuizFromWordList(words: List<VocabularyWord>, isOffline: Boolean) {
        val list = mutableListOf<FillInBlankQuestion>()
        words.forEachIndexed { i, wordItem ->
            val cleanWord = wordItem.word.lowercase().trim()
            val baseSentence = if (wordItem.userSentence.isNotBlank()) wordItem.userSentence else wordItem.exampleSentence
            var sentenceWithBlank = baseSentence
            if (sentenceWithBlank.lowercase().contains(cleanWord)) {
                val start = sentenceWithBlank.lowercase().indexOf(cleanWord)
                sentenceWithBlank = sentenceWithBlank.substring(0, start) + "_____" + sentenceWithBlank.substring(start + cleanWord.length)
            } else {
                sentenceWithBlank = "The admissions officer was looking for the most _____ candidate with a strong study plan."
            }

            val pool = (vocabWords.value.map { it.word.lowercase() } + MockData.wordTemplates.map { it.word.lowercase() })
                .filter { it != cleanWord }
                .distinct()
                .shuffled()
            val distractors = pool.take(3)
            val finalOptions = (distractors + wordItem.word).shuffled()
            val correctIndex = finalOptions.indexOfFirst { it.lowercase() == cleanWord }.coerceAtLeast(0)

            list.add(
                FillInBlankQuestion(
                    id = i + 1,
                    sentenceWithBlank = sentenceWithBlank,
                    options = finalOptions,
                    correctIndex = correctIndex,
                    explanation = "Custom quiz question for '${wordItem.word}'. Meaning: ${wordItem.meaning}."
                )
            )
        }
        aiQuizQuestions.value = list
        aiQuizFeedbackMessage.value = if (isOffline) {
            "⚡ Generated 4 smart review questions offline from your vocabulary database!"
        } else {
            "✨ Quiz compiled successfully from your saved vocabulary words!"
        }
    }

    fun submitAiQuizAnswer(selectedIndex: Int) {
        val questionsList = aiQuizQuestions.value
        val currentIndex = aiQuizCurrentIndex.value
        val q = questionsList.getOrNull(currentIndex) ?: return

        val isCorrect = selectedIndex == q.correctIndex
        val answers = aiQuizUserAnswers.value.toMutableList()
        answers.add(isCorrect)
        aiQuizUserAnswers.value = answers

        if (isCorrect) {
            aiQuizScore.value += 15
            addXPReward(15)
        } else {
            addXPReward(3)
        }

        aiQuizSelectedOption.value = q.options.getOrNull(selectedIndex)

        viewModelScope.launch {
            delay(2000) // Show selection review for 2 seconds
            if (currentIndex < questionsList.size - 1) {
                aiQuizCurrentIndex.value = currentIndex + 1
                aiQuizSelectedOption.value = null
            } else {
                aiQuizCompleted.value = true
                saveGameResultToDB("AINotebookQuiz", aiQuizScore.value, "AI evaluated saved notebook words")
            }
        }
    }

    fun restartAiQuiz() {
        generateAiQuizFromNotebook()
    }


    // --- SPEAKING RECORDER & INTERVIEW COACH ---

    fun startRecordingSpeech() {
        isRecordingMock.value = true
        recordingProgress.value = 0
        currentSpeechTranscript.value = ""
        speechEvaluationResult.value = null
    }

    fun stopRecordingSpeechAndTranscribe(questionText: String, isInterviewMode: Boolean = false) {
        isRecordingMock.value = false
        isSpeechAnalysisLoading.value = true
        
        // Simulating transcription first
        val presetAnswers = mapOf(
            "Why do you want to study in the USA?" to "I want to study in USA because USA is good and universities give good study, also AI has many opportunities.",
            "Tell me about your future goals." to "My future goal is I want back to my country and do job as software engineer inside machine learning.",
            "Tell me about your future goals after finishing your graduate studies in America." to "My future goal is to study hard and return to my country to automate farms.",
            "Why did you choose Artificial Intelligence?" to "I chose AI because it is growing so fast, it is modern, and I want to solve health problems."
        )

        val text = presetAnswers[questionText] ?: "I want to improve my communication skills because I have a big university interview coming up and need a good scholarship."
        currentSpeechTranscript.value = text

        // Trigger Gemini API evaluation
        viewModelScope.launch {
            val modelPrompt = """
                Evaluate this English interview response:
                Question: "$questionText"
                User answer transcript: "$text"
                
                Return a JSON object matching this structure EXACTLY (do not wrap in markdown or add notes):
                {
                   "correctedText": "The corrected version of the user answer with standard punctuation",
                   "naturalText": "A warm, natural, native-sounding response suitable for IELTS/conversations",
                   "advancedText": "A professional, vocabulary-rich version tailored for USA university admissions",
                   "feedbackMarkdown": "Markdown checklist specifying grammar fixes, vocabulary tips, filler count detection, and pronunciation recommendations",
                   "fluencyScore": 85,
                   "grammarScore": 75,
                   "vocabScore": 80,
                   "confidenceScore": 90,
                   "fillerCount": 2,
                   "clarityScore": 85,
                   "wordsPerMinute": 115
                }
            """.trimIndent()

            val response = GeminiClient.generateContent(modelPrompt, "You are an English admissions interviewer. Return strict JSON details matching the schema.", responseJson = true)

            if (response.startsWith("ERROR_MISSING_API_KEY") || response.startsWith("ERROR_API") || response.startsWith("ERROR_EXCEPTION")) {
                // Offline fallback
                val naturalText = if (questionText.contains("USA")) {
                    "I want to study in the USA because it offers top-tier academic curriculum, practical laboratory research, and amazing industry opportunities."
                } else {
                    "My immediate postgraduate career objective is to return home and work in data analytics."
                }
                
                speechEvaluationResult.value = SpeakingResultStructure(
                    correctedText = text.replace("study in USA", "study in the USA").replace("give good study", "provide high-quality education"),
                    naturalText = naturalText,
                    advancedText = "My primary impetus for seeking enrollment in USA institutions is the robust emphasis on practical experimentation and the state-of-the-art facilities in Artificial Intelligence.",
                    feedbackMarkdown = """
                        📢 **Offline Feedback (API key has not been setup yet)**
                        
                        * **Grammar Correction:** Change 'study in USA' to 'study in **the** USA'. Use 'the' before country abbreviations.
                        * **Word Choice:** 'give good study' is awkward. Upgrade to '**provide premium education**' or '**offer rigorous coursework**'.
                        * **Filler Alert:** Detected 2 micro-pauses ('um'). Maintain steady airflow!
                        
                        *Tip: Configure your Gemini API key in the AI Studio Secrets panel for personalized, real-time feedback.*
                    """.trimIndent(),
                    fluencyScore = 80,
                    grammarScore = 70,
                    vocabScore = 75,
                    confidenceScore = 85,
                    fillerCount = 2,
                    clarityScore = 80,
                    wordsPerMinute = 120
                )
            } else {
                try {
                    // Extract JSON if wrapped in markdown coding blocks (Gemini sometimes adds blocks even with strict instruction)
                    val jsonStr = response.trim()
                        .removePrefix("```json")
                        .removeSuffix("```")
                        .trim()
                    
                    val obj = JSONObject(jsonStr)
                    speechEvaluationResult.value = SpeakingResultStructure(
                        correctedText = obj.getString("correctedText"),
                        naturalText = obj.getString("naturalText"),
                        advancedText = obj.getString("advancedText"),
                        feedbackMarkdown = obj.getString("feedbackMarkdown"),
                        fluencyScore = obj.optInt("fluencyScore", 80),
                        grammarScore = obj.optInt("grammarScore", 75),
                        vocabScore = obj.optInt("vocabScore", 75),
                        confidenceScore = obj.optInt("confidenceScore", 80),
                        fillerCount = obj.optInt("fillerCount", 0),
                        clarityScore = obj.optInt("clarityScore", 80),
                        wordsPerMinute = obj.optInt("wordsPerMinute", 120)
                    )
                } catch (e: Exception) {
                    Log.e("VocabVM", "Failed to parse speak evaluate JSON response, raw: $response", e)
                    // Safe parsing fallback
                    speechEvaluationResult.value = SpeakingResultStructure(
                        correctedText = text,
                        naturalText = "I want to study in the USA to pursue practical learning opportunities.",
                        advancedText = "My impetus for acquiring university placement in the United States resides in its academic rigor.",
                        feedbackMarkdown = "Your response is solid! Gemini analyzed your speech successfully, but returned a raw text structure:\n\n$response",
                        fluencyScore = 80,
                        grammarScore = 80,
                        vocabScore = 80,
                        confidenceScore = 80,
                        fillerCount = 1,
                        clarityScore = 80,
                        wordsPerMinute = 120
                    )
                }
            }

            // Save to DB
            val eval = speechEvaluationResult.value!!
            val speakPr = SpeakingPractice(
                question = questionText,
                transcript = text,
                feedback = eval.feedbackMarkdown,
                fluencyScore = eval.fluencyScore,
                grammarScore = eval.grammarScore,
                vocabularyScore = eval.vocabScore,
                confidenceScore = eval.confidenceScore
            )
            repository.saveSpeakingPractice(speakPr)
            addXPReward(25)
            isSpeechAnalysisLoading.value = false
        }
    }


    // --- WRITING PRACTICE ACTIONS ---

    fun submitWritingAnswer() {
        val prompt = currentWritingPrompt.value
        val ans = writingInputText.value
        if (ans.isBlank()) return

        isWritingAnalysisLoading.value = true
        writingEvaluationResult.value = null

        viewModelScope.launch {
            val evalPrompt = """
                Evaluate this short English writing exercise:
                Prompt: "$prompt"
                User answer: "$ans"
                
                Please return a JSON object with this matching schema strictly (no outer markdown codeblocks or notes):
                {
                  "correctedText": "Syntactically correct version of the user essay text",
                  "naturalText": "An elegant, naturally fluent version of the text",
                  "advancedText": "An advanced scholarship-level version of the writing response",
                  "feedbackMarkdown": "Detailed markdown bullet points detailing spelling errors, tenses, prepositions, linking words, and suggested style upgrades"
                }
            """.trimIndent()

            val response = GeminiClient.generateContent(evalPrompt, "You are a professional IELTS writing evaluator. Formulate raw JSON outputs.", responseJson = true)

            if (response.startsWith("ERROR_MISSING_API_KEY") || response.startsWith("ERROR_API") || response.startsWith("ERROR_EXCEPTION")) {
                writingEvaluationResult.value = WritingResultStructure(
                    correctedText = ans.replace("it give", "it gives").replace("want study", "want to study"),
                    naturalText = "Artificial Intelligence is growing rapidly. It provides intuitive software solutions to secure user databases, thereby preventing critical information leaks.",
                    advancedText = "The field of Artificial Intelligence is experiencing exponential expansion, offering streamlined, intuitive software schemas designed specifically to secure massive databases and neutralize unauthorized leak threats.",
                    feedbackMarkdown = """
                        📢 **Offline Feedback (API Key is missing or unavailable)**
                        
                        * **Subject-Verb Agreement:** Use '**it gives**' or '**it provides**', not 'it give'. 'It' is third-person singular!
                        * **Infinitives:** Use 'want **to** study' instead of 'want study'.
                        * **Vocabulary Tip:** Replace 'good security' with '**robust encryption**' or '**system resilience**'.
                        
                        *Tip: Setup your private Gemini API key in the AI Studio Secrets panel to analyze spelling, comma splices, and vocabulary.*
                    """.trimIndent()
                )
            } else {
                try {
                    val jsonStr = response.trim()
                        .removePrefix("```json")
                        .removeSuffix("```")
                        .trim()
                    
                    val obj = JSONObject(jsonStr)
                    writingEvaluationResult.value = WritingResultStructure(
                        correctedText = obj.getString("correctedText"),
                        naturalText = obj.getString("naturalText"),
                        advancedText = obj.getString("advancedText"),
                        feedbackMarkdown = obj.getString("feedbackMarkdown")
                    )
                } catch (e: Exception) {
                    Log.e("VocabVM", "Failed to parse writing JSON response, response was: $response", e)
                    writingEvaluationResult.value = WritingResultStructure(
                        correctedText = ans,
                        naturalText = "AI improves data security efficiently.",
                        advancedText = "Autonomous programs reinforce cryptographic integrity.",
                        feedbackMarkdown = "Excellent essay! Gemini processed your text successfully, but returned a format error; here is the text:\n\n$response"
                    )
                }
            }

            // Save to DB
            val eval = writingEvaluationResult.value!!
            val writePr = WritingPractice(
                prompt = prompt,
                userAnswer = ans,
                correctedAnswer = eval.correctedText,
                naturalVersion = eval.naturalText,
                advancedVersion = eval.advancedText,
                feedback = eval.feedbackMarkdown
            )
            repository.saveWritingPractice(writePr)
            addXPReward(25)
            isWritingAnalysisLoading.value = false
        }
    }

    fun selectWritingPrompt(prompt: String) {
        currentWritingPrompt.value = prompt
        writingInputText.value = ""
        writingEvaluationResult.value = null
    }


    // --- AI COACH CONVERSATIONAL MODE ---

    fun sendCoachMessage() {
        val text = currentCoachInput.value.trim()
        if (text.isBlank()) return

        val userMsg = CoachMessage("user", text)
        val list = aiCoachMessages.value.toMutableList()
        list.add(userMsg)
        aiCoachMessages.value = list

        currentCoachInput.value = ""
        isCoachResponding.value = true

        viewModelScope.launch {
            // Build conversation history for context
            val historyBuilder = StringBuilder()
            list.takeLast(10).forEach {
                if (it.sender == "user") {
                    historyBuilder.append("User English Learner: ${it.text}\n")
                } else {
                    historyBuilder.append("AI Coach: ${it.text}\n")
                }
            }

            val systemInstruction = """
                You are a lightning-fast, elite English Vocabulary and Visa Interview Coach. 
                Keep responses extremely concise, under 85 words. Avoid long greetings or conversational preambles. 
                Structure feedback cleanly with markdown bullet points. Highlight advanced vocabulary suggestions in bold like **this**, providing instant better replacements for the words the user used.
            """.trimIndent()

            val response = GeminiClient.generateContent(historyBuilder.toString(), systemInstruction)

            val replyText = if (response.startsWith("ERROR_MISSING_API_KEY")) {
                "Oh, I would love to explain that to you! However, a Gemini API Key is missing. Simply enter your key in the Secrets panel, and I can answer all of your grammar, vocabulary, and USA visa-interview questions dynamically! Until then, we can study standard vocab cards in the games section!"
            } else {
                response
            }

            val updatedWithCoach = aiCoachMessages.value.toMutableList()
            updatedWithCoach.add(CoachMessage("coach", replyText))
            aiCoachMessages.value = updatedWithCoach
            isCoachResponding.value = false
            addXPReward(5)
        }
    }


    // --- HELPERS ---

    private fun addXPReward(xpArgs: Int) {
        viewModelScope.launch {
            repository.addXP(xpArgs)
            // also trigger a streak update check
            val currentProfile = repository.getProfileDirect()
            if (currentProfile != null) {
                val now = System.currentTimeMillis()
                val diff = now - currentProfile.lastActiveTimestamp
                // If it's a new day, increment streak
                if (diff > 86400000L) {
                    val currentStreak = currentProfile.streak
                    repository.updateStreak(currentStreak + 1, now)
                } else {
                    repository.updateStreak(currentProfile.streak, now)
                }
            }
        }
    }

    private fun saveGameResultToDB(gameType: String, score: Int, detailStr: String) {
        viewModelScope.launch {
            val result = GameResult(
                gameType = gameType,
                score = score,
                mistakes = detailStr
            )
            repository.saveGameResult(result)
        }
    }
}

// --- VIEWMODEL HELPER DATA STRUCTURES ---

data class SpeakingResultStructure(
    val correctedText: String,
    val naturalText: String,
    val advancedText: String,
    val feedbackMarkdown: String,
    val fluencyScore: Int,
    val grammarScore: Int,
    val vocabScore: Int,
    val confidenceScore: Int,
    val fillerCount: Int,
    val clarityScore: Int,
    val wordsPerMinute: Int
)

data class WritingResultStructure(
    val correctedText: String,
    val naturalText: String,
    val advancedText: String,
    val feedbackMarkdown: String
)

data class CoachMessage(
    val sender: String, // "user" or "coach"
    val text: String,
    val timestamp: Long = System.currentTimeMillis()
)
