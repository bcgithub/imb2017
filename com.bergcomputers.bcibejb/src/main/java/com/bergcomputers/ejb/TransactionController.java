package com.bergcomputers.ejb;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;

import com.bergcomputers.domain.Transaction;

@Stateless
public class TransactionController extends AbstractController<Transaction, Long> implements ITransactionController
{
	@PersistenceContext
	EntityManager em;
	
	public TransactionController() {
		super(Transaction.class);
	}
	
	@Override
	protected EntityManager getEntityManager() {
		return em;
	}
	
	@Override
	public Transaction find(long id) {
		return getEntityManager().find(Transaction.class, id);
	}
	
	@Override
	public List<Transaction> findAll() {
		return getEntityManager().createQuery("select c from Transaction c").getResultList();
	}
	
	@Override
	public List<Transaction> findRange(int startPosition, int size) {
		CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
		cq.select(cq.from(entityClass));
		Query q = getEntityManager().createQuery(cq);
		q.setMaxResults(size);
		q.setFirstResult(startPosition);
		return q.getResultList();
	}
	
	@Override
	public void delete(long transactionid)
	{
		Transaction item = find(transactionid);
		if (item != null) {
			getEntityManager().remove(item);
		}
	}
	
	@Override
	public Transaction create(Transaction transaction) {
		if (null != transaction && null == transaction.getCreationDate()) {
			transaction.setCreationDate(new Date());
		}
		getEntityManager().persist(transaction);
		getEntityManager().flush();
		return transaction;
	}
	
	@Override
	public Transaction update(Transaction transaction) {
		return getEntityManager().merge(transaction);
	}
}
