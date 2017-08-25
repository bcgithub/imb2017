package com.bergcomputers.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "ROLE")
@XmlRootElement
@NamedQuery(name=Role.findAllRoles,query="SELECT c from Role c")
public class Role extends BaseEntity implements Serializable, IRole {
	public final static String findAllRoles = "com.bergcomputers.role.findAll";
	
	@NotNull
	private String name;
	
	@Override
	public String toString() {
		return "Role [name=" + name + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Role other = (Role) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}


	/* (non-Javadoc)
	 * @see com.bergcomputers.domain.IRole#getName()
	 */
	@Override
	public String getName() {
		return name;
	}

	/* (non-Javadoc)
	 * @see com.bergcomputers.domain.IRole#setName(java.lang.String)
	 */
	@Override
	public void setName(String name) {
		this.name = name;
	}
	
}
