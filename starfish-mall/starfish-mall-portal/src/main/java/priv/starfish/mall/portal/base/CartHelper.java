/*
package priv.starfish.mall.portal.base;

import priv.starfish.common.user.UserContext;
import priv.starfish.common.user.UserContextHolder;
import priv.starfish.common.util.WebUtil;
import priv.starfish.common.web.WebEnvHelper;
import priv.starfish.mall.cart.dict.CartAction;
import priv.starfish.mall.cart.entity.SaleCart;
import priv.starfish.mall.cart.entity.SaleCartGoods;
import priv.starfish.mall.cart.entity.SaleCartSvc;
import priv.starfish.mall.cart.po.SaleCartCheckPo;
import priv.starfish.mall.cart.po.SaleCartGoodsPo;
import priv.starfish.mall.cart.po.SaleCartSvcPo;
import priv.starfish.mall.goods.entity.Product;
import priv.starfish.mall.service.CarSvcService;
import priv.starfish.mall.service.GoodsService;
import priv.starfish.mall.service.SaleOrderService;
import priv.starfish.mall.service.misc.CalcSaleHelper;
import priv.starfish.mall.svcx.entity.Svcx;

import javax.servlet.http.HttpSession;
import java.util.*;

*/
/**
 * 购物车辅助类
 * 
 * @author koqiui
 * @date 2015年10月26日 上午10:16:51
 *
 *//*


public final class CartHelper {

	private CartHelper() {
		//
	}

	private static GoodsService goodsService;

	private static CarSvcService carSvcService;

	private static SaleOrderService saleOrderService;

	// 初始化话引用的bean对象等
	public static void initRefObjects() {
		// searchService = WebEnvHelper.getSpringBean("searchService", SearchSvc.class);
		goodsService = WebEnvHelper.getSpringBean("goodsService", GoodsService.class);
		carSvcService = WebEnvHelper.getSpringBean("carSvcService", CarSvcService.class);
		saleOrderService = WebEnvHelper.getSpringBean("saleOrderService", SaleOrderService.class);
	}

	// 从购物车获取 //计算带checkBox
	public static SaleCart fetchSvcCartInfo(HttpSession session, String falgType) {
		SaleCart saleCart = getSvcCartInfo(session);
		if (saleCart != null) {
			saleCart = calcSaleCart(saleCart.getSaleCartSvcList(), saleCart.getSaleCartGoods(), falgType);
		}
		return saleCart;
	}

	// 计算购物车方法
	public static SaleCart calcSaleCart(List<SaleCartSvc> svcList, List<SaleCartGoods> goods, String falgType) {
		SaleCart saleCart = null;
		if (svcList != null && svcList.size() > 0 || goods != null && goods.size() > 0) {
			// 计算购物车
			saleCart = CalcSaleHelper.calcSaleCart(svcList, goods, falgType);
			// 返回
			return saleCart;
		} else {
			return saleCart;
		}
	}

	// 服务同步购物车
	public static SaleCart syncSaleCartSvc(HttpSession session, SaleCartSvcPo cartSvcPo) {
		SaleCart saleCart = getSvcCartInfo(session);
		// 是否有购物车
		if (saleCart != null) {
			// 得到购物车服务列表
			List<SaleCartSvc> saleCartSvcList = saleCart.getSaleCartSvcList();
			if (saleCartSvcList != null && saleCartSvcList.size() > 0) {
				// 是否存在该服务
				Boolean existSvc = false;
				for (SaleCartSvc saleCartSvc : saleCartSvcList) {
					if (saleCartSvc.getSvcId().equals(cartSvcPo.svcId)) {
						existSvc = true;
					}
				}
				// 存在
				if (existSvc) {
					SaleCartSvc minusSaleCartSvc = null;// 删除用
					for (SaleCartSvc saleCartSvc : saleCartSvcList) {
						if (saleCartSvc.getSvcId().equals(cartSvcPo.svcId)) {
							if (CartAction.add.equals(cartSvcPo.action)) {// 增加操作
								saleCartSvc = getcarSvc(cartSvcPo.svcId, getUserId(session));
							} else if (CartAction.minus.equals(cartSvcPo.action)) {// 减少操作
								// 删除服务
								minusSaleCartSvc = saleCartSvc;
							} else if (CartAction.update.equals(cartSvcPo.action)) {// 更新操作
								Boolean checkFlag = cartSvcPo.checkFlag;
								if (checkFlag != null) {
									saleCartSvc.setCheckFlag(checkFlag);
								}
							}
						}
					}
					// 删除服务
					if (minusSaleCartSvc != null) {
						saleCartSvcList.remove(minusSaleCartSvc);
						if (saleCartSvcList == null || saleCartSvcList.size() <= 0) {
							List<SaleCartGoods> goods = saleCart.getSaleCartGoods();
							if (goods == null || goods.size() <= 0) {
								saleCart = null;
							}
						}
					}
				} else {
					saleCartSvcList.add(getcarSvc(cartSvcPo.svcId, getUserId(session)));
				}
			} else {
				List<SaleCartSvc> svcList = new ArrayList<SaleCartSvc>();
				SaleCartSvc saleCartSvc = getcarSvc(cartSvcPo.svcId, getUserId(session));
				svcList.add(saleCartSvc);
				saleCart.setSaleCartSvcList(svcList);
			}
		} else {
			saleCart = new SaleCart();
			List<SaleCartSvc> saleCartSvcList = new ArrayList<SaleCartSvc>();
			saleCartSvcList.add(getcarSvc(cartSvcPo.svcId, getUserId(session)));
			saleCart.setSaleCartSvcList(saleCartSvcList);
		}
		syncSvcCartInfo(session, saleCart);
		if (saleCart != null) {
			saleCart = calcSaleCart(saleCart.getSaleCartSvcList(), saleCart.getSaleCartGoods(), "check");
		}
		return saleCart;
	}

	// 商品同步购物车
	public static SaleCart syncSvcCartGoods(HttpSession session, SaleCartGoodsPo goodsPo) {
		SaleCart saleCart = getSvcCartInfo(session);
		if (saleCart != null) {
			List<SaleCartGoods> goodsList = saleCart.getSaleCartGoods();
			if (goodsList != null && goodsList.size() > 0) {
				Boolean existGoods = false;// 是否存在商品
				for (SaleCartGoods saleCartGoods : goodsList) {
					if (saleCartGoods.getProductId().equals(goodsPo.productId)) {
						existGoods = true;
					}
				}
				// 判断是否存在商品
				if (existGoods) {
					//
					SaleCartGoods cartGoods = null;
					for (SaleCartGoods saleCartGoods : goodsList) {
						if (saleCartGoods.getProductId().equals(goodsPo.productId)) {
							// 是增加商品换是减少商品
							if (CartAction.add.equals(goodsPo.action)) {// 增加操作
								saleCartGoods.setQuantity(saleCartGoods.getQuantity() + goodsPo.quantity);
								saleCartGoods.setCheckFlag(true);
							} else if (CartAction.minus.equals(goodsPo.action)) {// 减少操作
								if ((saleCartGoods.getQuantity() - goodsPo.quantity) > 0) {
									saleCartGoods.setQuantity((saleCartGoods.getQuantity() - goodsPo.quantity));
								} else {
									// 删除商品
									cartGoods = saleCartGoods;
								}
							} else if (CartAction.update.equals(goodsPo.action)) {
								Boolean checkFlag = goodsPo.checkFlag;
								if (checkFlag != null) {
									saleCartGoods.setCheckFlag(checkFlag);
								}
								Integer quantity = goodsPo.quantity;
								if (quantity != null) {
									saleCartGoods.setQuantity(quantity);
								}
							}
						}
					}
					// 删除商品
					if (cartGoods != null) {
						goodsList.remove(cartGoods);
						if (goodsList == null || goodsList.size() <= 0) {
							List<SaleCartSvc> listSvc = saleCart.getSaleCartSvcList();
							if (listSvc == null || listSvc.size() <= 0) {
								saleCart = null;
							}
						}
					}
				} else {
					goodsList.add(getSaleCartGoods(goodsPo, saleCart.getId()));
				}
			} else {
				List<SaleCartGoods> goods = new ArrayList<SaleCartGoods>();
				goods.add(getSaleCartGoods(goodsPo, saleCart.getId()));
				saleCart.setSaleCartGoods(goods);
			}
		} else {
			Integer userId = getUserId(session);
			saleCart = getSaleCart(userId);
			List<SaleCartGoods> goods = new ArrayList<SaleCartGoods>();
			goods.add(getSaleCartGoods(goodsPo, userId));
			saleCart.setSaleCartGoods(goods);
		}
		syncSvcCartInfo(session, saleCart);
		if (saleCart != null) {
			saleCart = calcSaleCart(saleCart.getSaleCartSvcList(), saleCart.getSaleCartGoods(), "check");
		}
		return saleCart;
	}

	// 选中删除用和清除购物车用
	public static void syncCheckSaleCart(HttpSession session, SaleCartCheckPo cartPo) {
		if (cartPo != null) {
			List<SaleCartSvcPo> svcPoList = cartPo.svcPoList;
			if (svcPoList != null && svcPoList.size() > 0) {
				for (SaleCartSvcPo saleCartSvcPo : svcPoList) {
					if (saleCartSvcPo != null) {
						syncSaleCartSvc(session, saleCartSvcPo);
					}
				}
			}
			List<SaleCartGoodsPo> goodsPoList = cartPo.goodsPoList;
			if (goodsPoList != null && goodsPoList.size() > 0) {
				for (SaleCartGoodsPo saleCartGoodsPo : goodsPoList) {
					if (saleCartGoodsPo != null) {
						syncSvcCartGoods(session, saleCartGoodsPo);
					}
				}
			}
		}
	}

	public static SaleCart getSaleCart(Integer userId) {
		SaleCart saleCart = SaleCart.newOne();
		// 车辆信息暂定
		if (userId != null) {
			saleCart.setId(userId);
		}
		saleCart.setTs(new Date());
		return saleCart;
	}

	public static SaleCartGoods getSaleCartGoods(SaleCartGoodsPo goodsPo, Integer cartId) {
		Product product = goodsService.getProductById(goodsPo.productId);
		SaleCartGoods saleCartGoods = new SaleCartGoods();
		saleCartGoods.setCartId(cartId);
		saleCartGoods.setGoodsId(product.getGoodsId());
		saleCartGoods.setCheckFlag(true);
		saleCartGoods.setProductAmount(product.getSalePrice());
		saleCartGoods.setProductId(product.getId());
		saleCartGoods.setProductName(product.getTitle());
		saleCartGoods.setQuantity(goodsPo.quantity);
		if (product.getProductAlbumImgs() != null && product.getProductAlbumImgs().size() > 0) {
			saleCartGoods.setProductAlbumImg(product.getProductAlbumImgs().get(0).getFileBrowseUrl());
		}
		saleCartGoods.setTs(new Date());
		return saleCartGoods;
	}

	public static SaleCartSvc getcarSvc(Integer svcId, Integer cartId) {
		Svcx carSvc = carSvcService.getCarSvc(svcId);
		SaleCartSvc saleCartSvc = null;
		if (carSvc != null) {
			saleCartSvc = new SaleCartSvc();
			saleCartSvc.setSvcId(carSvc.getId());
			saleCartSvc.setCartId(cartId);
			saleCartSvc.setSalePrice(carSvc.getSalePrice());
			saleCartSvc.setCarSvcName(carSvc.getName());
			saleCartSvc.setCheckFlag(true);
			saleCartSvc.setTs(new Date());
			saleCartSvc.setSvcxAlbumImg(carSvc.getFileBrowseUrl());
			saleCartSvc.setSvcxAlbumImgApp(carSvc.getFileBrowseUrlApp());
			saleCartSvc.setDesc(carSvc.getDesc());
		}
		return saleCartSvc;
	}

	// 订单同步 购物车(暂定)
	public static SaleCart syncSvcCart(HttpSession session, SaleCartSvc svc, Integer userId) {
		SaleCart saleCart = getSvcCartInfo(session);
		// 查询服务
		Svcx carSvc = carSvcService.getCarSvc(svc.getSvcId());
		if (saleCart != null) {
			List<SaleCartSvc> saleCartSvcList = saleCart.getSaleCartSvcList();
			SaleCartSvc saleCartSvc = null;
			if (saleCartSvcList != null && saleCartSvcList.size() > 0) {
				// 是否存在改服务
				Boolean existSvc = false;
				for (SaleCartSvc cartSvc : saleCartSvcList) {
					if (svc.getSvcId().equals(cartSvc.getSvcId())) {
						// 判断是不是自选
						if (svc.getSvcId().equals(-1)) {
							// 获取购物车自选商品
							List<SaleCartGoods> listSaleCartGoods = cartSvc.getSaleCartGoodsList();
							//
							Map<Long, SaleCartGoods> mapGoods = new HashMap<Long, SaleCartGoods>();
							// 判断自选是否存在商品
							if (listSaleCartGoods != null && listSaleCartGoods.size() > 0) {
								for (SaleCartGoods saleCartGoods : listSaleCartGoods) {
									if (saleCartGoods != null) {
										mapGoods.put(saleCartGoods.getProductId(), saleCartGoods);
									}
								}
								// 订单中自选商品列表
								List<SaleCartGoods> listItem = svc.getSaleCartGoodsList();
								if (listItem != null && listItem.size() > 0) {
									List<SaleCartGoods> list = new ArrayList<SaleCartGoods>();
									for (SaleCartGoods goodsItem : listItem) {
										if (goodsItem != null) {
											if (!mapGoods.containsKey(goodsItem.getProductId())) {
												list.add(goodsItem);
											} else {
												SaleCartGoods saleCartGoods = mapGoods.get(goodsItem.getProductId());
												Integer quantity = goodsItem.getQuantity() + saleCartGoods.getQuantity();
												goodsItem.setQuantity(quantity);
												listSaleCartGoods.remove(saleCartGoods);
												list.add(goodsItem);
											}
										}

									}
									listSaleCartGoods.addAll(list);
								}
							} else {
								if (listSaleCartGoods == null) {
									listSaleCartGoods = new ArrayList<SaleCartGoods>();
								}
								listSaleCartGoods.addAll(svc.getSaleCartGoodsList());
							}
						}
						existSvc = true;
					}
				}
				if (!existSvc) {
					// 不存在保存服务
					saleCartSvc = svc;
					if (carSvc != null) {
						saleCartSvc.setDesc(carSvc.getDesc());
					}
					// 判断是否存在商品没有直接保存
					if (svc.getSaleCartGoodsList() != null && svc.getSaleCartGoodsList().size() > 0) {
						saleCartSvc.setSaleCartGoodsList(svc.getSaleCartGoodsList());
					}
					saleCartSvcList.add(saleCartSvc);
				}
			} else {
				// 没有服务直接保存
				saleCartSvc = svc;
				if (carSvc != null) {
					saleCartSvc.setDesc(carSvc.getDesc());
				}
				saleCartSvc.setSaleCartGoodsList(svc.getSaleCartGoodsList());
				if (saleCartSvcList == null) {
					saleCartSvcList = new ArrayList<SaleCartSvc>();
				}
				saleCartSvcList.add(saleCartSvc);
			}
		} else {
			// 没有购物车
			saleCart = getSaleCart(userId);
			SaleCartSvc saleCartSvc = svc;
			if (carSvc != null) {
				saleCartSvc.setDesc(carSvc.getDesc());
			}
			saleCartSvc.setSaleCartGoodsList(svc.getSaleCartGoodsList());
			List<SaleCartSvc> saleCartSvcList = new ArrayList<SaleCartSvc>();
			saleCartSvcList.add(saleCartSvc);
			saleCart.setSaleCartSvcList(saleCartSvcList);
		}
		syncSvcCartInfo(session, saleCart);
		return saleCart;
	}

	// 同步服务购物车
	public static void syncSvcCartInfo(HttpSession session, SaleCart saleCart) {
		UserContext userContext = getUserContext(session);
		if (userContext.isSysUser()) {
			CacheHelper.setUCData(userContext.getUserId(), AppBase.SESSION_KEY_SALE_CART, saleCart);
		} else {
			WebUtil.setSessionAttribute(session, AppBase.SESSION_KEY_SALE_CART, saleCart);
		}
	}

	// 获取购物车信息
	public static SaleCart getSvcCartInfo(HttpSession session) {
		UserContext userContext = getUserContext(session);
		if (userContext.isSysUser()) {
			return CacheHelper.getUCData(userContext.getUserId(), AppBase.SESSION_KEY_SALE_CART);
		} else {
			return WebUtil.getSessionAttribute(session, AppBase.SESSION_KEY_SALE_CART);
		}
	}

	// 同步全部服务购物车
	public static SaleCart syncSvcCartAllInfo(HttpSession session, List<SaleCartSvcPo> listPos) {
		if (listPos != null && listPos.size() > 0) {
			SaleCartSvc saleCartSvc = null;
			SaleCart saleCart = null;
			List<SaleCartSvc> listSaleCartSvc = new ArrayList<SaleCartSvc>();
			List<SaleCartSvcPo> listPo = listPos;
			for (SaleCartSvcPo saleCartSvcPo : listPo) {
				if (CartAction.add.equals(saleCartSvcPo.action)) {
					saleCartSvc = getcarSvc(saleCartSvcPo.svcId, getUserId(session));
					listSaleCartSvc.add(saleCartSvc);
				}
			}
			UserContext userContext = getUserContext(session);
			if (listSaleCartSvc != null && listSaleCartSvc.size() > 0) {
				saleCart = getSaleCart(userContext.getUserId());
				saleCart.setSaleCartSvcList(listSaleCartSvc);
			}
			syncSvcCartInfo(session, saleCart);
			if (saleCart != null) {
				saleCart = getSvcCartInfo(session);
			}
			return saleCart;
		} else {
			return null;
		}
	}

	//
	public static SaleCart fetchSaleCartInfo(HttpSession session) {
		SaleCart saleCartCheck = null;
		SaleCart saleCart = getSvcCartInfo(session);
		List<SaleCartSvc> svcList = new ArrayList<SaleCartSvc>();
		List<SaleCartGoods> goodsList = new ArrayList<SaleCartGoods>();
		if (saleCart != null) {
			List<SaleCartSvc> saleCartSvcList = saleCart.getSaleCartSvcList();
			if (saleCartSvcList != null && saleCartSvcList.size() > 0) {
				Integer userId = getUserId(session);
				if (userId != null) {
					saleOrderService.calcRankRate(saleCartSvcList, userId, -1);
				}
				for (SaleCartSvc saleCartSvc : saleCartSvcList) {
					if (saleCartSvc != null) {
						if (saleCartSvc.getCheckFlag() == true) {
							svcList.add(saleCartSvc);
						}
					}
				}
			}
			List<SaleCartGoods> goods = saleCart.getSaleCartGoods();
			if (goods != null && goods.size() > 0) {
				for (SaleCartGoods saleCartGoods : goods) {
					if (saleCartGoods != null) {
						if (saleCartGoods.getCheckFlag() == true) {
							goodsList.add(saleCartGoods);
						}
					}
				}
			}
			saleCartCheck = calcSaleCart(svcList, goodsList, "check");
		}
		return saleCartCheck;
	}

	*/
/**
	 * 查询购物车数量，首页购物车用
	 * 
	 * @param session
	 * @return
	 *//*

	public static Integer fetchSaleCartCount(HttpSession session) {
		SaleCart saleCart = getSvcCartInfo(session);
		//
		Integer cartCount = 0;
		if (saleCart != null) {
			List<SaleCartSvc> svcList = saleCart.getSaleCartSvcList();
			if (svcList != null && svcList.size() > 0) {
				for (SaleCartSvc svcCart : svcList) {
					if (svcCart != null) {
						cartCount += 1;
					}
				}
			}
			List<SaleCartGoods> goods = saleCart.getSaleCartGoods();
			if (goods != null && goods.size() > 0) {
				for (SaleCartGoods cartGoods : goods) {
					if (cartGoods != null) {
						cartCount += cartGoods.getQuantity();
					}
				}
			}
		}
		return cartCount;
	}

	// 同步cache购物车（session放入Cache）
	public static void mergeCenterCacheCart(HttpSession session) {
		// 获取用户信息
		UserContext userContext = getUserContext(session);
		Integer userId = userContext.getUserId();
		//
		if (userContext.isSysUser()) {
			// 获取session购物车信息
			SaleCart cartSession = WebUtil.getSessionAttribute(session, AppBase.SESSION_KEY_SALE_CART);
			// 空购物车不同步
			if (cartSession != null) {
				// 获取缓存中的购物车
				SaleCart cartCache = CacheHelper.getUCData(userId, AppBase.SESSION_KEY_SALE_CART);
				// 判断缓存中是否为空
				if (cartCache != null) {
					List<SaleCartSvc> svcSessionList = cartSession.getSaleCartSvcList();
					if (svcSessionList != null && svcSessionList.size() > 0) {
						// 保存要同步的Cache购物车服务
						Map<Integer, SaleCartSvc> mapSvcCache = new HashMap<Integer, SaleCartSvc>();
						// 取得购物车中服务
						List<SaleCartSvc> svcCacheList = cartCache.getSaleCartSvcList();

						// 判断服务是否为空
						if (svcCacheList != null && svcCacheList.size() > 0) {
							// 遍历保存Cache中服务
							for (SaleCartSvc saleCartSvc : svcCacheList) {
								Integer svcId = saleCartSvc.getSvcId();
								//
								mapSvcCache.put(svcId, saleCartSvc);
							}
							// 遍历session中服务
							for (SaleCartSvc saleCartSvc : svcSessionList) {
								Integer svcId = saleCartSvc.getSvcId();
								// 判断缓存中是否包含此项服务
								mapSvcCache.put(svcId, saleCartSvc);
							}
							// 存放最后服务
							List<SaleCartSvc> listSvc = new ArrayList<SaleCartSvc>();
							//
							for (Map.Entry<Integer, SaleCartSvc> entry : mapSvcCache.entrySet()) {
								SaleCartSvc cartSvcCache = entry.getValue();
								listSvc.add(cartSvcCache);
							}
							cartCache.setSaleCartSvcList(listSvc);
						} else {
							// 为空保存
							cartCache.setSaleCartSvcList(svcSessionList);
						}
					}
					// 得到session中服务商品
					List<SaleCartGoods> goodsSession = cartSession.getSaleCartGoods();
					// session商品
					if (goodsSession != null && goodsSession.size() > 0) {
						// 存放要Cache的商品
						Map<Long, SaleCartGoods> mapGoodsCache = new HashMap<Long, SaleCartGoods>();
						// 拿到缓存中此项商品
						List<SaleCartGoods> goodsCache = cartCache.getSaleCartGoods();
						// 遍历cache中商品
						if (goodsCache != null && goodsCache.size() > 0) {
							for (SaleCartGoods saleCartGoods : goodsCache) {
								Long productId = saleCartGoods.getProductId();
								//
								mapGoodsCache.put(productId, saleCartGoods);
							}
							// 遍历session中商品
							for (SaleCartGoods saleCartGoods : goodsSession) {
								//
								Long productId = saleCartGoods.getProductId();
								// 判断是否包含session中商品
								if (mapGoodsCache.containsKey(productId)) {
									// TODO:拿到cache商品(有疑问商品价格是否一直不变？)
									SaleCartGoods cartGoodsCache = mapGoodsCache.get(productId);
									//
									Integer quantity = cartGoodsCache.getQuantity() + saleCartGoods.getQuantity();
									//
									cartGoodsCache.setQuantity(quantity);
								} else {
									// 不包含
									mapGoodsCache.put(productId, saleCartGoods);
								}
							}

							// 存放最后商品
							List<SaleCartGoods> listGoods = new ArrayList<SaleCartGoods>();
							//
							for (Map.Entry<Long, SaleCartGoods> entry : mapGoodsCache.entrySet()) {
								SaleCartGoods cartGoodsCache = entry.getValue();
								listGoods.add(cartGoodsCache);
							}
							cartCache.setSaleCartGoods(listGoods);
						} else {
							cartCache.setSaleCartGoods(goodsSession);
						}
					}
				} else {
					// 为空直接保存
					cartCache = getSaleCart(userId);
					cartCache.setSaleCartSvcList(cartSession.getSaleCartSvcList());
					cartCache.setSaleCartGoods(cartSession.getSaleCartGoods());
				}
				// 保存
				CacheHelper.setUCData(userId, AppBase.SESSION_KEY_SALE_CART, cartCache);
				// 清空session购物车
				session.removeAttribute(AppBase.SESSION_KEY_SALE_CART);
			}
		}
	}

	// 清除服务购物车
	public static void syncSvcCartClearInfo(HttpSession session) {
		UserContext userContext = getUserContext(session);
		if (userContext.isSysUser()) {
			Integer userId = userContext.getUserId();
			CacheHelper.setUCData(userId, AppBase.SESSION_KEY_SALE_CART, null);
		} else {
			session.removeAttribute(AppBase.SESSION_KEY_SALE_CART);
		}

	}

	private static Integer getUserId(HttpSession session) {
		Integer userId = null;
		UserContext userContext = getUserContext(session);
		if (userContext != null) {
			userId = userContext.getUserId();
		}
		return userId;
	}

	// 获取用户信息
	protected static UserContext getUserContext(HttpSession session) {
		return WebUtil.getSessionAttribute(session, UserContextHolder.SESSION_KEY_USER_CONTEXT);
	}
}
*/
