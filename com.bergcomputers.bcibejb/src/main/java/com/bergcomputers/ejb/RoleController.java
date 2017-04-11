package com.bergcomputers.ejb;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;

import com.bergcomputers.domain.Currency;
import com.bergcomputers.domain.Role;

@Stateless
public class RoleController extends AbstractController<Role, Long> implements IRoleController {

	@PersistenceContext
	EntityManager em;

	public RoleController() {
		super(Role.class);
	}

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	public Role find(long id) {
		return getEntityManager().find(Role.class, id);
	}

	@Override
	public List<Role> findAll() {
		return getEntityManager().createQuery("select c from Role c").getResultList();
	}

	@Override
	public List<Role> findRange(int startPosition, int size) {
		CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
		cq.select(cq.from(entityClass));
		Query q = getEntityManager().createQuery(cq);
		q.setMaxResults(size);
		q.setFirstResult(startPosition);
		return q.getResultList();
	}
	@Override
	public void delete(long roleid) {

		Role item = find(roleid);
		if (item != null) {
			getEntityManager().remove(item);
		}
	}
	@Override
	public Role create(Role rol) {
		if (null != rol && null == rol.getCreationDate()) {
			rol.setCreationDate(new Date());
		}
		getEntityManager().persist(rol);
		getEntityManager().flush();
		return rol;
	}

	@Override
	public Role update(Role rol) {
		return getEntityManager().merge(rol);
	}

}
