package zbh.aibc.bl;

import zbh.remex.bs.BsCvo;
import zbh.remex.bs.BsRvo;
import zbh.remex.bs.anno.BsAnnotation;

/**ancar推广
 * @author zhangaiguo
 * @since 2015-01-08
 */
@BsAnnotation
public class TeamExpandBs {
	@BsAnnotation()
	public BsRvo execute(BsCvo bsCvo, BsRvo bsRvo) {
		return bsRvo;
	}
}