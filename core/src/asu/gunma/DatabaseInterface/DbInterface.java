package asu.gunma.DatabaseInterface;

import java.util.List;

import asu.gunma.DbContainers.VocabWord;

public interface DbInterface {
    public List<VocabWord> getDbVocab();
}
