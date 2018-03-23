package priv.starfish.common.model;

import java.util.List;

public class TreeNode {

	/** 节点id */
	private Integer id;

	/** 节点名称 */
	private String name;

	/** 节点级别 */
	private Integer level;

	/** 节点前图标 */
	private String icon;

	/** 节点父id */
	private Integer parentId;

	/** id全路径 ,分割 */
	private String idPath;

	/** 节点父名称 */
	private String parentName;

	/** 该节点是否有子节点（即叶子节点） */
	private boolean leaf;

	/** 该节点是否是父节点 */
	private boolean isParent;

	/** 该节点展开/折叠状态：true/false */
	private boolean open;

	/** 该节点的所有子节点 */
	private List<TreeNode> children;

	public TreeNode() {
	}

	public TreeNode(Integer id) {
		this.id = id;
	}

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

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public String getIdPath() {
		return idPath;
	}

	public void setIdPath(String idPath) {
		this.idPath = idPath;
	}

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public boolean getLeaf() {
		return leaf;
	}

	public void setLeaf(boolean leaf) {
		this.leaf = leaf;
	}

	public boolean getIsParent() {
		return isParent;
	}

	public void setIsParent(boolean isParent) {
		this.isParent = isParent;
	}

	public boolean isOpen() {
		return open;
	}

	public void setOpen(boolean open) {
		this.open = open;
	}

	public List<TreeNode> getChildren() {
		return children;
	}

	public void setChildren(List<TreeNode> children) {
		this.children = children;
	}

}
