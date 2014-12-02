package org.project.company.greenhomes.domain.entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.project.company.greenhomes.common.enums.RoleNames;
import org.project.company.greenhomes.domain.model.PasswordModel;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class User implements Cloneable, Serializable, UserDetails {
	private static final long serialVersionUID = 1L;

	private Set<UserRole> userRoleSet;
	private Long userId;
	private Integer countryId;

	private String title;
	private String firstName;
	private String familyName;
	private String addressLine1;
	private String addressLine2;
	private String addressLine3;
	private String postTown;
	private String county;
	private String postCode;
	private String userName;
	private String password;
	private String emailAddress;
	private Date createDate;

	private PasswordModel passwordModel = new PasswordModel();
	private Boolean active;

	private String[] userRoleId;

	private String userRoleString;

	public String getUserRoleString () {
		if (null == userRoleSet || userRoleSet.size() < 1) {
			return null;
		}
		userRoleString = "";
		//populate the values
		for (UserRole role : userRoleSet) {
			if (role.getRoleName().equalsIgnoreCase(RoleNames.ROLE_SUPER_USER.getValue())) {
				userRoleString += "Admin";
			}
			if (role.getRoleName().equalsIgnoreCase(RoleNames.ROLE_USER.getValue())) {
				if (userRoleString.length() > 0) {
					userRoleString += ", ";
				}
				userRoleString += "User";
			}
		}
		return userRoleString;
	}

	public void setUserRoleString (String userRoleString) {
		this.userRoleString = userRoleString;
	}

	public String[] getUserRoleId () {

		//		if (null == userRoleSet || userRoleSet.size() < 1)
		//		{
		//			return null;
		//		}
		//		userRoleId = new String[userRoleSet.size()];
		//		//populate the values
		//		int i = 0;
		//		for (UserRole role: userRoleSet)
		//		{
		//			userRoleId[i] = role.getRoleName()+" ";
		//		}
		return this.userRoleId;
	}

	public void setUserRoleId (String[] userRoleId) {
		//		if (null != userRoleId)
		//		{
		//			this.userRoleId = new String[userRoleId.length];
		//			for (int i = 0; i< userRoleId.length;i++)
		//			{
		//				this.userRoleId[i] = new String(userRoleId[i]);
		//			}
		//		}

		this.userRoleId = userRoleId;
	}

	/**
	 * Required by Acegi interface UserDetails. Used for Acegi authentication.
	 * Always returns 'true'
	 */
	public boolean isAccountNonLocked () {
		return active;
	}

	/**
	 * Required by Acegi interface UserDetails. Used for Acegi authentication.
	 * Always returns 'true'
	 */
	public boolean isCredentialsNonExpired () {
		return active;
	}

	/**
	 * Required by Acegi interface UserDetails. Used for Acegi authentication.
	 * Always returns 'true'
	 */
	public boolean isAccountNonExpired () {
		return active;
	}

	/**
	 * Required by Acegi interface UserDetails. Used for Acegi authentication. 
	 * Returns an array of granted roles for the user
	 */
	//    public GrantedAuthority[] getAuthorities() {
	//    	//note: this implementation could result in returning an array containing duplicate
	//    	//role entries, if the customer has more than one active account (is this true?)
	//    	//Not sure if Acegi will care about this
	//    	//List<Product> roles = new Vector<Product>();
	////    	for (UserRole account : userRoleSet){
	////    		if (account.getAccountStatus().equals(AccountStatus.STATUS_ACTIVE.getValue())){
	////    			roles.add(account.getProduct());
	////    		}
	////    	}
	//        GrantedAuthority[] arrayAuths = {};
	//        arrayAuths = (GrantedAuthority[]) userRoleSet.toArray(arrayAuths);
	//        return arrayAuths;
	//    }

	/**
	 * Required by Acegi interface UserDetails. Used for Acegi authentication.
	 * If the customer has at least one active account, the customer is allowed to login
	 */
	public boolean isEnabled () {
		return active;
	}

	/**
	 * Required by Acegi interface UserDetails. Used for Acegi authentication.
	 *
	 * @return loginId
	 */
	public String getUsername () {
		return userName;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities () {
		return null;
	}

	/**
	 * Required by Acegi interface UserDetails. Used for Acegi authentication.
	 *
	 * @return loginPassword
	 */
	public String getPassword () {
		return password;
	}
	//End Acegi requirements

	public void setPassword (String password) {
		this.password = password;
	}

	public String getAddressLine1 () {
		return addressLine1;
	}

	public void setAddressLine1 (String addressLine1) {
		this.addressLine1 = addressLine1;
	}

	public String getAddressLine2 () {
		return addressLine2;
	}

	public void setAddressLine2 (String addressLine2) {
		this.addressLine2 = addressLine2;
	}

	public String getAddressLine3 () {
		return addressLine3;
	}

	public void setAddressLine3 (String addressLine3) {
		this.addressLine3 = addressLine3;
	}

	/**
	 * @return the countryId
	 */
	public Integer getCountryId () {
		return countryId;
	}

	/**
	 * @param countryId the countryId to set
	 */
	public void setCountryId (Integer countryId) {
		this.countryId = countryId;
	}

	/**
	 * @return the county
	 */
	public String getCounty () {
		return county;
	}

	/**
	 * @param county the county to set
	 */
	public void setCounty (String county) {
		this.county = county;
	}

	/**
	 * @return the emailAddress
	 */
	public String getEmailAddress () {
		return emailAddress;
	}

	/**
	 * @param emailAddress the emailAddress to set
	 */
	public void setEmailAddress (String emailAddress) {
		this.emailAddress = emailAddress;
	}

	/**
	 * @return the postCode
	 */
	public String getPostCode () {
		return postCode;
	}

	/**
	 * @param postCode the postCode to set
	 */
	public void setPostCode (String postCode) {
		this.postCode = postCode;
	}

	/**
	 * @return the postTown
	 */
	public String getPostTown () {
		return postTown;
	}

	/**
	 * @param postTown the postTown to set
	 */
	public void setPostTown (String postTown) {
		this.postTown = postTown;
	}

	/**
	 * @return the title
	 */
	public String getTitle () {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle (String title) {
		this.title = title;
	}

	public Long getUserId () {
		return userId;
	}

	public void setUserId (Long userId) {
		this.userId = userId;
	}

	public String getFirstName () {
		return firstName;
	}

	public void setFirstName (String firstName) {
		this.firstName = firstName;
	}

	public String getFamilyName () {
		return familyName;
	}

	public void setFamilyName (String familyName) {
		this.familyName = familyName;
	}

	public String getUserName () {
		return userName;
	}

	public void setUserName (String userName) {
		this.userName = userName;
	}

	public Boolean getActive () {
		return active;
	}

	public void setActive (Boolean active) {
		this.active = active;
	}

	/**
	 * @return the roleSet
	 */
	public Set<UserRole> getUserRoleSet () {
		return userRoleSet;
	}

	/**
	 * @param userRoleSet the userRoleSet to set
	 */
	public void setUserRoleSet (Set<UserRole> userRoleSet) {
		this.userRoleSet = userRoleSet;
	}

	public void addUserRole (UserRole userRole) {
		if (null == userRoleSet) {

			userRoleSet = new HashSet<UserRole>();
		}

		//userRole.setUserId(userId);
		userRole.setUser(this);
		userRoleSet.add(userRole);
	}

	public Date getCreateDate () {
		return createDate;
	}

	public void setCreateDate (Date createDate) {
		this.createDate = createDate;
	}

	public PasswordModel getPasswordModel () {
		return passwordModel;
	}

	public void setPasswordModel (PasswordModel passwordModel) {
		this.passwordModel = passwordModel;
	}

}
