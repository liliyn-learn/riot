package com.riotgames.tftanalytics.dao;

import javax.persistence.RollbackException;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import com.riotgames.tftanalytics.bean.Joueur;


public class JoueurDAO {
private static SessionFactory factory = new MetadataSources(new StandardServiceRegistryBuilder().configure("hibernate.cfg.xml").build()).getMetadataBuilder().build().getSessionFactoryBuilder().build();
	
	public void save(Joueur e) {
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
	
	public Joueur get(int id) {
		Session session = factory.openSession();
		Transaction t = session.beginTransaction();
		Joueur e = null;
		try {
			e = (Joueur) session.get("Etudient", id);
			t.commit();
		} catch (RollbackException exep) {
			t.rollback();
			System.err.println(exep.getMessage());
		}
		return e;
	}
}
