package asu.gunma.DbContainers;


/**
 * The VocabWord class establishes a container that is used to define the
 * SQLite Table layout with a table name and corresponding column names.
 *
 * @author pdunlavey
 * @version 1.0
 * @date 10-22-18
 */
public class VocabWord {
    public static final String TABLE_NAME = "Vocab";

    public static final String COLUMN_KANJI = "TargetVocabularyKanji";
    public static final String COLUMN_KANA = "TargetVocabularyKana";
    public static final String COLUMN_ENG = "TargetVocabulary";
    public static final String COLUMN_MODULE = "Category";
    public static final String COLUMN_CORRECT_WORDS = "CorrectWords";
    public static final String COLUMN_AUDIO = "Audio";

    public static final String SQLITE_CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_KANJI + " TEXT," +
                    COLUMN_KANA + " TEXT," +
                    COLUMN_ENG + " TEXT," +
                    COLUMN_MODULE + " TEXT," +
                    COLUMN_CORRECT_WORDS + " TEXT," +
                    COLUMN_AUDIO + " TEXT" + ")";

    public static final String SQLITE_DELETE_TABLE =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

    private String kanjiSpelling;
    private String kanaSpelling;
    private String engSpelling;
    private String moduleCategory;
    private String correctWords;
    private String audio;

    public VocabWord() {
    }

    public VocabWord(String kanjiSpelling, String kanaSpelling, String engSpelling,
                     String moduleCategory, String correctWords, String audio) {
        this.kanjiSpelling = kanjiSpelling;
        this.kanaSpelling = kanaSpelling;
        this.engSpelling = engSpelling;
        this.moduleCategory = moduleCategory;
        this.correctWords = correctWords;
        this.audio = audio;
    }

    public String getKanjiSpelling() {
        return kanjiSpelling;
    }

    public void setKanjiSpelling(String kanjiSpelling) {
        this.kanjiSpelling = kanjiSpelling;
    }

    public String getKanaSpelling() {
        return kanaSpelling;
    }

    public void setKanaSpelling(String kanaSpelling) {
        this.kanaSpelling = kanaSpelling;
    }

    public String getEngSpelling() {
        return engSpelling;
    }

    public void setEngSpelling(String engSpelling) {
        this.engSpelling = engSpelling;
    }

    public void setModuleCategory(String moduleCategory) {
        this.moduleCategory = moduleCategory;
    }

    public String getModuleCategory() {
        return moduleCategory;
    }

    public String getCorrectWords() { return correctWords; }

    public void setCorrectWords(String correctWords) {
        this.correctWords = correctWords;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

    public String getAudio() { return audio; }

}
