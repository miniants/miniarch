package cn.remex.wechat.wechatservice.bean;

/**
 * Created by guoqi on 2016/3/23.
 */

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlRootElement(name = "xml")
public class WeChatPayNotifyOuter implements Serializable{
    private static final long serialVersionUID = -2230440914739821654L;
    private String return_code;
    private String return_msg;

    public String getReturn_msg() {
        return return_msg;
    }

    public void setReturn_msg(String return_msg) {
        this.return_msg = return_msg;
    }

    public String getReturn_code() {
        return return_code;
    }

    public void setReturn_code(String return_code) {
        this.return_code = return_code;
    }
}
