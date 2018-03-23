package priv.starfish.common.token;

public interface TokenSessionStore {
	public static enum TokenSessionEvent{
		Created("创建"),//
		Updated("更新"),//
		Expired("过期"),//
		Removed("删除");
		
		private String text;

		TokenSessionEvent(String text) {
			this.text = text;
		}

		public String getText() {
			return this.text;
		}
	}
	
	void addTokenSessionEventListener(TokenSessionEventListener tokenSessionEventListener);
	void setTokenSessionEventListener(TokenSessionEventListener tokenSessionEventListener);

	//
	void setTokenGenerator(TokenGenerator tokenGenerator);

	void setTokenSessionExpireStrategy(TokenSessionExpireStrategy tokenSessionExpireStrategy);

	TokenSession getTokenSession(Integer userId, Integer appId);

	TokenSession getTokenSession(String token);

	TokenSession createTokenSession(Integer userId, Integer appId);

	boolean touchTokenSession(String token);

	TokenSession removeTokenSession(String token);
	
	void __clear();
}
