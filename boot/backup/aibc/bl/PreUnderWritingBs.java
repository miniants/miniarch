package zbh.aibc.bl;

import anbox.aibc.AiwbConsts;
import zbh.remex.bs.BsCvo;
import zbh.remex.bs.BsRvo;
import zbh.remex.bs.anno.BsAnnotation;
/**预核保
 * @author zhangaiguo
 * @since 2015-01-08
 */
@BsAnnotation
public class PreUnderWritingBs  implements AiwbConsts{
	@BsAnnotation()
	public BsRvo execute(BsCvo bsCvo, BsRvo bsRvo) {
		return bsRvo;
	}
}