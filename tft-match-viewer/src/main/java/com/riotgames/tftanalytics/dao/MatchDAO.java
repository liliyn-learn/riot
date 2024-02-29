package com.riotgames.tftanalytics.dao;

import javax.persistence.RollbackException;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import com.riotgames.tftanalytics.bean.Joueur;
import com.riotgames.tftanalytics.bean.Match;


public class MatchDAO {
private static SessionFactory factory = new MetadataSources(new StandardServiceRegistryBuilder().configure("hibernate.cfg.xml").build()).getMetadataBuilder().build().getSessionFactoryBuilder().build();
	
	public void save(Match e) {
		Session session = factory.openSession();
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
		Session session = factory.openSession();
		Transaction t = session.beginTransaction();
		Match e = null;
		try {
			e = (Match) session.get("Etudient", id);
			t.commit();
		} catch (RollbackException exep) {
			t.rollback();
			System.err.println(exep.getMessage());
		}
		return e;
	}
}
