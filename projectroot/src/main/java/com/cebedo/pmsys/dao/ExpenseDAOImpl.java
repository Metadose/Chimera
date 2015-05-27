package com.cebedo.pmsys.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import com.cebedo.pmsys.helper.DAOHelper;
import com.cebedo.pmsys.model.Expense;

@Repository
public class ExpenseDAOImpl implements ExpenseDAO {

    private DAOHelper daoHelper = new DAOHelper();
    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
	this.sessionFactory = sessionFactory;
    }

    @Override
    public void create(Expense expense) {
	Session session = this.sessionFactory.getCurrentSession();
	session.persist(expense);
    }

    @Override
    public Expense getByID(long id) {
	Session session = this.sessionFactory.getCurrentSession();
	Expense expense = (Expense) this.daoHelper.criteriaGetObjByID(session,
		Expense.class, Expense.PROPERTY_ID, id).uniqueResult();
	return expense;
    }

    @Override
    public void update(Expense expense) {
	Session session = this.sessionFactory.getCurrentSession();
	session.update(expense);
    }

    @Override
    public void delete(long id) {
	Session session = this.sessionFactory.getCurrentSession();
	Expense expense = getByID(id);
	if (expense != null) {
	    session.delete(expense);
	}
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Expense> list() {
	Session session = this.sessionFactory.getCurrentSession();
	List<Expense> list = session.createQuery(
		"FROM " + Expense.class.getName()).list();
	return list;
    }

}
