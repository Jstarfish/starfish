package priv.starfish.mall.service.xyz;

import java.util.Map;

public interface Crawler<TResult> {
	public static enum CarType {
		Brand, Serial, Model
	}

	public String getSiteDomain();

	public String getSiteBaseUrl();

	public void extract();

	public TResult handleBrandInfo(Map<String, Object> reqParams, String zm);

	public TResult handleSerialInfo(Map<String, Object> reqParams, TResult brandResult);

	public TResult handleModelInfo(Map<String, Object> reqParams, TResult serialResult);

}
