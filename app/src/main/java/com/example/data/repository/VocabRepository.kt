package com.example.data.repository

import com.example.data.database.*
import kotlinx.coroutines.flow.Flow

class VocabRepository(private val db: AppDatabase) {
    private val profileDao = db.userProfileDao()
    private val vocabDao = db.vocabularyDao()
    private val practiceDao = db.practiceDao()
    private val gameDao = db.gameResultDao()

    // --- User Profile ---
    val userProfile: Flow<UserProfile?> = profileDao.getUserProfileFlow()

    suspend fun getProfileDirect(): UserProfile? = profileDao.getUserProfile()
    
    suspend fun saveProfile(profile: UserProfile) {
        profileDao.insertOrUpdateProfile(profile)
    }

    suspend fun addXP(xp: Int) {
        profileDao.addXP(xp)
    }

    suspend fun updateStreak(streak: Int, timestamp: Long) {
        profileDao.updateStreak(streak, timestamp)
    }

    // --- Vocabulary ---
    val allWords: Flow<List<VocabularyWord>> = vocabDao.getAllWordsFlow()
    val wordCount: Flow<Int> = vocabDao.getWordCountFlow()
    val masteredWordCount: Flow<Int> = vocabDao.getMasteredWordCountFlow()

    fun getWordsForReview(now: Long): Flow<List<VocabularyWord>> {
        return vocabDao.getWordsForReviewFlow(now)
    }

    suspend fun getWord(word: String): VocabularyWord? {
        return vocabDao.getWord(word)
    }

    suspend fun insertVocabularyWord(word: VocabularyWord) {
        vocabDao.insertWord(word)
    }

    suspend fun deleteVocabularyWord(word: VocabularyWord) {
        vocabDao.deleteWord(word)
    }

    // --- Practices ---
    val allSpeakingPractices: Flow<List<SpeakingPractice>> = practiceDao.getAllSpeakingPractices()
    val allWritingPractices: Flow<List<WritingPractice>> = practiceDao.getAllWritingPractices()

    suspend fun saveSpeakingPractice(practice: SpeakingPractice) {
        practiceDao.insertSpeakingPractice(practice)
    }

    suspend fun saveWritingPractice(practice: WritingPractice) {
        practiceDao.insertWritingPractice(practice)
    }

    // --- Game Results ---
    val allGameResults: Flow<List<GameResult>> = gameDao.getAllResults()

    suspend fun saveGameResult(result: GameResult) {
        gameDao.insertResult(result)
    }
}
