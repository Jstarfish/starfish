package priv.starfish.common.dag;

import priv.starfish.common.util.TypeUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * 有向图 （用于根据依赖关系检测依赖环，按先后顺序输出有向图中的节点）
 * 
 * @author koqiui
 *
 * @param <T>
 *            节点类型
 */
public class DirectedGraph<T> {
	private Set<DirectedEdge<T>> allEdges = new HashSet<DirectedEdge<T>>();

	public static <T> DirectedGraph<T> newOne() {
		return new DirectedGraph<T>();
	}

	public boolean addEdge(DirectedEdge<T> edge) {
		if (!edge.isValid()) {
			throw new RuntimeException("无效的有向边：源节点和目标节点不能相同！");
		}
		return this.allEdges.add(edge);
	}

	public boolean addEdges(Collection<? extends DirectedEdge<T>> edges) {
		boolean changed = false;
		for (DirectedEdge<T> edge : edges) {
			changed = this.addEdge(edge) || changed;
		}
		return changed;
	}

	public boolean addEdge(T srcNode, T dstNode) {
		return this.addEdge(DirectedEdge.newOne(srcNode, dstNode));
	}

	public Set<T> getAllEdgeNodes() {
		Set<T> allNodes = new HashSet<T>();
		for (DirectedEdge<T> edge : allEdges) {
			allNodes.add(edge.getSrcNode());
			allNodes.add(edge.getDstNode());
		}
		return allNodes;
	}

	/**
	 * 获取直接出向边
	 * 
	 * @author koqiui
	 * @date 2016年1月21日 下午9:13:12
	 * 
	 * @param srcNode
	 * @return
	 */
	public Set<DirectedEdge<T>> getFanoutEdges(T srcNode) {
		Set<DirectedEdge<T>> fanoutEdges = new HashSet<DirectedEdge<T>>();
		for (DirectedEdge<T> edge : allEdges) {
			if (edge.getSrcNode().equals(srcNode)) {
				fanoutEdges.add(edge);
			}
		}
		return fanoutEdges;
	}

	/**
	 * 获取直接出向节点
	 * 
	 * @author koqiui
	 * @date 2016年2月28日 上午12:58:52
	 * 
	 * @param srcNode
	 * @return
	 */
	public List<T> getFanoutNodes(T srcNode) {
		List<T> fanoutNodes = new ArrayList<T>();
		for (DirectedEdge<T> edge : allEdges) {
			if (edge.getSrcNode().equals(srcNode)) {
				fanoutNodes.add(edge.getDstNode());
			}
		}
		return fanoutNodes;
	}

	/**
	 * 根据依赖遍增依赖/目标节点的权重
	 * 
	 * @param refNode
	 * @param nodeWeights
	 */
	private void incrementAllEdgeNodeWeightsFrom(T refNode, Map<T, Integer> nodeWeights) {
		nodeWeights.put(refNode, nodeWeights.get(refNode) + 1);
		Set<DirectedEdge<T>> fanoutEdges = this.getFanoutEdges(refNode);
		for (DirectedEdge<T> edge : fanoutEdges) {
			T dstNode = edge.getDstNode();
			incrementAllEdgeNodeWeightsFrom(dstNode, nodeWeights);
		}
	}

	/**
	 * 获取直接入向边
	 * 
	 * @author koqiui
	 * @date 2016年1月21日 下午9:13:46
	 * 
	 * @param dstNode
	 * @return
	 */
	public Set<DirectedEdge<T>> getFaninEdges(T dstNode) {
		Set<DirectedEdge<T>> faninEdges = new HashSet<DirectedEdge<T>>();
		for (DirectedEdge<T> edge : allEdges) {
			if (edge.getDstNode().equals(dstNode)) {
				faninEdges.add(edge);
			}
		}
		return faninEdges;
	}

	private void fillAllFaninEdgesTo(T dstNode, Set<DirectedEdge<T>> edges) {
		Set<DirectedEdge<T>> tmpEdges = getFaninEdges(dstNode);
		if (tmpEdges.isEmpty()) {
			return;
		}
		edges.addAll(tmpEdges);
		//
		for (DirectedEdge<T> tmpEdge : tmpEdges) {
			T srcNode = tmpEdge.getSrcNode();
			fillAllFaninEdgesTo(srcNode, edges);
		}
	}

	public Set<DirectedEdge<T>> getAllEdgesDependingOn(T dstNode) {
		Set<DirectedEdge<T>> edges = new HashSet<DirectedEdge<T>>();
		fillAllFaninEdgesTo(dstNode, edges);
		return edges;
	}

	/**
	 * 根据依赖遍增访问/源头节点的权重
	 * 
	 * @author koqiui
	 * @date 2016年1月21日 下午9:07:36
	 * 
	 * @param refNode
	 * @param nodeWeights
	 */
	private void incrementAllEdgeNodeWeightsTo(T refNode, Map<T, Integer> nodeWeights) {
		nodeWeights.put(refNode, nodeWeights.get(refNode) + 1);
		Set<DirectedEdge<T>> faninEdges = this.getFaninEdges(refNode);
		for (DirectedEdge<T> edge : faninEdges) {
			T srcNode = edge.getSrcNode();
			incrementAllEdgeNodeWeightsTo(srcNode, nodeWeights);
		}
	}

	/**
	 * 判断是否为无环图
	 * 
	 * @return
	 */
	public boolean isAcyclic() {
		Set<T> allNodes = this.getAllEdgeNodes();
		for (T node : allNodes) {
			List<T> priorNodes = new ArrayList<T>();
			if (!isAcyclic(node, priorNodes)) {
				return false;
			}
		}
		return true;
	}

	private boolean isAcyclic(T curNode, List<T> priorNodes) {
		if (priorNodes.contains(curNode)) {
			System.err.println("发现循环路径：" + priorNodes + " -> " + curNode);
			return false;
		}
		priorNodes.add(curNode);
		// System.out.println(priorNodes);
		Set<DirectedEdge<T>> fanoutEdges = this.getFanoutEdges(curNode);
		for (DirectedEdge<T> edge : fanoutEdges) {
			T dstNode = edge.getDstNode();
			if (!isAcyclic(dstNode, priorNodes)) {
				return false;
			} else {
				priorNodes.remove(priorNodes.size() - 1);
				// System.out.println(priorNodes);
			}
		}
		return true;
	}

	/**
	 * 获取解析后的节点（按【创建】依赖顺序）
	 * 
	 * @return
	 */
	public List<T> getArrangedEdgeNodes() {
		if (!this.isAcyclic()) {
			throw new RuntimeException("解析节点出错，存在循环路径！");
		}
		List<T> arrangedNodes = new ArrayList<T>();
		//
		Map<T, Integer> nodeWeights = new HashMap<T, Integer>();
		Set<T> allNodes = this.getAllEdgeNodes();
		for (T node : allNodes) {
			nodeWeights.put(node, 0);
		}
		for (DirectedEdge<T> edge : allEdges) {
			T dstNode = edge.getDstNode();
			incrementAllEdgeNodeWeightsFrom(dstNode, nodeWeights);
		}
		// System.out.println(nodeWeights);
		Map<T, Integer> sortedMap = TypeUtil.sortMapByValues(nodeWeights, true);
		arrangedNodes.addAll(sortedMap.keySet());
		//
		return arrangedNodes;
	}

	// -------------------------------------
	/**
	 * 获取解析后的节点（按【删除】依赖顺序）
	 * 
	 * @author koqiui
	 * @date 2016年1月21日 下午9:15:23
	 * 
	 * @param dstNode
	 * @return
	 */
	public List<T> getArrangedEdgeNodesDependingOn(T dstNode) {
		if (!this.isAcyclic()) {
			throw new RuntimeException("解析节点出错，存在循环路径！");
		}
		if (dstNode == null) {
			throw new RuntimeException("给定的节点不能为 null ！");
		}
		List<T> arrangedNodes = new ArrayList<T>();
		//
		Map<T, Integer> nodeWeights = new HashMap<T, Integer>();
		Set<DirectedEdge<T>> tmpEdges = getAllEdgesDependingOn(dstNode);
		Set<T> tmpNodes = new HashSet<T>();
		for (DirectedEdge<T> edge : tmpEdges) {
			tmpNodes.add(edge.getSrcNode());
			tmpNodes.add(edge.getDstNode());
		}
		for (T node : tmpNodes) {
			nodeWeights.put(node, 0);
		}
		for (DirectedEdge<T> edge : tmpEdges) {
			T dstNodeX = edge.getDstNode();
			incrementAllEdgeNodeWeightsTo(dstNodeX, nodeWeights);
		}
		// System.out.println(nodeWeights);
		Map<T, Integer> sortedMap = TypeUtil.sortMapByValues(nodeWeights, true);
		arrangedNodes.addAll(sortedMap.keySet());
		//
		return arrangedNodes;
	}

}
