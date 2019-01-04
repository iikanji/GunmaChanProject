package asu.gunma.speech;

import asu.gunma.DatabaseInterface.DbInterface;
import asu.gunma.DbContainers.*;

public class DbCallback implements DbInterface {
    protected VocabWord inputWord;
    protected StudentMetric studentMetric;
    protected Instructor instructor;

    public VocabWord getGameVocabWord(){
        return inputWord;
    }
    public void setGameVocabWord(VocabWord vocabWord){
        inputWord = vocabWord;
    }

    public StudentMetric getGameStudentMetric(){
        return studentMetric;
    }
    public void setGameStudentMetric(StudentMetric sMetric){
        studentMetric = sMetric;
    }

    public Instructor getGameInstructor(){
        return instructor;
    }
    public void setGameInstructor(Instructor instructor){
        this.instructor = instructor;
    }

}
