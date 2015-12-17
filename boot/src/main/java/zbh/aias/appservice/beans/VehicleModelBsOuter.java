package zbh.aias.appservice.beans;

import java.io.Serializable;

/**
 * @auther GQY
 * @Date 2015/12/5.
 * 车型查询出参
 */
public class VehicleModelBsOuter implements Serializable {
    private static final long serialVersionUID = 2526222779458279039L;
    private String rBCode; //车型代码
    private String modelName; // 车型名字

    public String getrBCode() {
        return rBCode;
    }

    public void setrBCode(String rBCode) {
        this.rBCode = rBCode;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }
}
