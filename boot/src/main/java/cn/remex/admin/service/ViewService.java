package cn.remex.admin.service;

import cn.remex.db.ContainerFactory;
import cn.remex.db.model.view.SysView;
import cn.remex.web.service.BusinessService;

/**
 * Created by LIU on 16/1/5.
 */
@BusinessService
public class ViewService {
    @BusinessService
    public SysView view(String viewName) {
//        ContainerFactory.createDbCvo(SysView.class)
//                //.withModel(SysView::getBeanName)
//                .withList();


        return null;//new BsRvo(true,"OK","CODE01",resetDb?"重构数据库成功":"清理数据库缓存成功","text_layout","text");
    }
}
