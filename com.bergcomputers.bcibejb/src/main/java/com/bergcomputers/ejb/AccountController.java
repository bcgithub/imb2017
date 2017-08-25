package com.bergcomputers.ejb;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;

import com.bergcomputers.domain.Account;
import com.bergcomputers.domain.Customer;


@Stateless
public class AccountController extends AbstractController<Account,Long> implements IAccountController{
	@PersistenceContext
	EntityManager em;
	
	public AccountController(){
		super(Account.class);
	}
	
	@Override
	protected EntityManager getEntityManager() {
		return em;
	}
	
	@Override
	public Account find(long id){
		return getEntityManager().find(Account.class, id);
	}
	
	@Override
	public List<Account> findAll() {
		return getEntityManager().createQuery("select c from Account c").getResultList();
	}
	
	@Override
	public List<Account> findRange(int startPosition, int size) {
		CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
		cq.select(cq.from(entityClass));
		Query q = getEntityManager().createQuery(cq);
		q.setMaxResults(size);
		q.setFirstResult(startPosition);
		return q.getResultList();
	}
	
	@Override
	public void delete(long accountid) {

		Account item = find(accountid);
		if (item != null) {
			getEntityManager().remove(item);
		}
	}
	
	@Override
	public Account create(Account account) {
		if (null != account && null == account.getCreationDate()) {
			account.setCreationDate(new Date());
		}
		getEntityManager().persist(account);
		getEntityManager().flush();
		return account;
	}

	@Override
	public Account update(Account account) {
		return getEntityManager().merge(account);
	}
	
	@Override
	public List<Account>  getAccountForCustomer(long customerid){
		return getEntityManager().createQuery("select c from Account c where c.customer.id="+customerid).getResultList();
	}
}
