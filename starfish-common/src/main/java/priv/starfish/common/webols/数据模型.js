var nackModel = {
	eventType : "nack",
	attrs : {
		snId : "14e273e0-fb70-45c9-a794-f6d8928b7ca3",
		reason : "位置原因"
	}
};

var fackModel = {
	eventType : "fack",
	attrs : {
		snId : "14e273e0-fb70-45c9-a794-f6d8928b7ca3",
		reason : "位置原因"
	}
};

var toackModel = {
	type : "toack",
	attrs : {
		snId : "14e273e0-fb70-45c9-a794-f6d8928b7ca3",
		reason : "会话无效或已过期"
	}
};

// 启动会话
var startModel = {
	eventType : "start",
	// C | S | M
	eventSource : "C",
	// C:customerId || S:servantId
	customerId : "110",
	servantId : "220"
};

var startAckModel = {
	source : "C",
	type : "start-ack",
	attrs : {
		snId : "14e273e0-fb70-45c9-a794-f6d8928b7ca3",
		timeout : 10
	}
};

// 结束会话
var endModel = {
	eventType : "end",
	snId : "14e273e0-fb70-45c9-a794-f6d8928b7ca3"
};

var endAckModel = {
	type : "end-ack",
	attrs : {
		snId : "14e273e0-fb70-45c9-a794-f6d8928b7ca3"
	}
};

// 心跳
var heartbeatModel = {
	eventType : "heartbeat",
	snId : "14e273e0-fb70-45c9-a794-f6d8928b7ca3"
};

var heartbeatAckModel = {
	type : "heartbeat-ack",
	attrs : {
		snId : "14e273e0-fb70-45c9-a794-f6d8928b7ca3"
	}
};

// 发言
var speakModel = {
	eventType : "speak",
	// C | S | M
	eventSource : "C",
	customerId : "110",
	servantId : "220",
	snId : "asadfsdgdg",
	content : "你好，请问..."
};

var speakAckModel = {
	type : "speak-ack",
	// C | S | M
	source : "C",
	attrs : {
		snId : "14e273e0-fb70-45c9-a794-f6d8928b7ca3",
		customerId : "110",
		servantId : "220",
		snId : "asadfsdgdg",
		content : "你好，请问...",
		ts : "2014-06-22 13:01"
	}
};

// 接听
var listenModel = {
	eventType : "listen",
	// C | S | M
	eventSource : "C",
	// C:customerId || S:servantId
	customerId : "110",
	servantId : "220",
	snId : "asadfsdgdg"
};

var listenAckModel = {
	type : "listen-ack",
	// C | S | M
	source : "C",
	// C:customerId || S:servantId
	attrs : {
		messages : {
			"210" : [ {
				source : "S",
				customerId : "110",
				servantId : "220",
				content : "各式款式都有",
				ts : "2015-06-22 10:30"
			} ],
			"220" : []
		},
		snId : "asadfsdgdg"
	}
};

// 检查
