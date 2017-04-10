package com.bergcomputers.ejb;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;

import com.bergcomputers.domain.Device;

public class DeviceControler extends AbstractController<Device, Long> implements IDeviceController {


	public DeviceControler() {
		super(Device.class);

	}

	@PersistenceContext
	EntityManager em;
	
	@Override
	protected EntityManager getEntityManager() {
		return em;
	}	

	@Override
	public List<Device> findRange(int startPosition, int size) {
		CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
		cq.select(cq.from(entityClass));
		Query q = getEntityManager().createQuery(cq);
		q.setMaxResults(size);
		q.setFirstResult(startPosition);
		return q.getResultList();
	}

}
