package com.riotgames.tftanalytics.dao;

import javax.persistence.RollbackException;

import org.hibernate.Transaction;

import com.riotgames.tftanalytics.bean.Match;


public class MatchDAO extends DAO {
	
	public MatchDAO() {
		super();
	}

	public void save(Match e) {
		session = factory.openSession();
		Transaction t = session.beginTransaction();
		try {
			session.save(e);
			t.commit();
		} catch (RollbackException exep) {
			t.rollback();
			System.err.println(exep.getMessage());
		}
		session.close();
	}
	
	public Match get(int id) {
		session = factory.openSession();
		Transaction t = session.beginTransaction();
		Match e = null;
		try {
			e = (Match) session.get(Match.class, id);
			t.commit();
		} catch (RollbackException exep) {
			t.rollback();
			System.err.println(exep.getMessage());
		}
		session.close();
		return e;
	}
}
