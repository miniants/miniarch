package zbh.aibc.bl;

import zbh.aibc.AiwbConsts;
import zbh.aibc.AiwbConsts;
import zbh.remex.bs.BsCvo;
import zbh.remex.bs.BsRvo;
import zbh.remex.bs.anno.BsAnnotation;

/**报价询价单查询
 * @author zhangaiguo
 * @since 2015-01-08
 */
@BsAnnotation
public class QuotationQueryBs implements AiwbConsts {
	@BsAnnotation()
	public BsRvo execute(BsCvo bsCvo, BsRvo bsRvo) {
		return bsRvo;
	}
}
