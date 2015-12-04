package zbh.wx.utils.menu;

import com.opensymphony.xwork2.util.logging.Logger;
import com.opensymphony.xwork2.util.logging.LoggerFactory;

import zbh.wx.utils.WxUtil;


  
/** 
 * 菜单管理器类 
 *  
 * @author 
 * @date 
 */  
public class MenuManager {  
    private static Logger log = LoggerFactory.getLogger(MenuManager.class);  
  
    public static void main(String[] args) {  
        // 第三方用户唯一凭证  
        String appId = "wx16f616bf5fa97471";  
        // 第三方用户唯一凭证密钥  
        String appSecret = "1a3e97a49bd883f4bced4a73aeb460a3";  
  
        // 调用接口获取access_token  
        AccessToken at = WxUtil.getAccessToken(appId, appSecret);  
  
        if (null != at) {  
            // 调用接口创建菜单  
            int result = WxUtil.createMenu(getMenu(), at.getToken());  
  
            // 判断菜单创建结果  
            if (0 == result)  
                log.info("菜单创建成功！");  
            else  
                log.info("菜单创建失败，错误码：" + result);  
        }  
    }  
  
    /** 
     * 组装菜单数据 
     *  
     * @return 
     */  
    private static Menu getMenu() {  
        CommonButton btn11 = new CommonButton();  
        btn11.setType("view");  
        btn11.setName("安卡比价");  
//        btn11.setKey("11");  
        btn11.setUrl("https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx16f616bf5fa97471&redirect_uri=http://www.qqbx.com.zbh/WxSys/wx/page/cf_atin.html&response_type=code&scope=snsapi_base&state=2#wechat_redirect");
        
  
//        CommonButton btn21 = new CommonButton();  
//        btn21.setName("关于安卡");  
//        btn21.setType("view");  
//        //btn21.setKey("21");  
//        btn21.setUrl("http://mp.weixin.qq.com/s?__biz=MzA3MTc0NDIxOA==&mid=202511725&idx=1&sn=a9aa3fdd54aac2d3704644180e1b4352&scene=1&from=groupmessage&isappinstalled=0#rd");
        CommonButton btn21 = new CommonButton();  
        btn21.setName("理赔服务");  
        btn21.setType("view");  
        btn21.setUrl("http://www.qqbx.com.zbh/WxSys/wx/page/yh_claimGuide.html");
        //btn22.setKey("22");  
//        btn22.setUrl("http://www.qqbx.com.cn/wx/wx/page/carinfo.html");
        CommonButton btn22 = new CommonButton();  
        btn22.setName("操作指南");  
        btn22.setType("view");  
       // btn23.setKey("23");  
        btn22.setUrl("http://www.qqbx.com.zbh/WxSys/wx/page/yh_operateGuide.html");
        CommonButton btn23 = new CommonButton();  
        btn23.setName("关于安卡");  
        btn23.setType("view");  
        // btn23.setKey("23");  
        btn23.setUrl("http://www.qqbx.com.zbh/WxSys/wx/page/yh_aboutAncar.html");
        ComplexButton mainBtn3 = new ComplexButton();  
        mainBtn3.setName("安卡服务");  
        mainBtn3.setSub_button(new CommonButton[] { btn21, btn22,btn23}); 
        
      
      CommonButton btn31 = new CommonButton();  
    	 btn31.setType("view");  
    	 btn31.setName("会员中心");  
//      btn11.setKey("11");  
    	 btn31.setUrl("https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx16f616bf5fa97471&redirect_uri=http://www.qqbx.com.zbh/WxSys/wx/page/mm_member.html&response_type=code&scope=snsapi_base&state=2#wechat_redirect");
      
//        btn31.setName("会员中心");  
//        btn31.setType("pic_photo_or_album");  
//        btn31.setType("view");  
//        btn31.setUrl("https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx16f616bf5fa97471&redirect_uri=http://www.qqbx.com.cn/wx/wx/page/mm_member.html&response_type=code&scope=snsapi_userinfo&state=3#wechat_redirect");
//  
//        CommonButton btn32 = new CommonButton();  
//        btn32.setName("订单查询支付");  
//        btn32.setType("click");  
//        btn32.setKey("32");  
//        
//        CommonButton btn33 = new CommonButton();  
//        btn33.setName("售后服务查询");  
//        btn33.setType("click");  
//        btn33.setKey("33");  
//        
//        CommonButton btn34 = new CommonButton();  
//        btn34.setName("保单递送跟踪");  
//        btn34.setType("click");  
//        btn34.setKey("34");  
//        
//        CommonButton btn35 = new CommonButton();  
//        btn35.setName("个人信息");  
//        btn35.setType("click");  
//        btn35.setKey("35");  
//
//        ComplexButton mainBtn3 = new ComplexButton();  
//        mainBtn3.setName("我的车险");  
//        mainBtn3.setSub_button(new CommonButton[] { btn31, btn32, btn33,btn34,btn35});  
//  
        /** 
         * 这是公众号xiaoqrobot目前的菜单结构，每个一级菜单都有二级菜单项<br> 
         *  
         * 在某个一级菜单下没有二级菜单的情况，menu该如何定义呢？<br> 
         * 比如，第三个一级菜单项不是“更多体验”，而直接是“幽默笑话”，那么menu应该这样定义：<br> 
         * menu.setButton(new Button[] { mainBtn1, mainBtn2, btn33 }); 
         */  
        Menu menu = new Menu();  
        menu.setButton(new Button[] { btn11, mainBtn3,btn31 });  
//        menu.setButton(new Button[] { btn11}); 
        return menu;  
    }  
}  
