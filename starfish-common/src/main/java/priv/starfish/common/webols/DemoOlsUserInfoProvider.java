package priv.starfish.common.webols;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class DemoOlsUserInfoProvider implements OlsUserInfoProvider {

	private Map<String, OlsCustomer> customers = new HashMap<String, OlsCustomer>();
	private Map<String, OlsServant> servants = new HashMap<String, OlsServant>();

	public DemoOlsUserInfoProvider() {
		//
		OlsCustomer customer;
		customer = new OlsCustomer();
		customer.id = "110";
		customer.name = "老大";
		customer.logoUrl = "http://www.qqzhi.com/uploadpic/2015-01-29/064947280.jpg";
		customers.put(customer.id, customer);

		customer = new OlsCustomer();
		customer.id = "120";
		customer.name = "老小";
		customers.put(customer.id, customer);
		//
		OlsServant servant;
		servant = new OlsServant();
		servant.id = "210";
		servant.name = "小甜甜";
		servant.no = "s01";
		servant.orgName = "盛大旗舰店";
		servants.put(servant.id, servant);

		servant = new OlsServant();
		servant.id = "220";
		servant.name = "奶茶妹";
		servant.no = "s03";
		servant.orgName = "儿童天地店";
		servants.put(servant.id, servant);
	}

	@Override
	public OlsCustomer getCustomer(String customerId, HttpServletRequest request) {
		return customers.get(customerId);
	}

	@Override
	public OlsServant getServant(String servantId, HttpServletRequest request) {
		return servants.get(servantId);
	}

}
