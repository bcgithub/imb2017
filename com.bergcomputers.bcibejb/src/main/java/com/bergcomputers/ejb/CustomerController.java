package com.bergcomputers.ejb;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;

import com.bergcomputers.domain.Currency;
import com.bergcomputers.domain.Customer;

@Stateless
public class CustomerController extends AbstractController<Customer, Long>  implements ICustomerController {
	
	
	

	public CustomerController(Class<Customer> entityClass) {
		super(entityClass);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Customer find(long id) {
		return getEntityManager().find(Customer.class, id);
	}
	
	@Override
	public List<Customer> findRange(int startPosition, int size) 
	{
		CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
		cq.select(cq.from(entityClass));
		Query q = getEntityManager().createQuery(cq);
		q.setMaxResults(size);
		q.setFirstResult(startPosition);
		return q.getResultList();
	}
	
	@Override
	public void delete(long customerid) 
	{
		Customer item = find(customerid);
		if (item != null) {
			getEntityManager().remove(item);
		}
	}
	
	@Override
	public Customer create(Customer customer) {
		if (null != customer && null == customer.getCreationDate()) {
			customer.setCreationDate(new Date());
		}
		getEntityManager().persist(customer);
		getEntityManager().flush();
		return customer;
	}
	
	public Customer update(Customer customer) {
		return getEntityManager().merge(customer);
	}


	@Override
	protected EntityManager getEntityManager() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public List<Customer> findAll() {
		return getEntityManager().createQuery("select c from Customer c").getResultList();
	}
	
	

}
