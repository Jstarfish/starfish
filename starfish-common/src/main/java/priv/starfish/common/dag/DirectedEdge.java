package priv.starfish.common.dag;

/**
 * 有向边
 * 
 * @author koqiui
 *
 * @param <T> 节点类型
 */
public class DirectedEdge<T> {
	private T srcNode;
	private T dstNode;

	private DirectedEdge(T srcNode, T dstNode) {
		this.srcNode = srcNode;
		this.dstNode = dstNode;
	}

	private DirectedEdge() {
		//
	}

	public T getSrcNode() {
		return srcNode;
	}

	public void setSrcNode(T srcNode) {
		this.srcNode = srcNode;
	}

	public T getDstNode() {
		return dstNode;
	}

	public void setDstNode(T dstNode) {
		this.dstNode = dstNode;
	}

	public boolean isValid() {
		return this.srcNode != null && this.dstNode != null && !this.srcNode.equals(this.dstNode);
	}

	public static <T> DirectedEdge<T> newOne(T srcNode, T dstNode) {
		return new DirectedEdge<T>(srcNode, dstNode);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((srcNode == null) ? 0 : srcNode.hashCode());
		result = prime * result + ((dstNode == null) ? 0 : dstNode.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object another) {
		if (this == another)
			return true;
		if (another == null)
			return false;
		if (getClass() != another.getClass())
			return false;
		DirectedEdge<?> other = (DirectedEdge<?>) another;
		if (srcNode == null) {
			if (other.srcNode != null)
				return false;
		} else if (!srcNode.equals(other.srcNode))
			return false;
		if (dstNode == null) {
			if (other.dstNode != null)
				return false;
		} else if (!dstNode.equals(other.dstNode))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "(" + srcNode + " -> " + dstNode + ")";
	}

}
