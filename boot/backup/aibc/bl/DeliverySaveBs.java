package zbh.aibc.bl;

import anbox.aibc.AiwbConsts;
import anbox.aibc.appBeans.delivery.DeliveryCvo;
import anbox.aibc.appBeans.delivery.DeliveryExtend;
import anbox.aibc.appBeans.delivery.DeliveryRvo;
import zbh.remex.bs.BsCvo;
import zbh.remex.bs.BsRvo;
import zbh.remex.bs.anno.BsAnnotation;

/**递送信息保存
 * @author zhangaiguo
 * @since 2015-01-08
 */
@BsAnnotation
public class DeliverySaveBs  implements AiwbConsts{
	@BsAnnotation(bsCvoBodyClass=DeliveryCvo.class,bsRvoBodyClass=DeliveryRvo.class,
			bsCvoExtendClass=DeliveryExtend.class,bsRvoExtendClass=DeliveryExtend.class)
	public BsRvo execute(BsCvo bsCvo, BsRvo bsRvo) {
		DeliveryCvo dCvo = bsCvo.getBody();
		
		DeliveryRvo deliveryRvo = new DeliveryRvo();
		bsRvo.setBody(deliveryRvo);
		bsRvo.setExtend(new DeliveryExtend(true, "保存地址的服务尚未开发完成！"));
		return bsRvo;
	}
}
