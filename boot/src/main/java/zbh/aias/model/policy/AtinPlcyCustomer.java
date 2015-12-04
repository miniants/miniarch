package zbh.aias.model.policy;

/**
 * 保单客户关系信息
 * @author guoshaopeng
 * @since 20130712
 */
public class AtinPlcyCustomer extends PlcyCustomer{
	private static final long serialVersionUID = 6251154233432385623L;	
	private AtinPolicy atinPolicy;
	public AtinPolicy getAtinPolicy() {
		return atinPolicy;
	}
	public void setAtinPolicy(AtinPolicy atinPolicy) {
		this.atinPolicy = atinPolicy;
	}
}
