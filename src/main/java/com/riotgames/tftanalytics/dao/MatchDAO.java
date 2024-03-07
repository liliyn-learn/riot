package com.riotgames.tftanalytics.dao;

import javax.persistence.RollbackException;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.riotgames.tftanalytics.bean.Match;
import com.riotgames.tftanalytics.bean.Match;


public class MatchDAO extends DAO {
	
	public MatchDAO() {
		super();
	}

    public void save(Match e) {
        try (Session session = factory.openSession()) {
            Transaction t = session.beginTransaction();
            try {
                session.save(e);
                t.commit();
            } catch (Exception ex) {
                t.rollback();
                ex.printStackTrace();
            }
        }
    }
    
    public Match get(int id) {
        Match e = null;
        try (Session session = factory.openSession()) {
            Transaction t = session.beginTransaction();
            try {
                e = (Match) session.get(Match.class, id);
                t.commit();
            } catch (Exception ex) {
                t.rollback();
                ex.printStackTrace();
            }
        }
        return e;
    }
}
