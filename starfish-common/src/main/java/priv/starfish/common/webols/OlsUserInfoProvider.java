package priv.starfish.common.webols;

import javax.servlet.http.HttpServletRequest;

public interface OlsUserInfoProvider {
	OlsCustomer getCustomer(String customerId, HttpServletRequest request);

	OlsServant getServant(String servantId, HttpServletRequest request);
}
