package cn.remex.wechat.beans.messages;

/**
 * 微信模板具体
 * Created by guoqi on 2016/2/29.
 */
public class TemplateMsg {
    private String value; //值

    private String color; //颜色


    public TemplateMsg(String value, String color) {
        super();
        this.value = value;
        this.color = color;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }


}
