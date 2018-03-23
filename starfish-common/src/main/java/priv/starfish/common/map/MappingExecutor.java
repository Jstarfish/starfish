package priv.starfish.common.map;

import priv.starfish.common.util.ElUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public abstract class MappingExecutor {

	public Map<String, Object> handleInParams(List<ParamMapping> ioParamMappings, Object inConext) {
		Map<String, Object> retMap = new HashMap<String, Object>();
		for (ParamMapping paramMapping : ioParamMappings) {
			if (paramMapping.getIoFlag() == 1) {
				Object value = paramMapping.getValue();
				if (paramMapping.getVarFlag() && value != null) {
					value = ElUtil.eval(value.toString(), inConext);
				}
				retMap.put(paramMapping.getName(), value);
			}
		}
		return retMap;
	}

	public Map<String, Object> handleOutParams(List<ParamMapping> ioParamMappings, Object outContext) {
		Map<String, Object> retMap = new HashMap<String, Object>();
		for (ParamMapping paramMapping : ioParamMappings) {
			if (paramMapping.getIoFlag() == 2) {
				Object value = paramMapping.getValue();
				if (paramMapping.getVarFlag() && value != null) {
					value = ElUtil.eval(value.toString(), outContext);
				}
				retMap.put(paramMapping.getName(), value);
			}
		}
		return retMap;
	}

	public abstract Object execute(List<ParamMapping> ioParamMappings, Object inConext, Map<String, Object> extraMap);

}
