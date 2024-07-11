package model.dao;

import model.beans.Argomento;

import java.sql.Connection;
import java.util.ArrayList;

public interface ArgomentoDao {
    void delete(int id);
    Argomento findById(int id);
    ArrayList<Argomento> findByNome(String nome);
    ArrayList<Argomento> findAllByLezioneId(int idLezione);
    int countAllByCorsoId(int idCorso);
    public ArrayList<String> findFiles(int id);
    public void updateOrInsertFile(Connection conn, String newFilename, int idArgomento);
}

