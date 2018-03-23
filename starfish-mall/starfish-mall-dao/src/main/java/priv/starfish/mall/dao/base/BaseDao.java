package priv.starfish.mall.dao.base;

import java.io.Serializable;
import java.util.List;

import priv.starfish.common.model.Couple;

/**
 * 
 * @author koqiui
 *
 * @param <T>
 *            实体类
 * @param <K>
 *            实体主键类型
 */
public abstract interface BaseDao<T, K extends Serializable> {

	T selectById(K entityId);

	int insert(T entity);

	int update(T entity);

	int deleteById(K entityId);

	/** 获取数据库中实体总数量 */
	long getEntityCount(Class<?> entityClass);

	/** 获取数据库中实体的最大seqNo值 */
	int getEntityMaxSeqNo(Class<?> entityClass);

	/** 获取数据库中实体的某一字段值为给定值的最大seqNo值 */
	int getEntityMaxSeqNo(Class<?> entityClass, String refFieldName, Object refFieldValue);

	/** 获取实体类 id 为给定值的实体本身和其seqNo兄/弟的对组 */
	Couple<T, T> getSeqNoBrothersById(Class<T> entityClass, K id, boolean lesser);

	/** 获取实体类 id 为给定值 且某一字段值为给定值的 实体本身和其seqNo兄/弟的对组 */
	Couple<T, T> getSeqNoBrothersById(Class<T> entityClass, K id, String refFieldName, boolean lesser);

	/** 获取数据库中实体的某一字段值为给定值的所有实体id列表 */
	List<Integer> getEntityIds(Class<?> entityClass, String refFieldName, Object refFieldValue);

	/** 返回seqence模拟表的下一个Integer id */
	Integer newSeqInt(Class<?> entityClass);

	/** 返回seqence模拟表的下一个Long id */
	Long newSeqLong(Class<?> entityClass);

}
