package com.riotgames.tftanalytics.dao;

import javax.persistence.RollbackException;


import org.hibernate.Transaction;


import com.riotgames.tftanalytics.bean.Joueur;


public class JoueurDAO extends DAO {
	public void save(Joueur e) {
		Transaction t = session.beginTransaction();
		try {
			session.save(e);
			t.commit();
		} catch (RollbackException exep) {
			t.rollback();
			System.err.println(exep.getMessage());
		}
	}
	
	public Joueur get(int id) {
		Transaction t = session.beginTransaction();
		Joueur e = null;
		try {
			e = (Joueur) session.get(Joueur.class, id);
			t.commit();
		} catch (RollbackException exep) {
			t.rollback();
			System.err.println(exep.getMessage());
		}
		return e;
	}
}
