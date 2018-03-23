package priv.starfish.common.jdbc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import priv.starfish.common.util.ColumnUtil;
import priv.starfish.common.util.StrUtil;


public abstract class BaseJdbcDao {
	protected final Log logger = LogFactory.getLog(this.getClass());
	//
	private static final IPlaceHolderMaker namedParamPlaceHolderMaker = new IPlaceHolderMaker() {
		@Override
		public String makePlaceHolder(String orginalStr) {
			return ":" + orginalStr.trim();
		}

		@Override
		public boolean isPlaceHolder(String valueStr) {
			if (!StrUtil.hasText(valueStr)) {
				return false;
			}
			return valueStr.startsWith(":");
		}

	};

	private NamedParameterJdbcTemplate jdbcTemplate;

	protected NamedParameterJdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(NamedParameterJdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	protected SqlParameterSource getSqlParameterSource(Object paramBean) {
		return new BeanPropertySqlParameterSource(paramBean);
	}

	protected <T> SqlParameterSource getSqlParameterSource(Class<T> paramBeanClass) {
		try {
			return new BeanPropertySqlParameterSource(paramBeanClass.newInstance());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	protected Map<String, Object> getParamMap() {
		return new HashMap<String, Object>();
	}

	protected <T> RowMapper<T> getColumnMarkedTypeRowMapper(Class<T> modelType) {
		return ColumnMarkedTypeRowMapperProvider.getRowMapper(modelType);
	}

	protected RowMapper<FieldColMappedBean> getFieldColMappedBeanRowMapper(final FieldColMappedBean refMappedBean) {
		return FieldColMappedBeanRowMapperProvider.getRowMapper(refMappedBean);
	}

	protected <T> T queryForObject(String sql, Map<String, ?> paramMap, Class<T> modelType) {
		return this.getJdbcTemplate().queryForObject(sql, paramMap, this.getColumnMarkedTypeRowMapper(modelType));
	}

	protected <T> T queryForObject(String sql, Object paramBean, Class<T> modelType) {
		return this.getJdbcTemplate().queryForObject(sql, this.getSqlParameterSource(paramBean),
				this.getColumnMarkedTypeRowMapper(modelType));
	}

	protected FieldColMappedBean queryForObject(String sql, Map<String, ?> paramMap,
			final FieldColMappedBean refMappedBean) {
		return this.getJdbcTemplate().queryForObject(sql, paramMap, this.getFieldColMappedBeanRowMapper(refMappedBean));
	}

	protected FieldColMappedBean queryForObject(String sql, Object paramBean, final FieldColMappedBean refMappedBean) {
		return this.getJdbcTemplate().queryForObject(sql, this.getSqlParameterSource(paramBean),
				this.getFieldColMappedBeanRowMapper(refMappedBean));
	}

	protected <T> List<T> queryForObjectList(String sql, Map<String, ?> paramMap, Class<T> modelType) {
		List<Map<String, Object>> rowMapList = this.getJdbcTemplate().queryForList(sql, paramMap);
		return ColumnUtil.rowMapListToColumnMarkedObjectList(rowMapList, modelType);
	}

	protected <T> List<T> queryForObjectList(String sql, Object paramBean, Class<T> modelType) {
		List<Map<String, Object>> rowMapList = this.getJdbcTemplate().queryForList(sql,
				this.getSqlParameterSource(paramBean));
		return ColumnUtil.rowMapListToColumnMarkedObjectList(rowMapList, modelType);
	}

	protected List<FieldColMappedBean> queryForObjectList(String sql, Map<String, ?> paramMap,
			final FieldColMappedBean refMappedBean) {
		List<Map<String, Object>> rowMapList = this.getJdbcTemplate().queryForList(sql, paramMap);
		return ColumnUtil.rowMapListToFieldColMappedBeanList(rowMapList, refMappedBean);
	}

	protected List<FieldColMappedBean> queryForObjectList(String sql, Object paramBean,
			final FieldColMappedBean refMappedBean) {
		List<Map<String, Object>> rowMapList = this.getJdbcTemplate().queryForList(sql,
				this.getSqlParameterSource(paramBean));
		return ColumnUtil.rowMapListToFieldColMappedBeanList(rowMapList, refMappedBean);
	}

	protected SelectBuilder getSelectBuilder() {
		SelectBuilder builder = SelectBuilder.getInstance();
		builder.setPlaceHolderMaker(namedParamPlaceHolderMaker);
		return builder;
	}

	protected InsertBuilder getInsertBuilder() {
		InsertBuilder builder = InsertBuilder.getInstance();
		builder.setPlaceHolderMaker(namedParamPlaceHolderMaker);
		return builder;
	}

	protected UpdateBuilder getUpdateBuilder() {
		UpdateBuilder builder = UpdateBuilder.getInstance();
		builder.setPlaceHolderMaker(namedParamPlaceHolderMaker);
		return builder;
	}

	protected DeleteBuilder getDeleteBuilder() {
		DeleteBuilder builder = DeleteBuilder.getInstance();
		builder.setPlaceHolderMaker(namedParamPlaceHolderMaker);
		return builder;
	}

	protected String asPlaceHolder(String orginalStr) {
		return namedParamPlaceHolderMaker.makePlaceHolder(orginalStr);
	}

}
