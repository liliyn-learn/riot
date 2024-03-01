package com.riotgames.tftanalytics.dao;

import javax.persistence.RollbackException;

import org.hibernate.Transaction;

import com.riotgames.tftanalytics.bean.MatchAnalyzer;


public class MatchAnalyzerDAO extends DAO{
	public void save(MatchAnalyzer e) {
		Transaction t = session.beginTransaction();
		try {
			session.save(e);
			t.commit();
		} catch (RollbackException exep) {
			t.rollback();
			System.err.println(exep.getMessage());
		}
	}
	
	public MatchAnalyzer get(int id) {
		Transaction t = session.beginTransaction();
		MatchAnalyzer e = null;
		try {
			e = (MatchAnalyzer) session.get(MatchAnalyzer.class, id);
			t.commit();
		} catch (RollbackException exep) {
			t.rollback();
			System.err.println(exep.getMessage());
		}
		return e;
	}
}
