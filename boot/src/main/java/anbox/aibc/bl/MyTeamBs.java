package anbox.aibc.bl;

import anbox.aibc.AiwbConsts;
import cn.remex.bs.BsCvo;
import cn.remex.bs.BsRvo;
import cn.remex.bs.anno.BsAnnotation;

/**我的团队
 * @author zhangaiguo
 * @since 2015-01-08
 */
@BsAnnotation
public class MyTeamBs  implements AiwbConsts{
	
	@BsAnnotation()
	public BsRvo execute(BsCvo bsCvo,BsRvo bsRvo){
		
		return bsRvo;
	}

}
