package com.bergcomputers.ejb;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;

import com.bergcomputers.domain.Device;
@Stateless
public class DeviceControler extends AbstractController<Device, Long> implements IDeviceController {

	@PersistenceContext
	EntityManager em;

	public DeviceControler() {
		super(Device.class);
	}


	
	@Override
	protected EntityManager getEntityManager() {
		return em;
	}	
	
	@Override
	public Device find(long id) {
		return getEntityManager().find(Device.class, id);
	}
	
	@Override
	public List<Device> findAll() {
		return getEntityManager().createQuery("select c from Device c").getResultList();
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

	@Override
	public void delete(long deviceid) {
		Device item = find(deviceid);
		if (item != null) {
			getEntityManager().remove(item);
		}
	}
	
	@Override
	public Device create(Device device) {
		if (null != device && null == device.getCreationDate()) {
			device.setCreationDate(new Date());
		}
		getEntityManager().persist(device);
		getEntityManager().flush();
		return device;
	}
	
	@Override
	public Device update(Device device) {
		return getEntityManager().merge(device);
	}
	
}
