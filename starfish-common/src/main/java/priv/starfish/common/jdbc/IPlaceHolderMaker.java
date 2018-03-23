package priv.starfish.common.jdbc;

public interface IPlaceHolderMaker {
	String makePlaceHolder(String orginalStr);

	public boolean isPlaceHolder(String valueStr);
}
