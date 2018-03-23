package priv.starfish.mall.service.impl;

import org.springframework.stereotype.Service;
import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.mall.dao.market.ActivityDao;
import priv.starfish.mall.market.entity.Activity;
import priv.starfish.mall.service.ActivityService;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.List;

@Service("activityService")
public class ActivityServiceImpl extends BaseServiceImpl implements ActivityService {

	@Resource
	ActivityDao activityDao;

	@Override
	public Activity getActivityById(Integer id) {
		Activity activity = activityDao.selectById(id);
		this.filterFileBrowseUrl(activity);
		return activity;
	}

	@Override
	public Activity getActivityByYearAndNameAndTargetFlag(Integer year, String name, Integer targetFlag) {
		Activity activity = activityDao.selectByYearAndNameAndTargetFlag(year, name, targetFlag);
		this.filterFileBrowseUrl(activity);
		return activity;
	}

	@Override
	public boolean insert(Activity activity) {
		return activityDao.insert(activity) > 0;
	}

	@Override
	public boolean update(Activity activity) {
		return activityDao.update(activity) > 0;
	}

	@Override
	public boolean deleteActivityById(Integer id) {
		return activityDao.deleteById(id) > 0;
	}

	@Override
	public PaginatedList<Activity> getActivitysByFilter(PaginatedFilter paginatedFilter) {
		PaginatedList<Activity> activityData = activityDao.selectActivitys(paginatedFilter);
		List<Activity> listActivity = activityData.getRows();
		for (Activity activity : listActivity) {
			this.filterFileBrowseUrl(activity);
		}
		activityData.setRows(listActivity);
		return activityData;
	}

	@Override
	public boolean saveActivity(Activity activity) {
		if (activity.getId() == null) {
			Calendar calendar = Calendar.getInstance();
			activity.setYear(calendar.get(Calendar.YEAR));
			//
			if (activity.getStatus() == null) {
				activity.setStatus(0);
				;
			}
			return this.activityDao.insert(activity) > 0;
		} else {
			return this.activityDao.update(activity) > 0;
		}
	}

}
