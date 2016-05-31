package cn.remex.wechat.beans.messages;


import java.util.LinkedHashMap;

/**
 * 微信模板信息
 * Created by guoqi on 2016/2/29.
 */
public class TemplateMessage {

    private String touser; //微信 openId

    private String template_id; //模板id 在公众号获取

    private String url; //消息链接

    private String topcolor; //顶部颜色
    /**
     * 消息内容：   key: 替换模板中的内容
     *  "data":{
             "first": {
             "value":"恭喜你购买成功！",
             "color":"#173177"
             },
             "keynote1":{
             "value":"巧克力",
             "color":"#173177"
             },
             "keynote2": {
             "value":"39.8元",
             "color":"#173177"
             },
             "keynote3": {
             "value":"2014年9月22日",
             "color":"#173177"
             },
             "remark":{
             "value":"欢迎再次购买！",
             "color":"#173177"
             }
        }
     */
    private LinkedHashMap<String, TemplateMsg> data;

    public String getTouser() {
        return touser;
    }



    public void setTouser(String touser) {
        this.touser = touser;
    }



    public String getTemplate_id() {
        return template_id;
    }



    public void setTemplate_id(String template_id) {
        this.template_id = template_id;
    }



    public String getUrl() {
        return url;
    }



    public void setUrl(String url) {
        this.url = url;
    }



    public String getTopcolor() {
        return topcolor;
    }



    public void setTopcolor(String topcolor) {
        this.topcolor = topcolor;
    }


    public LinkedHashMap<String, TemplateMsg> getData() {
        return data;
    }

    public void setData(LinkedHashMap<String, TemplateMsg> data) {
        this.data = data;
    }
}
