package com.example.data.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

// --- ENTITIES ---

@Entity(tableName = "user_profiles")
data class UserProfile(
    @PrimaryKey val id: String = "current_user",
    val name: String,
    val level: String, // Beginner, Intermediate, Advanced
    val goals: String, // Comma-separated: Interview, DET, IELTS, etc.
    val dailyTimeLimit: Int, // 15, 30, 60 minutes
    val favoriteTopics: String, // Comma-separated
    val streak: Int = 0,
    val totalXP: Int = 0,
    val lastActiveTimestamp: Long = 0L,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "vocabulary_words")
data class VocabularyWord(
    @PrimaryKey val word: String, // Unique word itself
    val meaning: String,
    val simpleExplanation: String,
    val nativeMeaning: String = "", // Urdu / other
    val wordType: String, // noun, verb, adjective, adverb, phrase
    val pronunciation: String,
    val exampleSentence: String,
    val userSentence: String = "",
    val synonyms: String = "", // Semicolon-separated
    val antonyms: String = "", // Semicolon-separated
    val collocations: String = "", // Semicolon-separated
    val topic: String,
    val difficultyLevel: String, // A1, A2, B1, B2, C1, C2
    val learnedAt: Long = System.currentTimeMillis(),
    val nextReviewAt: Long = System.currentTimeMillis(),
    val reviewCount: Int = 0,
    val masteryLevel: Int = 1, // 1 to 5
    val mistakes: Int = 0
)

@Entity(tableName = "speaking_practices")
data class SpeakingPractice(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val question: String,
    val transcript: String,
    val feedback: String,
    val fluencyScore: Int,
    val grammarScore: Int,
    val vocabularyScore: Int,
    val confidenceScore: Int,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "writing_practices")
data class WritingPractice(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val prompt: String,
    val userAnswer: String,
    val correctedAnswer: String,
    val naturalVersion: String,
    val advancedVersion: String,
    val feedback: String,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "game_results")
data class GameResult(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val gameType: String, // FillBlanks, SynonymMatching, WeakUpgrade, WordStory, WordSentenceBuilder
    val score: Int,
    val mistakes: String, // Semicolon-separated or description
    val completedAt: Long = System.currentTimeMillis()
)


// --- DAOS ---

@Dao
interface UserProfileDao {
    @Query("SELECT * FROM user_profiles WHERE id = 'current_user' LIMIT 1")
    fun getUserProfileFlow(): Flow<UserProfile?>

    @Query("SELECT * FROM user_profiles WHERE id = 'current_user' LIMIT 1")
    suspend fun getUserProfile(): UserProfile?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateProfile(profile: UserProfile)

    @Query("UPDATE user_profiles SET totalXP = totalXP + :xp WHERE id = 'current_user'")
    suspend fun addXP(xp: Int)

    @Query("UPDATE user_profiles SET streak = :streak, lastActiveTimestamp = :timestamp WHERE id = 'current_user'")
    suspend fun updateStreak(streak: Int, timestamp: Long)
}

@Dao
interface VocabularyDao {
    @Query("SELECT * FROM vocabulary_words ORDER BY learnedAt DESC")
    fun getAllWordsFlow(): Flow<List<VocabularyWord>>

    @Query("SELECT * FROM vocabulary_words WHERE word = :word LIMIT 1")
    suspend fun getWord(word: String): VocabularyWord?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWord(word: VocabularyWord)

    @Delete
    suspend fun deleteWord(word: VocabularyWord)

    @Query("SELECT * FROM vocabulary_words WHERE nextReviewAt <= :now ORDER BY nextReviewAt ASC")
    fun getWordsForReviewFlow(now: Long): Flow<List<VocabularyWord>>

    @Query("SELECT COUNT(*) FROM vocabulary_words")
    fun getWordCountFlow(): Flow<Int>

    @Query("SELECT COUNT(*) FROM vocabulary_words WHERE masteryLevel >= 4")
    fun getMasteredWordCountFlow(): Flow<Int>
}

@Dao
interface PracticeDao {
    @Query("SELECT * FROM speaking_practices ORDER BY createdAt DESC")
    fun getAllSpeakingPractices(): Flow<List<SpeakingPractice>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSpeakingPractice(practice: SpeakingPractice)

    @Query("SELECT * FROM writing_practices ORDER BY createdAt DESC")
    fun getAllWritingPractices(): Flow<List<WritingPractice>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWritingPractice(practice: WritingPractice)
}

@Dao
interface GameResultDao {
    @Query("SELECT * FROM game_results ORDER BY completedAt DESC")
    fun getAllResults(): Flow<List<GameResult>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertResult(result: GameResult)
}


// --- DATABASE ---

@Database(
    entities = [
        UserProfile::class,
        VocabularyWord::class,
        SpeakingPractice::class,
        WritingPractice::class,
        GameResult::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userProfileDao(): UserProfileDao
    abstract fun vocabularyDao(): VocabularyDao
    abstract fun practiceDao(): PracticeDao
    abstract fun gameResultDao(): GameResultDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: android.content.Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = androidx.room.Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "vocabquest_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
