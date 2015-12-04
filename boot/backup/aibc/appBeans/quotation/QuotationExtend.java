package zbh.aibc.appBeans.quotation;

import zbh.aibc.appBeans.AncarExtend;
import zbh.aibc.appBeans.AncarExtend;

public class QuotationExtend extends AncarExtend {
	private static final long serialVersionUID = 8677509263447375843L;
	public QuotationExtend(boolean status, String msg) {
		super(status, msg);
	}
	public QuotationExtend(boolean status, String msg, String errorCode, String errorMsg, Object userData) {
		super(status, msg, errorCode, errorMsg, userData);
	}
	
}
