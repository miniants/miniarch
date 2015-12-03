package anbox.aibc.model.quotation;
/**
 * 询价单客户关系
 * @author guoshaopeng
 * @since 20130712
 */
public class AtinQttnCustomer extends QttnCustomer{

	private static final long serialVersionUID = 2823849981319395594L;
	private AtinQuotation atinQuotation;
	public AtinQuotation getAtinQuotation() {
		return atinQuotation;
	}
	public void setAtinQuotation(AtinQuotation atinQuotation) {
		this.atinQuotation = atinQuotation;
	}
}
