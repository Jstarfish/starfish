package priv.starfish.mall.statis.dto;

import java.math.BigDecimal;

/**
 * 卫星店铺统计数据
 * 
 * @author 李江
 * @date 2016年2月21日 下午12:19:27
 *
 */
public class DistShopSumDto {
	/** 卫星店铺编号 */
	private Integer id;

	/** 卫星店铺名称 */
	private String name;
	
	/** 隶属店铺编号 */
	private Integer ownerShopId;
	
	/** 隶属店铺名称 */
	private String ownerShopName;
	
	/** 卫星店铺地区 */
	private String regionName;
	
	/** 服务次数 */
	private Long svcCount;
	
	/** 代理单数 */
	private Long agentCount;
	
	/** 承接单数 */
	private Long allocateCount;
	
	/** 收入金额 */
	private BigDecimal incomeAmount;

	/** 访客次数 */
	private Long visitorCount;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getOwnerShopId() {
		return ownerShopId;
	}

	public void setOwnerShopId(Integer ownerShopId) {
		this.ownerShopId = ownerShopId;
	}

	public String getOwnerShopName() {
		return ownerShopName;
	}

	public void setOwnerShopName(String ownerShopName) {
		this.ownerShopName = ownerShopName;
	}

	public String getRegionName() {
		return regionName;
	}

	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}

	public Long getSvcCount() {
		return svcCount;
	}

	public void setSvcCount(Long svcCount) {
		this.svcCount = svcCount;
	}

	public Long getAgentCount() {
		return agentCount;
	}

	public void setAgentCount(Long agentCount) {
		this.agentCount = agentCount;
	}

	public Long getAllocateCount() {
		return allocateCount;
	}

	public void setAllocateCount(Long allocateCount) {
		this.allocateCount = allocateCount;
	}

	public BigDecimal getIncomeAmount() {
		return incomeAmount;
	}

	public void setIncomeAmount(BigDecimal incomeAmount) {
		this.incomeAmount = incomeAmount;
	}

	public Long getVisitorCount() {
		return visitorCount;
	}

	public void setVisitorCount(Long visitorCount) {
		this.visitorCount = visitorCount;
	}
}
