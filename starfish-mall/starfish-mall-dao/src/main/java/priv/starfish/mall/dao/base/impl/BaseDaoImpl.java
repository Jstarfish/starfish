package priv.starfish.mall.dao.base.impl;


import com.github.miemiedev.mybatis.paginator.domain.Order;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.github.miemiedev.mybatis.paginator.domain.Paginator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.session.SqlSession;
import priv.starfish.common.annotation.Column;
import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.common.annotation.Id;
import priv.starfish.common.annotation.Table;
import priv.starfish.common.model.Couple;
import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.common.model.Pagination;
import priv.starfish.common.util.StrUtil;
import priv.starfish.common.util.TypeUtil;
import priv.starfish.mall.comn.dto.IntSeqInfo;
import priv.starfish.mall.comn.dto.LongSeqInfo;
import priv.starfish.mall.dao.base.BaseDao;

import javax.annotation.Resource;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * DAO基类，提供常用方法
 *
 * @author koqiui
 *
 * @param <T>
 *            实体类
 * @param <K>
 *            实体主键类型
 */
public abstract class BaseDaoImpl<T, K extends Serializable> implements BaseDao<T, K> {
	protected final Log logger = LogFactory.getLog(this.getClass());

	protected Class<?> sqlTargetInterfaceType = TypeUtil.getInterfaceTypeWithAnnotation(this.getClass(), IBatisSqlTarget.class);

	protected String defaultSqlNameSpace = sqlTargetInterfaceType.getName();
	protected String defaultDataSubject = TypeUtil.getSuperClassGenericType(this.getClass(), 0).getSimpleName();

	protected String getDefaultSqlNameSpace() {
		return this.defaultSqlNameSpace;
	}

	protected String getDefaultDataSubject() {
		return this.defaultDataSubject;
	}

	protected String getNamedSqlId(Class<?> sqlNameSpaceClass, String sqlId) {
		return sqlNameSpaceClass.getName() + "." + sqlId;
	}

	protected String getNamedSqlId(String sqlNameSpace, String sqlId) {
		return sqlNameSpace + "." + sqlId;
	}

	protected String getNamedSqlId(String sqlId) {
		return this.getDefaultSqlNameSpace() + "." + sqlId;
	}

	@Resource
	protected SqlSession sqlSession;

	protected SqlSession getSqlSession() {
		// return SqlSessionUtils.getSqlSession(sqlSessionFactory);
		return this.sqlSession;
	}

	// 创建Map<String, Object>的便捷方法
	protected Map<String, Object> newParamMap() {
		return new HashMap<String, Object>();
	}

	// 创建List<T>的便捷方法
	protected List<T> newParamList(Class<T> elemType) {
		return new ArrayList<T>();
	}

	// Dao , Service 层输入的数据和返回的数据必须是通用格式
	// 通用分页信息 => Mybatis所需的分页信息
	protected PageBounds toPageBounds(Pagination pagination, Map<String, String> sortItems) {
		if (sortItems == null || sortItems.isEmpty()) {
			return new PageBounds(pagination.getPageNumber(), pagination.getPageSize());
		} else {
			List<Order> orderItems = new ArrayList<Order>();
			for (Map.Entry<String, String> sortItem : sortItems.entrySet()) {
				String colName = sortItem.getKey();
				String order = sortItem.getValue();
				Order orderItem = Order.create(colName, order);
				orderItems.add(orderItem);
			}
			return new PageBounds(pagination.getPageNumber(), pagination.getPageSize(), orderItems);
		}

	}

	protected PageBounds toPageBounds(Pagination pagination, List<Order> orderItems) {
		return new PageBounds(pagination.getPageNumber(), pagination.getPageSize(), orderItems);
	}

	protected PageBounds toPageBounds(Pagination pagination) {
		return new PageBounds(pagination.getPageNumber(), pagination.getPageSize());
	}

	// Mybatis分页信息 => 通用分页信息
	protected <E> PaginatedList<E> toPaginatedList(PageList<E> PageList) {
		PaginatedList<E> paginatedList = PaginatedList.newOne();
		Pagination pagination = paginatedList.getPagination();
		//
		Paginator paginator = PageList.getPaginator();
		pagination.setTotalCount(paginator.getTotalCount());
		pagination.setPageSize(paginator.getPage());
		pagination.setPageNumber(paginator.getTotalPages());

		paginatedList.setRows(PageList);
		//
		return paginatedList;
	}

	@SuppressWarnings("unchecked")
	/**
	 * 检查并纠正页码下溢问题（请求的页面比实际的页码大，而结果为空的情况）
	 *
	 * @author koqiui
	 * @date 2015年11月19日 下午6:48:10
	 *
	 * @param paginatedFilter
	 * @param sqlId
	 * @param PageList
	 * @return
	 */
	protected <E> com.github.miemiedev.mybatis.paginator.domain.PageList<E> checkAndRefetchList(PaginatedFilter paginatedFilter, String sqlId, PageList<E> PageList) {
		Pagination srcPagination = paginatedFilter.getPagination();
		Paginator retPagination = PageList.getPaginator();
		int retPageNumber = retPagination.getTotalPages();
		if (retPageNumber < srcPagination.getPageNumber()) {
			srcPagination.setPageNumber(retPageNumber);
			PageBounds pageBounds = toPageBounds(srcPagination, paginatedFilter.getSortItems());
			return (PageList<E>) this.getSqlSession().selectList(sqlId, paginatedFilter.getFilterItems(), pageBounds);
		} else {
			return PageList;
		}
	}

	// 根据实体类获取其对应的表名
	protected String getTableName(Class<?> entityClass) {
		Table tableAnno = entityClass.getAnnotation(Table.class);
		return tableAnno == null ? null : tableAnno.name();
	}

	// 根据实体类 获取其对应的id列名
	protected String getIdColName(Class<?> entityClass) {
		try {
			Field[] fields = entityClass.getDeclaredFields();
			for (int i = 0; i < fields.length; i++) {
				Field field = fields[i];
				field.setAccessible(true);
				if (field.isAnnotationPresent(Id.class)) {
					Id idAnno = field.getAnnotation(Id.class);
					String idName = idAnno.name();
					if (!StrUtil.hasText(idName)) {
						idName = field.getName();
					}
					return idName;
				}
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return null;
	}

	// 根据实体类 和字段名 获取其对应的列名
	protected String getColumnName(Class<?> entityClass, String fieldName) {
		try {
			Field field = entityClass.getDeclaredField(fieldName);
			field.setAccessible(true);
			Column colAnno = field.getAnnotation(Column.class);
			if (colAnno != null) {
				String colName = colAnno.name();
				if (!StrUtil.hasText(colName)) {
					colName = field.getName();
					return colName;
				}
			}
			// Id idAnno = field.getAnnotation(Id.class);
			// if (idAnno != null) {
			// String idName = idAnno.name();
			// if (!StrUtil.hasText(idName)) {
			// idName = field.getName();
			// }
			// return idName;
			// }
		} catch (Exception e) {
			logger.error(e);
		}
		return null;
	}

	// 获取给定实体类对应的表数据行数
	public long getEntityCount(Class<?> entityClass) {
		String tableName = getTableName(entityClass);
		if (tableName == null) {
			return -1L;
		}
		//
		String sqlId = this.getNamedSqlId(BaseDao.class, "getEntityCount");
		//
		Map<String, Object> params = this.newParamMap();
		params.put("tableName", tableName);
		return this.getSqlSession().selectOne(sqlId, params);
	}

	public int getEntityMaxSeqNo(Class<?> entityClass) {
		return getEntityMaxSeqNo(entityClass, null, null);
	}

	public int getEntityMaxSeqNo(Class<?> entityClass, String refFieldName, Object refFieldValue) {
		String tableName = getTableName(entityClass);
		if (tableName == null) {
			return -1;
		}
		//
		String sqlId = this.getNamedSqlId(BaseDao.class, "getEntityMaxSeqNo");
		//
		Map<String, Object> params = this.newParamMap();
		params.put("tableName", tableName);
		if (StrUtil.hasText(refFieldName)) {
			String refColName = this.getColumnName(entityClass, refFieldName);
			if (!StrUtil.hasText(refColName)) {
				return -1;
			}
			params.put("colName", refColName);
			params.put("colValue", refFieldValue);
		}
		return this.getSqlSession().selectOne(sqlId, params);
	}

	private K getBrotherIdBySeqNo(Class<T> entityClass, K id, int seqNo, String refFieldName, Object refFieldValue, boolean lesser) {
		String tableName = getTableName(entityClass);
		if (tableName == null) {
			return null;
		}
		String idName = this.getIdColName(entityClass);
		if (idName == null) {
			return null;
		}
		//
		String sqlId = this.getNamedSqlId(BaseDao.class, "getBrotherIdBySeqNo");
		//
		Map<String, Object> params = this.newParamMap();
		params.put("tableName", tableName);
		params.put("idName", idName);
		params.put("idValue", id);
		params.put("seqNo", seqNo);
		params.put("lesser", lesser);
		if (StrUtil.hasText(refFieldName)) {
			String refColName = this.getColumnName(entityClass, refFieldName);
			if (!StrUtil.hasText(refColName)) {
				return null;
			}
			params.put("colName", refColName);
			params.put("colValue", refFieldValue);
		}
		return this.getSqlSession().selectOne(sqlId, params);
	}

	public Couple<T, T> getSeqNoBrothersById(Class<T> entityClass, K id, boolean lesser) {
		return getSeqNoBrothersById(entityClass, id, null, lesser);
	}

	public Couple<T, T> getSeqNoBrothersById(Class<T> entityClass, K id, String refFieldName, boolean lesser) {
		T entity = this.selectById(id);
		if (entity == null) {
			return null;
		} else {
			Integer seqNo = (Integer) TypeUtil.getObjectFieldValue(entity, "seqNo");
			if (seqNo == null) {
				return null;
			}
			Object refFieldValue = TypeUtil.getObjectFieldValue(entity, refFieldName);
			K id2 = this.getBrotherIdBySeqNo(entityClass, id, seqNo, refFieldName, refFieldValue, lesser);
			T entity2 = id2 == null ? null : this.selectById(id2);
			return Couple.newOne(entity, entity2);
		}
	}

	public List<Integer> getEntityIds(Class<?> entityClass, String refFieldName, Object refFieldValue) {
		String tableName = getTableName(entityClass);
		if (tableName == null) {
			return null;
		}
		//
		String refColName = this.getColumnName(entityClass, refFieldName);
		if (!StrUtil.hasText(refColName)) {
			return null;
		}
		//
		String sqlId = this.getNamedSqlId(BaseDao.class, "getEntityIds");
		//
		Map<String, Object> params = this.newParamMap();
		params.put("tableName", tableName);
		params.put("colName", refColName);
		params.put("colValue", refFieldValue);
		return this.getSqlSession().selectList(sqlId, params);
	}

	public Integer newSeqInt(Class<?> entityClass) {
		String tableName = getTableName(entityClass);
		if (tableName == null) {
			return null;
		}
		//
		String sqlId = this.getNamedSqlId(BaseDao.class, "newIntSequence");
		//
		IntSeqInfo intSeq = new IntSeqInfo();
		intSeq.setTableName(tableName);
		int count = this.getSqlSession().insert(sqlId, intSeq);
		//
		return count > 0 ? intSeq.getId() : null;
	}

	public Long newSeqLong(Class<?> entityClass) {
		String tableName = getTableName(entityClass);
		if (tableName == null) {
			return null;
		}
		//
		String sqlId = this.getNamedSqlId(BaseDao.class, "newIntSequence");
		//
		LongSeqInfo longSeq = new LongSeqInfo();
		longSeq.setTableName(tableName);
		int count = this.getSqlSession().insert(sqlId, longSeq);
		//
		return count > 0 ? longSeq.getId() : null;
	}

}
