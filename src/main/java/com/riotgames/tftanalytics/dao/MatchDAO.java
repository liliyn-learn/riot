package com.riotgames.tftanalytics.dao;

import javax.persistence.RollbackException;

import org.hibernate.Transaction;

import com.riotgames.tftanalytics.bean.Match;


public class MatchDAO extends DAO {
	public void save(Match e) {
		Transaction t = session.beginTransaction();
		try {
			session.save(e);
			t.commit();
		} catch (RollbackException exep) {
			t.rollback();
			System.err.println(exep.getMessage());
		}
	}
	
	public Match get(int id) {
		Transaction t = session.beginTransaction();
		Match e = null;
		try {
			e = (Match) session.get(Match.class, id);
			t.commit();
		} catch (RollbackException exep) {
			t.rollback();
			System.err.println(exep.getMessage());
		}
		return e;
	}
}
