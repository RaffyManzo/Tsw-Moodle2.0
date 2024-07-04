package model.dao;

import model.beans.Lezione;

import java.util.ArrayList;

public interface LezioneDao {
    void delete(int id);

    Lezione findById(int id);

    ArrayList<Lezione> findByTitolo(String titolo);

    ArrayList<Lezione> findAllByCorsoId(int idCorso);
}
