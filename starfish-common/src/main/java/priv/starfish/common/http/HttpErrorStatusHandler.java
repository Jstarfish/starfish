package priv.starfish.common.http;

public interface HttpErrorStatusHandler {
	public void onErrorStatus(int statusCode, String reasonPhrase);
}
