package priv.starfish.common.token;

public interface TokenSessionExpireStrategy {
	boolean isTokenSessionExpired(TokenSession tokenSession);
}
