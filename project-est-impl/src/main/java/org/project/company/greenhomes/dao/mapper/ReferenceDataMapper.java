package org.project.company.greenhomes.dao.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.project.company.greenhomes.domain.model.ReferenceData;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Class for mapping sql queries to objects. In this case {@link ReferenceData}
 *
 *
 */
public class ReferenceDataMapper implements RowMapper {

	public Object mapRow (ResultSet rs, int arg1) throws SQLException {
		ReferenceData data = new ReferenceData();
		data.setReferenceDataKey(rs.getString(2));
		data.setShortName(rs.getString(1));
		data.setLongName(rs.getString(1));
		return data;
	}

}
