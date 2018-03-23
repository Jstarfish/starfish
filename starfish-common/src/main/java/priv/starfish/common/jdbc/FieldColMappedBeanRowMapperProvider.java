package priv.starfish.common.jdbc;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.JdbcUtils;
import priv.starfish.common.util.ColumnUtil;


/**
 * 为标记为@ColumnMarked的数据模型类自动生成（并缓存）记录行到数据模型的转换器
 * 
 * @author Hu Changwei
 * 
 */
public class FieldColMappedBeanRowMapperProvider {

	public static class FieldColMappedBeanRowMapper implements RowMapper<FieldColMappedBean> {
		private final FieldColMappedBean refMappedBean;

		public FieldColMappedBeanRowMapper(final FieldColMappedBean refMappedBean) {
			this.refMappedBean = refMappedBean;
		}

		@SuppressWarnings("rawtypes")
		@Override
		public FieldColMappedBean mapRow(ResultSet rs, int rowNum) throws SQLException {
			try {
				FieldColMappedBean object = FieldColMappedBean.cloneByFields(refMappedBean);
				Map<String, ColumnInfo> colInfoMap = ColumnUtil.getBeanColumnInfoMap(object);
				ResultSetMetaData metadata = rs.getMetaData();
				int colCount = metadata.getColumnCount();
				for (int i = 1; i <= colCount; i++) {
					String colName = metadata.getColumnName(i).toUpperCase();
					ColumnInfo colInfo = colInfoMap.get(colName);
					if (colInfo == null) {
						continue;
					}
					Class<?> fieldType = colInfo.getFieldType();
					Object colValue = JdbcUtils.getResultSetValue(rs, i, fieldType);
					String fieldName = colInfo.getFieldName();
					object.setFieldValue(fieldName, colValue);
				}
				return object;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
	}

	public static RowMapper<FieldColMappedBean> getRowMapper(final FieldColMappedBean refMappedBean) {
		return new FieldColMappedBeanRowMapper(refMappedBean);
	}
}
