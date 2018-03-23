package priv.starfish.mall.dao.comn;

import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.mall.dao.base.BaseDao;
import priv.starfish.mall.comn.entity.RepoFile;

@IBatisSqlTarget
public interface RepoFileDao extends BaseDao<RepoFile, Long> {
	RepoFile selectById(Long id);

	RepoFile selectByUuid(String uuid);

	RepoFile selectByUsageAndRelPath(String usage, String relPath);

	int insert(RepoFile repoFile);

	int update(RepoFile repoFile);

	int deleteById(Long id);

	int deleteByUuid(String uuid);

}