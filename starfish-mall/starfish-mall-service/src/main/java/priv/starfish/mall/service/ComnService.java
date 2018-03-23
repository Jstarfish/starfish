package priv.starfish.mall.service;

import java.util.List;

import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.mall.comn.entity.Faq;
import priv.starfish.mall.comn.entity.FaqCat;
import priv.starfish.mall.comn.entity.FaqGroup;

/**
 * 基础功能 
 * 目前只有常见问题功能模块
 * @author 邓华锋
 * @date 2015年9月19日 下午3:10:09
 *
 */
public interface ComnService extends BaseService {
	
	//---------------------------------------常见问题分类--------------------------------------------
	/**
	 * 常见问题分类分页
	 * @author 邓华锋
	 * @date 2015年9月19日 下午7:32:02
	 * 
	 * @param paginatedFilter 
	 * 						like name
	 * @return
	 */
	PaginatedList<FaqCat> getFaqCatsByFilter(PaginatedFilter paginatedFilter);
	
	/**
	 * 获取常见问题分类列表及关联的分组集合
	 * @author 邓华锋
	 * @date 2015年9月19日 下午7:32:19
	 * 
	 * @return
	 */
	public List<FaqCat> getFaqCats();
	
	/**
	 * 根据常见问题分类ID获取常见问题对象
	 * @author 邓华锋
	 * @date 2015年9月19日 下午7:33:06
	 * 
	 * @param id
	 * @return
	 */
	FaqCat getFaqCatById(Integer id);
	
	/**
	 * 判断是否已经存在常见问题分类
	 * @author 邓华锋
	 * @date 2015年9月30日 下午6:00:56
	 * 
	 * @param name
	 * @return
	 */
	boolean existFaqCatByName(String name);
	
	/**
	 * 保存常见问题分类
	 * @author 邓华锋
	 * @date 2015年9月19日 下午7:46:00
	 * 
	 * @param faqCat
	 * @return
	 */
	boolean saveFaqCat(FaqCat faqCat);
	
	/**
	 * 更新常见问题分类
	 * @author 邓华锋
	 * @date 2015年9月19日 下午7:46:26
	 * 
	 * @param faqCat
	 * @return
	 */
	boolean updateFaqCat(FaqCat faqCat);
	
	/**
	 * 删除常见问题分类
	 * @author 邓华锋
	 * @date 2015年9月19日 下午7:47:12
	 * 
	 * @param id
	 * @return
	 */
	boolean deleteFaqCatById(Integer id);
	
	//---------------------------------------常见问题分组--------------------------------------------
	/**
	 * 常见问题分组分页
	 * @author 邓华锋
	 * @date 2015年9月19日 下午7:47:52
	 * 
	 * @param paginatedFilter
	 * 						like name,= catId
	 * @return
	 */
	PaginatedList<FaqGroup> getFaqGroupsByFilter(PaginatedFilter paginatedFilter);
	
	/**
	 * 根据分类ID获取常见问题分组集合
	 * @author 邓华锋
	 * @date 2015年9月19日 下午3:10:08
	 * 
	 * @param catId
	 * @return
	 */
	public List<FaqGroup> getFaqGroupsByCatId(Integer catId);
	
	/**
	 * 根据分组ID获取常见问题分组
	 * @author 邓华锋
	 * @date 2015年9月19日 下午7:48:19
	 * 
	 * @param id
	 * @return
	 */
	FaqGroup getFaqGroupById(Integer id);
	
	/**
	 * 保存常见问题分组
	 * @author 邓华锋
	 * @date 2015年9月19日 下午7:49:11
	 * 
	 * @param faqGroup
	 * @return
	 */
	boolean saveFaqGroup(FaqGroup faqGroup);
	
	/**
	 * 更新常见问题分组
	 * @author 邓华锋
	 * @date 2015年9月19日 下午7:49:33
	 * 
	 * @param faqGroup
	 * @return
	 */
	boolean updateFaqGroup(FaqGroup faqGroup);
	
	/**
	 * 根据分组ID删除常见问题分组
	 * @author 邓华锋
	 * @date 2015年9月19日 下午7:49:46
	 * 
	 * @param groupId
	 * @return
	 */
	boolean deleteFaqGroupById(Integer groupId);
	
	//---------------------------------------常见问题--------------------------------------------
	/**
	 * 常见问题分页
	 * @author 邓华锋
	 * @date 2015年9月19日 下午7:51:16
	 * 
	 * @param paginatedFilter  
	 * 							question  like '%keyword%' OR answer  like '%keyword%'
	 * @return
	 */
	PaginatedList<Faq> getFaqsByFilter(PaginatedFilter paginatedFilter);
	
	/**
	 * 保存问题
	 * @author 邓华锋
	 * @date 2015年9月19日 下午7:52:31
	 * 
	 * @param faq
	 * @return
	 */
	boolean saveFaq(Faq faq);
	
	/**
	 * 更新常见问题
	 * @author 邓华锋
	 * @date 2015年9月19日 下午7:55:47
	 * 
	 * @param faq 
	 * @return
	 */
	boolean updateFaq(Faq faq);
	
	/**
	 * 根据常见问题ID获取常见问题
	 * @author 邓华锋
	 * @date 2015年10月12日 下午3:04:43
	 * 
	 * @param id
	 * @return
	 */
	Faq getFaqById(Integer id);
	
	/**
	 * 根据常见问题ID删除问题
	 * @author 邓华锋
	 * @date 2015年9月19日 下午7:56:01
	 * 
	 * @param id 
	 * @return
	 */
	boolean deleteFaqById(Integer id);
	
	//----------------------------------------------------------------------------------------
}
