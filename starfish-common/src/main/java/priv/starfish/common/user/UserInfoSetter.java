package priv.starfish.common.user;

public interface UserInfoSetter {
	void prepare();

	void setUserInfo(UserContext userContext);
}
