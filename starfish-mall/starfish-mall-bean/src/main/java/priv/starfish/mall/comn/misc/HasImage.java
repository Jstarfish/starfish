package priv.starfish.mall.comn.misc;

public interface HasImage extends HasBrowseUrl {
	public String getImageUsage();

	void setImageUsage(String imageUsage);
	
	String getImagePath();

	void setImagePath(String imagePath);
}
