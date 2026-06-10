package com.example.data

// --- DATA STRUCTURES ---

data class DailyInput(
    val id: String,
    val title: String,
    val text: String,
    val topic: String,
    val difficulty: String, // Beginner, Intermediate, Advanced
    val keyWords: List<String>,
    val author: String = "VocabQuest Coach"
)

data class TopicData(
    val categoryName: String,
    val iconName: String, // name of icon
    val importantWords: List<WordTemplate>,
    val speakingQuestion: String,
    val writingPrompt: String,
    val suggestedAnswer: String
)

data class WordTemplate(
    val word: String,
    val meaning: String,
    val explanation: String,
    val wordType: String,
    val pronunciation: String,
    val topic: String,
    val difficulty: String,
    val synonyms: List<String>,
    val antonyms: List<String>,
    val collocations: List<String>,
    val exampleSentence: String
)

data class FillInBlankQuestion(
    val id: Int,
    val sentenceWithBlank: String, // Contains "_____"
    val options: List<String>,
    val correctIndex: Int,
    val explanation: String
)

data class SynonymPair(
    val weak: String,
    val strong: String,
    val example: String
)

data class InterviewTemplate(
    val topic: String,
    val question: String,
    val simpleSample: String,
    val naturalSample: String,
    val advancedSample: String,
    val keyVocabulary: List<String>,
    val tips: String
)

data class ScrambledSentence(
    val id: Int,
    val scrambled: List<String>,
    val correct: String,
    val hint: String
)

data class PictureTask(
    val id: Int,
    val title: String,
    val descriptionPrompt: String,
    val imageUrl: String, // Coil loadable, or mock drawable/desc
    val suggestedKeywords: List<String>
)


// --- RAW MOCK DATA PROVIDER ---

object MockData {

    val dailyInputs = listOf(
        DailyInput(
            id = "di_1",
            title = "The Evolution of AI",
            text = "Artificial Intelligence is shifting from simple spreadsheets to system automation. Modern data science offers flexible research tools that help search databases instantly. Many universities provide scholarships for students who are passionate about data privacy and building intuitive software systems.",
            topic = "Artificial Intelligence",
            difficulty = "Intermediate",
            keyWords = listOf("automation", "flexible", "scholarships", "passionate", "intuitive")
        ),
        DailyInput(
            id = "di_2",
            title = "Academic Journey in the USA",
            text = "Studying abroad presents an outstanding campus opportunity for global candidates. Graduate degrees demand top-tier academic performance and consistent research efforts under senior professors. Choosing a flexible learning curriculum is beneficial because busy students can easily adjust their schedule.",
            topic = "USA Study",
            difficulty = "Intermediate",
            keyWords = listOf("opportunity", "academic performance", "campus", "curriculum", "beneficial")
        ),
        DailyInput(
            id = "di_3",
            title = "Why Data Privacy Matters",
            text = "Data privacy is essential in today's tech campus. Obsolete software systems often lead to leaks, making security upgrades a major priority for technology professionals. With the rise of virtual assistants, keeping data safe has become extremely challenging yet extremely vital for business.",
            topic = "Technology",
            difficulty = "Advanced",
            keyWords = listOf("essential", "obsolete", "leaks", "challenging", "priority")
        ),
         DailyInput(
            id = "di_4",
            title = "Preparing for Visa Interviews",
            text = "A visa interview requires a confident presentation of your personal goals. Candidates must explain their sponsor details and academic purpose with honest answers. Having a clear plan of returning is critical because it displays stability and genuine motivation.",
            topic = "Visa Interview",
            difficulty = "Beginner",
            keyWords = listOf("confident", "purpose", "honest", "sponsor", "motivation")
        )
    )

    val wordTemplates = listOf(
        WordTemplate(
            word = "flexible",
            meaning = "able to change or adjust easily",
            explanation = "Can be modified or adjusted according to circumstances without breaking or causing conflict.",
            wordType = "adjective",
            pronunciation = "flek-suh-buhl",
            topic = "Education / Interview",
            difficulty = "B1",
            synonyms = listOf("adaptable", "adjustable", "elastic"),
            antonyms = listOf("fixed", "rigid", "unbending"),
            collocations = listOf("flexible schedule", "flexible working hours", "flexible study plan"),
            exampleSentence = "Online learning is flexible because students can study at any time."
        ),
        WordTemplate(
            word = "intuitive",
            meaning = "easy to understand or use without special training",
            explanation = "Understood or operated naturally or through instruction-free instincts.",
            wordType = "adjective",
            pronunciation = "in-too-uh-tiv",
            topic = "Technology",
            difficulty = "C1",
            synonyms = listOf("user-friendly", "natural", "instinctive"),
            antonyms = listOf("complex", "convoluted", "unnatural"),
            collocations = listOf("intuitive design", "intuitive software", "intuitive layout"),
            exampleSentence = "The smartphone's interface is so intuitive that even a child can use it."
        ),
        WordTemplate(
            word = "obsolete",
            meaning = "no longer produced or used; out of date",
            explanation = "Replaced by something newer, more efficient, and more modern.",
            wordType = "adjective",
            pronunciation = "ob-suh-leet",
            topic = "Technology",
            difficulty = "B2",
            synonyms = listOf("outdated", "antique", "expired"),
            antonyms = listOf("modern", "current", "state-of-the-art"),
            collocations = listOf("obsolete software", "obsolete technology", "obsolete guidelines"),
            exampleSentence = "Floppy disks became obsolete after USB drives were introduced."
        ),
        WordTemplate(
            word = "scholarship",
            meaning = "financial aid given to a student for their education",
            explanation = "A grant or payment made to support a student's education, normally awarded on academic or athletic merit.",
            wordType = "noun",
            pronunciation = "skol-er-ship",
            topic = "University Life",
            difficulty = "A2",
            synonyms = listOf("grant", "fellowship", "financial aid"),
            antonyms = listOf("tuition bill", "student loan"),
            collocations = listOf("academic scholarship", "full scholarship", "apply for scholarship"),
            exampleSentence = "She received a full scholarship to study computer science at MIT."
        ),
        WordTemplate(
            word = "confident",
            meaning = "feeling sure about oneself and one's abilities",
            explanation = "Having complete trust or certainty about something, or showing self-assurance.",
            wordType = "adjective",
            pronunciation = "kon-fuh-dent",
            topic = "Communication",
            difficulty = "A2",
            synonyms = listOf("assured", "certain", "self-reliant"),
            antonyms = listOf("insecure", "doubtful", "timid"),
            collocations = listOf("confident smile", "feel confident", "confident posture"),
            exampleSentence = "Always make direct eye contact and display a confident attitude during the interview."
        ),
        WordTemplate(
            word = "automation",
            meaning = "the use of machines/computers to do work automatically",
            explanation = "The execution of processes or procedures by technological system controls, reducing human labor.",
            wordType = "noun",
            pronunciation = "aw-tuh-mey-shuhn",
            topic = "Artificial Intelligence",
            difficulty = "B2",
            synonyms = listOf("computerization", "mechanization", "industrialization"),
            antonyms = listOf("manual labor", "handcrafting"),
            collocations = listOf("industrial automation", "process automation", "marketing automation"),
            exampleSentence = "In computer science, automation helper scripts save time by doing repeated chores."
        )
    )

    val topicsData = listOf(
        TopicData(
            categoryName = "Education",
            iconName = "school",
            importantWords = wordTemplates.filter { it.topic.contains("Education") || it.topic.contains("University") },
            speakingQuestion = "Why do you want to pursue higher education in the USA?",
            writingPrompt = "Explain the advantages of practical learning vs. theoretical memorization.",
            suggestedAnswer = "I want to study in the USA because it offers advanced education, practical lab learning, and unmatched research opportunities. This curriculum will help me build a top-tier career."
        ),
        TopicData(
            categoryName = "Technology & AI",
            iconName = "smart_toy",
            importantWords = wordTemplates.filter { it.topic.contains("Technology") || it.topic.contains("Intelligence") },
            speakingQuestion = "How do you think Artificial Intelligence is changing your chosen field?",
            writingPrompt = "Discuss the ethical challenges of database leaks and user data privacy.",
            suggestedAnswer = "Artificial Intelligence improves productivity through extensive automation. By using intuitive software and fast algorithms, we can solve complex health, financial, and environmental problems."
        ),
        TopicData(
            categoryName = "USA Interview",
            iconName = "gavel",
            importantWords = wordTemplates.filter { it.topic.contains("Interview") || it.topic.contains("Communication") },
            speakingQuestion = "Tell me about your career plans after finishing your graduate studies in America.",
            writingPrompt = "Detail your financial sponsor structure and ties returning to your home country.",
            suggestedAnswer = "My sponsor, who works in medical billing, is fully funding my study trip. After graduation, I intend to return immediately to introduce automated software solutions to local industries."
        )
    )

    val fillInTheBlanks = listOf(
        FillInBlankQuestion(
            id = 1,
            sentenceWithBlank = "I want to _____ my English speaking skills before my visa interview.",
            options = listOf("improve", "challenge", "obsolete", "flexible"),
            correctIndex = 0,
            explanation = "We need a verb that means to make better. 'Improve' fits perfectly here."
        ),
        FillInBlankQuestion(
            id = 2,
            sentenceWithBlank = "The USA offers students many unique research _____ on campus.",
            options = listOf("sponsors", "opportunities", "performances", "materials"),
            correctIndex = 1,
            explanation = "An 'opportunity' is a set of circumstances that makes it possible to do something."
        ),
        FillInBlankQuestion(
            id = 3,
            sentenceWithBlank = "My study schedule is very _____ because I can study morning, afternoon, or evening.",
            options = listOf("obsolete", "rigid", "flexible", "leaked"),
            correctIndex = 2,
            explanation = "A schedule that is adaptable and easy to adjust or change is 'flexible'."
        ),
        FillInBlankQuestion(
            id = 4,
            sentenceWithBlank = "Always maintain good eye contact to look more _____ in front of professors.",
            options = listOf("intuitive", "confident", "obsolete", "passive"),
            correctIndex = 1,
            explanation = "'Confident' means feeling self-assured and certain about your abilities."
        ),
        FillInBlankQuestion(
            id = 5,
            sentenceWithBlank = "Artificial Intelligence is a practical field because it helps solve _____ problems.",
            options = listOf("obsolete", "theoretical", "real-world", "imaginary"),
            correctIndex = 2,
            explanation = "'Real-world' problems exist inside practical scenarios, in contrast to purely theoretical math."
        )
    )

    val synonymPairs = listOf(
        SynonymPair("good", "beneficial", "Regular mock practice is beneficial for fluency."),
        SynonymPair("bad", "harmful", "Using fillers constantly is harmful to interview delivery."),
        SynonymPair("chance", "opportunity", "This scholarship represents a rare opportunity."),
        SynonymPair("change", "modify", "I need to modify my personal introduction answer."),
        SynonymPair("easy to use", "intuitive", "The campus app has a highly intuitive layout."),
        SynonymPair("old / useless", "obsolete", "Don't depend on obsolete interview frameworks.")
    )

    val dontSayVery = listOf(
        SynonymPair("very good", "excellent", "The student gave an excellent self-introduction."),
        SynonymPair("very important", "essential", "Understanding your project details is essential."),
        SynonymPair("very useful", "valuable / practical", "Her advice on visas was incredibly valuable."),
        SynonymPair("very hard", "challenging", "Explaining complex AI code can be challenging."),
        SynonymPair("very easy", "simple", "Forming standard sentences is simple with practice."),
        SynonymPair("very sure", "confident", "I feel confident about my scholarship chances.")
    )

    val scrambledSentences = listOf(
        ScrambledSentence(
            id = 1,
            scrambled = listOf("USA", "study", "I", "want", "in", "to", "the"),
            correct = "I want to study in the USA.",
            hint = "Express your desire to study in America. Remember 'the' goes with 'USA'."
        ),
        ScrambledSentence(
            id = 2,
            scrambled = listOf("Artificial Intelligence", "real-world problems", "helps", "solve", "in", "education"),
            correct = "Artificial Intelligence helps solve real-world problems in education.",
            hint = "Focus on the main subject (AI), followed by what it does (helps solve), and where."
        ),
         ScrambledSentence(
            id = 3,
            scrambled = listOf("my sponsor", "bills", "finances", "funding", "all", "is", "fully"),
            correct = "My sponsor is fully funding all my finances.",
            hint = "Start with the sponsor as the active agent, followed by status verb (is funding)."
        )
    )

    val pictureTasks = listOf(
        PictureTask(
            id = 1,
            title = "University Campus",
            descriptionPrompt = "A beautiful classic university campus with a brick library building, vast green lawns, and college students sitting around discussing assignments under safe sunny weather.",
            imageUrl = "https://images.unsplash.com/photo-1541339907198-e08756dedf3f?w=600&auto=format&fit=crop&q=80",
            suggestedKeywords = listOf("campus", "professors", "assignments", "academic performance", "scholarship")
        ),
        PictureTask(
            id = 2,
            title = "Group Discussion / Discussion Lab",
            descriptionPrompt = "A modern tech class workspace with laptop computers, whiteboard diagrams displaying data structures, and multi-cultural students brainstorming together confidently.",
            imageUrl = "https://images.unsplash.com/photo-1522071820081-009f0129c71c?w=600&auto=format&fit=crop&q=80",
            suggestedKeywords = listOf("intuitive", "automation", "cooperation", "practical learning", "AI")
        )
    )

    val interviewQuestions = listOf(
        InterviewTemplate(
            topic = "USA Study Motivation",
            question = "Why do you want to study in the USA?",
            simpleSample = "I want study in USA because USA is good and universities give good study.",
            naturalSample = "I want to study in the USA because it offers very high-quality education and good opportunities in technology.",
            advancedSample = "I am eager to pursue my education in the USA due to its highly advanced academic standards, focus on practical learning, and access to peerless research laboratories in computer science.",
            keyVocabulary = listOf("advanced academic standards", "practical learning", "access to research"),
            tips = "Never say 'because USA is famous'. Focus on the industry-level research in your major, practical labs, and lack of similar advanced coursework in your home country."
        ),
        InterviewTemplate(
            topic = "Program Selection",
            question = "Why did you choose Artificial Intelligence?",
            simpleSample = "I like AI because AI is modern and I want to write computer programs.",
            naturalSample = "I chose Artificial Intelligence because it is growing fast and there are many job opportunities in it.",
            advancedSample = "I decided on Artificial Intelligence because of its immense capacity to automate redundant tasks and solve complex real-world challenges in industries ranging from healthcare to finance.",
            keyVocabulary = listOf("immense capacity", "solve real-world challenges", "automate redundant tasks"),
            tips = "Tie Artificial Intelligence back to your undergraduate final year project or local automation challenges that you are passionate about addressing."
        ),
        InterviewTemplate(
            topic = "Future Career Goals",
            question = "What are your future goals after graduation?",
            simpleSample = "I want simple job and live happily with my computer skills.",
            naturalSample = "My goal is to return home and work as a data scientist in a big local company.",
            advancedSample = "My immediate post-graduation objective is to return to my home country and integrate autonomous technology inside our local agricultural systems to optimize yield and resource distribution.",
            keyVocabulary = listOf("post-graduation objective", "autonomous technology", "optimize resource distribution"),
            tips = "You must articulate a clear homecoming intent. USA non-immigrant visas require indicating strong ties that bring you back after finishing studies."
        )
    )
}
