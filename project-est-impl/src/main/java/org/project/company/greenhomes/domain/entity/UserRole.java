package org.project.company.greenhomes.domain.entity;

import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;

public class UserRole implements Cloneable, Serializable, GrantedAuthority {

	private static final long serialVersionUID = 1L;
	//private Long userId;
	private User user;
	private Long userRoleId;
	private String roleName;

	//	public Long getUserId() {
	//		return userId;
	//	}
	//	public void setUserId(Long userId) {
	//		this.userId = userId;
	//	}
	public Long getUserRoleId () {
		return userRoleId;
	}

	public void setUserRoleId (Long userRoleId) {
		this.userRoleId = userRoleId;
	}

	public String getRoleName () {
		return roleName;
	}

	public void setRoleName (String roleName) {
		this.roleName = roleName;
	}

	public String getAuthority () {
		return getRoleName();
	}

	public int compareTo (Object o) {
		if (null != o && getClass() == o.getClass()) {
			final UserRole uRole = (UserRole)o;
			if (null != roleName && null != uRole.getRoleName()) {
				if (roleName.equalsIgnoreCase(uRole.getRoleName())) {
					return 0;
				}
			}
		}
		return -1;
	}

	@Override
	public int hashCode () {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((roleName == null) ? 0 : roleName.hashCode());
		//result = prime * result + ((userId == null) ? 0 : userId.hashCode());
		return result;
	}

	@Override
	public boolean equals (Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final UserRole other = (UserRole)obj;
		if (roleName == null) {
			if (other.roleName != null) {
				return false;
			}
		} else if (!roleName.equals(other.roleName)) {
			return false;
		}
		//		if (userId == null) {
		//			if (other.userId != null)
		//				return false;
		//		} else if (!userId.equals(other.userId))
		//			return false;
		return true;
	}

	public User getUser () {
		return user;
	}

	public void setUser (User user) {
		this.user = user;
	}

}
