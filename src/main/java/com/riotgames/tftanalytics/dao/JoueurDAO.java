package com.riotgames.tftanalytics.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;


import com.riotgames.tftanalytics.bean.Joueur;
public class JoueurDAO extends DAO {
    
    public JoueurDAO() {
        super();
    }

    public void save(Joueur e) {
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
    
    public Joueur get(int id) {
        Joueur e = null;
        try (Session session = factory.openSession()) {
            Transaction t = session.beginTransaction();
            try {
                e = (Joueur) session.get(Joueur.class, id);
                t.commit();
            } catch (Exception ex) {
                t.rollback();
                ex.printStackTrace();
            }
        }
        return e;
    }
}