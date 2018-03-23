package priv.starfish.mall.dao.comn;

import java.util.List;

import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.mall.dao.base.BaseDao;
import priv.starfish.mall.comn.entity.BankProvinceCode;

@IBatisSqlTarget
public interface BankProvinceCodeDao extends BaseDao<BankProvinceCode, Integer> {
	BankProvinceCode selectById(Integer id);

	int insert(BankProvinceCode bankProvinceCode);

	int update(BankProvinceCode bankProvinceCode);

	int deleteById(Integer id);

	List<BankProvinceCode> selectAll();
}