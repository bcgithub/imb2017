package com.bergcomputers.ejb;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;

import com.bergcomputers.domain.Beneficiary;
import com.bergcomputers.domain.Currency;

public class BeneficiaryController  extends AbstractController<Beneficiary, Long> implements IBeneficiaryController {

	
	public Beneficiary find(long id){
		return getEntityManager().find(Beneficiary.class, id);
	}
	
	public List<Beneficiary> findAll(){
		return getEntityManager().createQuery("select c from Beneficiary c").getResultList();
	}
	
	public List<Beneficiary> findRange(int startPosition, int size){
		CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
		cq.select(cq.from(entityClass));
		Query q = getEntityManager().createQuery(cq);
		q.setMaxResults(size);
		q.setFirstResult(startPosition);
		return q.getResultList();
	}
	
	public BeneficiaryController(Class<Beneficiary> entityClass) {
		super(entityClass);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected EntityManager getEntityManager() {
		// TODO Auto-generated method stub
		return null;
	}

}
