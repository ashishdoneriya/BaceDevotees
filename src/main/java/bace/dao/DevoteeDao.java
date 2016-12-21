package bace.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.SimpleExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import bace.pojo.Devotee;
import static bace.utils.Constants.*;

@Transactional
@Repository("devoteeDao")
public class DevoteeDao {

	@Autowired
	private SessionFactory sessionFactory;

	private Session session;
	
	public int save(Devotee devotee) {
		session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(devotee);
		return devotee.getId();
	}
	
	public void delete(int id) {
		session = sessionFactory.getCurrentSession();
		session.delete(new Devotee(id));
	}
	
	public Devotee get(int id) {
		session = sessionFactory.getCurrentSession();
		return (Devotee) session.get(Devotee.class, id);
	}

	@SuppressWarnings("unchecked")
	public List<Devotee> list(String searchQuery, String pageNumber, String maximumResults) {
		session = sessionFactory.getCurrentSession();
		Criteria cr = session.createCriteria(Devotee.class);
		cr.addOrder(Order.asc(NAME));
		cr.setMaxResults(Integer.parseInt(maximumResults));
		cr.setFirstResult(Integer.parseInt(pageNumber) - 1);
		if (searchQuery != null && !searchQuery.isEmpty()) {
			SimpleExpression se1, se2, se3, se4, se5, se6, se7;
			se1 = Restrictions.like(NAME, PERCENT + searchQuery + PERCENT);
			se2 = Restrictions.like(PERMANENT_ADDRESS, PERCENT + searchQuery + PERCENT);
			se3 = Restrictions.like(CURRENT_ADDRESS, PERCENT + searchQuery + PERCENT);
			se4 = Restrictions.like(MOBILE_NUMBER, PERCENT + searchQuery + PERCENT);
			se5 = Restrictions.like(EMAIL, PERCENT + searchQuery + PERCENT);
			se6 = Restrictions.like(EMERGENCY_NUMBER, PERCENT + searchQuery + PERCENT);
			se7 = Restrictions.like(FATHER_NAME, PERCENT + searchQuery + PERCENT);
			cr.add(Restrictions.or(se1, se2, se3, se4, se5, se6, se7));
		}
		return cr.list();
	}
	
	public int getNumberOfPages(String maximumResults) {
		return (Integer) session.createCriteria(Devotee.class).setProjection(Projections.rowCount()).uniqueResult();

	}
}
