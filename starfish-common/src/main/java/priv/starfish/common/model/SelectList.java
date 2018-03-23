package priv.starfish.common.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import priv.starfish.common.annotation.AsSelectList;
import priv.starfish.common.base.AnnotatedClassScanner;
import priv.starfish.common.util.EnumUtil;

public class SelectList {

	List<SelectItem> items = new ArrayList<SelectItem>();
	SelectItem unSelectedItem;
	String defaultValue;

	// ----------------
	public SelectList addItem(String value, String text) {
		this.items.add(SelectItem.newOne(value, text));
		//
		return this;
	}

	public SelectList addItem(SelectItem item) {
		this.items.add(item);
		//
		return this;
	}

	public SelectList setItems(List<SelectItem> items) {
		this.items.clear();
		if (items != null) {
			this.items.addAll(items);
		}
		//
		return this;
	}

	public List<SelectItem> getItems() {
		return items;
	}

	// ----------------
	public SelectList setUnSelectedItem(String value, String text) {
		this.unSelectedItem = SelectItem.newOne(value, text);
		//
		return this;
	}

	public SelectList setUnSelectedItem(SelectItem unSelectedItem) {
		this.unSelectedItem = unSelectedItem;
		//
		return this;
	}

	public SelectItem getUnSelectedItem() {
		return unSelectedItem;
	}

	// ----------------
	public SelectList setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
		//
		return this;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	//
	public static SelectList newOne() {
		return new SelectList();
	}

	public static SelectList newOne(Class<? extends Enum<?>> enumClass) {
		SelectList selectList = new SelectList();
		List<SelectItem> selItems = EnumUtil.toSelectItems(enumClass);
		selectList.setItems(selItems);
		return selectList;
	}

	/**
	 * 扫描(被AsSelectList标注的作为)选择列表的类
	 * 
	 * @author koqiui
	 * @date 2015年7月12日 下午11:55:43
	 * 
	 * @param packagesToScan
	 * @return
	 */
	public static Map<String, SelectList> scanDictSelectLists(String... packagesToScan) {
		Map<String, SelectList> retListsMap = new LinkedHashMap<String, SelectList>();
		AnnotatedClassScanner classScanner = new AnnotatedClassScanner(packagesToScan, AsSelectList.class);
		try {
			System.out.println("--  扫描@AsSelectList  -- ");
			Set<Class<?>> dictClasses = classScanner.getClassSet();
			for (Class<?> dictClass : dictClasses) {
				AsSelectList anno = dictClass.getAnnotation(AsSelectList.class);
				String name = anno.name();
				SelectList selectList = SelectList.newOne((Class<? extends Enum<?>>) dictClass);
				retListsMap.put(name, selectList);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return retListsMap;
	}
}
