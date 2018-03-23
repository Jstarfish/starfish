package priv.starfish.common.token;

public interface TokenSessionEventListener {
	void onSessionCreated(TokenSession tokenSession);

	void onSessionUpdated(TokenSession tokenSession);

	void onSessionExpired(TokenSession tokenSession);

	void onSessionRemoved(TokenSession tokenSession);
}
