package priv.starfish.common.model;

import java.util.List;
/**
 * 关联关系变化信息
 * @author koqiui
 * @date 2015年5月15日 上午2:00:55
 *
 */
public class RelChangeInfo {
	Integer mainId;
	List<Integer> subIdsAdded;
	List<Integer> subIdsDeleted;

	public Integer getMainId() {
		return mainId;
	}

	public void setMainId(Integer mainId) {
		this.mainId = mainId;
	}

	public List<Integer> getSubIdsAdded() {
		return subIdsAdded;
	}

	public void setSubIdsAdded(List<Integer> subIdsAdded) {
		this.subIdsAdded = subIdsAdded;
	}

	public List<Integer> getSubIdsDeleted() {
		return subIdsDeleted;
	}

	public void setSubIdsDeleted(List<Integer> subIdsDeleted) {
		this.subIdsDeleted = subIdsDeleted;
	}

}
