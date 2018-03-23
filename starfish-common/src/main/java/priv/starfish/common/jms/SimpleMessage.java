package priv.starfish.common.jms;

import java.io.Serializable;

public class SimpleMessage implements Serializable {
	private static final long serialVersionUID = -4856866532916218579L;
	//
	public static final int Unknown = 0;
	//
	public static final int Created = 1;
	public static final int Updated = 2;
	public static final int Deleted = 4;
	//
	public static final int Queue = 1;
	public static final int Topic = 2;

	//
	public static SimpleMessage newOne() {
		return new SimpleMessage();
	}

	// 消息来源（比如发送的应用节点id）
	public String source;
	// 消息类型（Queue =1, Topic=2）
	public int type = Unknown;
	// 消息类别
	public String category;
	// 消息焦点
	public String subject;
	// 消息key（比如实体的主键）
	public Serializable key;
	// 变更类型（比如实体变更类型）
	public Integer change = Unknown;
	// 消息数据（比如具体的实体信息）
	public Serializable data;
	// 额外信息
	public Serializable extra;
	// 消息生成时间
	public long timestamp = System.currentTimeMillis();

	@Override
	public String toString() {
		return "SimpleMessage [source=" + source + ", type=" + type + ", category=" + category + ", subject=" + subject + ", key=" + key + ", change=" + change + ", data=" + data + ", extra=" + extra + ", timestamp=" + timestamp + "]";
	}
}
