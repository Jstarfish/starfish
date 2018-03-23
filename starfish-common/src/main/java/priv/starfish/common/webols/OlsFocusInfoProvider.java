package priv.starfish.common.webols;

import javax.servlet.http.HttpServletRequest;

public interface OlsFocusInfoProvider {
	Object getFocusInfo(String focusCode, String focusId, HttpServletRequest request);
}
