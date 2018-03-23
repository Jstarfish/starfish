package priv.starfish.common.map;

public class ParamMapping {
	private String name;
	private Boolean varFlag = false;
	private Object value;
	// 1 - 请求， 2- 响应
	private Integer ioFlag = 1;
	private String desc;

	public static ParamMapping newOne() {
		return new ParamMapping();
	}

	public String getName() {
		return name;
	}

	public ParamMapping setName(String name) {
		this.name = name;
		//
		return this;
	}

	public Boolean getVarFlag() {
		return varFlag;
	}

	public ParamMapping setVarFlag(Boolean varFlag) {
		this.varFlag = varFlag;
		//
		return this;
	}

	public Object getValue() {
		return value;
	}

	public ParamMapping setValue(Object value) {
		this.value = value;
		//
		return this;
	}

	public Integer getIoFlag() {
		return ioFlag;
	}

	public ParamMapping setIoFlag(Integer ioFlag) {
		this.ioFlag = ioFlag;
		//
		return this;
	}

	public String getDesc() {
		return desc;
	}

	public ParamMapping setDesc(String desc) {
		this.desc = desc;
		//
		return this;
	}

	@Override
	public String toString() {
		return "ParamMapping [name=" + name + ", varFlag=" + varFlag + ", value=" + value + ", ioFlag=" + ioFlag + ", desc=" + desc + "]";
	}

}
