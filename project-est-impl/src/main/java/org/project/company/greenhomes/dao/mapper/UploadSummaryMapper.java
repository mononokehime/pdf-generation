package org.project.company.greenhomes.dao.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.project.company.greenhomes.domain.entity.UploadSummary;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

/**
 * Class for mapping sql queries to objects. In this case {@link UploadSummary}
 *
 *
 */
public class UploadSummaryMapper implements RowMapper {

	public Object mapRow (ResultSet rs, int arg1) throws SQLException {
		UploadSummary summary = new UploadSummary();
		summary.setEndTime(rs.getDate("END_TIME"));
		Calendar cal = Calendar.getInstance();
		summary.setStartTime(rs.getDate("START_TIME", cal));
		summary.setUploadSummaryId(rs.getLong("UPLOAD_SUMMARY_ID"));
		summary.setUploadType(rs.getString("UPLOAD_TYPE"));
		summary.setErrorCount(rs.getInt("ERROR_COUNT"));
		return summary;
	}

}
