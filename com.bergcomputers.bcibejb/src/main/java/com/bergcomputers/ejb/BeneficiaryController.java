package com.bergcomputers.ejb;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;

import com.bergcomputers.domain.Beneficiary;
import com.bergcomputers.domain.Currency;
import com.bergcomputers.domain.Role;

@Stateless
public class BeneficiaryController  extends AbstractController<Beneficiary, Long> implements IBeneficiaryController {

	@PersistenceContext
	EntityManager em;
	
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
	
	@Override
	public void delete(long bnfid) {

		Beneficiary item = find(bnfid);
		if (item != null) {
			getEntityManager().remove(item);
		}
	}
	@Override
	public Beneficiary create(Beneficiary bnf) {
		if (null != bnf && null == bnf.getCreationDate()) {
			bnf.setCreationDate(new Date());
		}
		getEntityManager().persist(bnf);
		getEntityManager().flush();
		return bnf;
	}

	@Override
	public Beneficiary update(Beneficiary bnf) {
		return getEntityManager().merge(bnf);
	}

	
	public BeneficiaryController() {
		super(Beneficiary.class);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

}
