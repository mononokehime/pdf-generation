package org.project.company.greenhomes.dao.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.project.company.greenhomes.domain.entity.PropertyEPC;
import org.project.company.greenhomes.domain.entity.UploadSummary;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Class for mapping sql queries to objects. In this case {@link UploadSummary}
 *
 *
 */
public class PropertyAddressMapper implements RowMapper {

	public Object mapRow (ResultSet rs, int arg1) throws SQLException {
		PropertyEPC address = new PropertyEPC();
		address.setAddressKey(rs.getString("address_key"));
		address.setWorkFlowStatus(rs.getString("work_flow_status"));
		address.setAddressLine1(rs.getString("ADDRESS_LINE_1"));
		address.setAddressLine2(rs.getString("ADDRESS_LINE_2"));
		address.setAddressLine3(rs.getString("ADDRESS_LINE_3"));
		address.setTown(rs.getString("TOWN"));
		address.setPostcodeIncode(rs.getString("POSTCODE_INCODE"));
		address.setPostcodeOutcode(rs.getString("POSTCODE_OUTCODE"));
		address.setPropertyAddressId(rs.getString("PROPERTY_ADDRESS_ID"));
		address.setESTAC(rs.getInt("ESTAC"));
		address.setLocalAuthority(rs.getString("LOCAL_AUTHORITY"));
		address.setCountry(rs.getString("COUNTRY"));
		address.setRownum(rs.getInt("RNUM"));
		return address;
	}

}
