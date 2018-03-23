package priv.starfish.mall.categ.dto;

import java.util.List;

import priv.starfish.mall.categ.entity.BrandDef;

public class GoodsCatDto {
	
	/** 商品分类Id */
	private Integer catId;
	
	/** 商品分类相关品牌列表 */
	private List<BrandDef> brands;
}
