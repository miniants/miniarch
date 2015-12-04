package zbh.wx.bs.old;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

import zbh.wx.appbean.WechatPara;
import zbh.wx.utils.WxUtil;
import zbh.wx.utils.menu.AccessToken;
import zbh.wx.xmlbeans.ResBaseMessage;
import zbh.remex.bs.BsCvo;
import cn.remex.db.Container;
import cn.remex.db.ContainerFactory;

public class WxUploadImgBs {
	private BsCvo bsCvo;
	public WxUploadImgBs(final BsCvo bsCvo) {
		this.bsCvo = bsCvo;
	}
	Container session = ContainerFactory.getSession();
public String controller() throws MalformedURLException, IOException{
	 // 第三方用户唯一凭证  
    String appId = "wx16f616bf5fa97471";  
    // 第三方用户唯一凭证密钥  
    String appSecret = "1a3e97a49bd883f4bced4a73aeb460a3";  
    // 调用接口获取access_token  
    AccessToken at = WxUtil.getAccessToken(appId, appSecret);  
	
	
	long createTime = new Date().getTime()/1000;
//	WechatPara wechatPara = (WechatPara)cvo.getBean();
	WechatPara wechatPara = bsCvo.getBody(WechatPara.class);
	String mediaId = wechatPara.getMediaId();
	String toUserName = wechatPara.getToUserName();
	String fromUserName = wechatPara.getFromUserName();
	ResBaseMessage rsbm = new ResBaseMessage();
	rsbm.setToUserName(fromUserName);
	rsbm.setFromUserName(toUserName);
	rsbm.setCreateTime(createTime);
	String responseBody = null;
	String url = "http://file.api.weixin.qq.com/cgi-bin/media/get?access_token="+at.getToken()+"&media_id="+mediaId;
	HttpURLConnection httpConnection = (HttpURLConnection) new URL(url).openConnection();
	httpConnection.setRequestMethod("GET");
	httpConnection.setDoOutput(true);
	httpConnection.setDoInput(true);
	httpConnection.setAllowUserInteraction(true);
	httpConnection.setConnectTimeout(1000);
	httpConnection.connect();
	byte[] bs = new byte[1024];
	int len;
	File sf = new File("g:\\images\\"+fromUserName);
	if(!sf.exists()){
		sf.mkdirs();
	}
	InputStream is = httpConnection.getInputStream();
	OutputStream os = new FileOutputStream(sf.getPath()+"\\"+mediaId+".jpg");
	
	while((len = is.read(bs)) != -1){
		os.write(bs,0,len);
	}
	os.close();
	is.close();
	
	
	
	
	
	return responseBody;
	}

}


