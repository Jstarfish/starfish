package priv.starfish.mall.dao.comn.impl;

import org.springframework.stereotype.Component;
import priv.starfish.mall.comn.entity.RepoFile;
import priv.starfish.mall.dao.base.impl.BaseDaoImpl;
import priv.starfish.mall.dao.comn.RepoFileDao;

import java.util.Map;

@Component("repoFileDao")
public class RepoFileDaoImpl extends BaseDaoImpl<RepoFile, Long> implements RepoFileDao {
	@Override
	public RepoFile selectById(Long id) {
		String sqlId = this.getNamedSqlId("selectById");
		//
		return this.getSqlSession().selectOne(sqlId, id);
	}

	@Override
	public RepoFile selectByUuid(String uuid) {
		String sqlId = this.getNamedSqlId("selectByUuid");
		//
		return this.getSqlSession().selectOne(sqlId, uuid);
	}

	@Override
	public RepoFile selectByUsageAndRelPath(String usage, String relPath) {
		String sqlId = this.getNamedSqlId("selectByUsageAndRelPath");
		//
		Map<String, Object> params = this.newParamMap();
		params.put("usage", usage);
		params.put("relPath", relPath);
		//
		return this.getSqlSession().selectOne(sqlId, params);
	}

	@Override
	public int insert(RepoFile repoFile) {
		String sqlId = this.getNamedSqlId("insert");
		//
		return this.getSqlSession().insert(sqlId, repoFile);
	}

	@Override
	public int update(RepoFile repoFile) {
		String sqlId = this.getNamedSqlId("update");
		//
		return this.getSqlSession().update(sqlId, repoFile);
	}

	@Override
	public int deleteById(Long id) {
		String sqlId = this.getNamedSqlId("deleteById");
		//
		return this.getSqlSession().delete(sqlId, id);
	}

	@Override
	public int deleteByUuid(String uuid) {
		String sqlId = this.getNamedSqlId("deleteByUuid");
		//
		return this.getSqlSession().delete(sqlId, uuid);
	}
}