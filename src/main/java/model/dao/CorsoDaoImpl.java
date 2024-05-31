package model.dao;

import model.beans.Corso;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CorsoDaoImpl extends AbstractDataAccessObject<Corso> implements CorsoDao{
    @Override
    protected Corso extractFromResultSet(ResultSet rs) throws SQLException {
        return null;
    }

    @Override
    protected void insertInto(Corso corso) throws SQLException {

    }

    @Override
    protected <E> void update(E e) {

    }
}
