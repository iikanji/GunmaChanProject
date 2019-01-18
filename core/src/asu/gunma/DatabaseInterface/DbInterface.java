package asu.gunma.DatabaseInterface;

import asu.gunma.DbContainers.Instructor;
import asu.gunma.DbContainers.StudentMetric;
import asu.gunma.DbContainers.VocabWord;

public interface DbInterface {
    public VocabWord getGameVocabWord();
    public void setGameVocabWord(VocabWord vocabWord);
    public StudentMetric getGameStudentMetric();
    public void setGameStudentMetric(StudentMetric sMetric);
    public Instructor getGameInstructor();
    public void setGameInstructor(Instructor instructor);
}
