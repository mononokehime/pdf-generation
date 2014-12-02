package org.project.company.greenhomes.dao.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.project.company.greenhomes.domain.model.LocationData;

import java.sql.ResultSet;
import java.sql.SQLException;

public class LocationDataMapper implements RowMapper {

	public Object mapRow (ResultSet rs, int arg1) throws SQLException {
		LocationData data = new LocationData();
		data.setCentreCode(rs.getInt("centre_code"));
		data.setEstSegmentDescription(rs.getString("ESTSEGMENTDESCRIPTION"));
		data.setLocalAuthority(rs.getString("oslaua"));
		data.setRegion(rs.getString("region"));
		return data;
	}

}
