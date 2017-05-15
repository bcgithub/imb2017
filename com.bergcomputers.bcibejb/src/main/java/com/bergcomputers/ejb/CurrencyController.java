package com.bergcomputers.ejb;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;

import com.bergcomputers.domain.Currency;

@Stateless
public class CurrencyController extends AbstractController<Currency, Long> implements ICurrencyController {

	@PersistenceContext
	EntityManager em;

	public CurrencyController() {
		super(Currency.class);
	}

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	public Currency find(long id) {
		return getEntityManager().find(Currency.class, id);
	}

	@Override
	public List<Currency> findAll() {
		return getEntityManager().createQuery("select c from Currency c").getResultList();
	}

	@Override
	public List<Currency> findRange(int startPosition, int size) {
		CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
		cq.select(cq.from(entityClass));
		Query q = getEntityManager().createQuery(cq);
		q.setMaxResults(size);
		q.setFirstResult(startPosition);
		return q.getResultList();
	}

	@Override
	public void delete(long currencyid) {

		Currency item = find(currencyid);
		if (item != null) {
			getEntityManager().remove(item);
		}
	}

	@Override
	public Currency create(Currency currency) {
		if (null != currency && null == currency.getCreationDate()) {
			currency.setCreationDate(new Date());
		}
		getEntityManager().persist(currency);
		getEntityManager().flush();
		return currency;
	}
	
	

	@Override
	public Currency update(Currency currency) {
		return getEntityManager().merge(currency);
	}

}
