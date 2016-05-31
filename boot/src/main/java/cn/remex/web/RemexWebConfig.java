package cn.remex.web;

import cn.remex.core.RemexRefreshable;
import cn.remex.web.service.ServiceFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 名称：
 * 缩写：
 * 用途：
 * Created by yangy on 2016/4/23 0023.
 */
public class RemexWebConfig  implements RemexRefreshable {
    private static String crossDomain = "-";
    private static List<String> bsPackages = new ArrayList<>();
    private static List<Object> listerners = new ArrayList<>();
    static {
        String defaultPackage = "cn.remex";
        bsPackages.add(defaultPackage);
        ServiceFactory.scanPackagesForBs(defaultPackage);
    }

    public void setBsPackages(List<String> curBsPackages) {
        curBsPackages.stream()
                .filter(p -> !bsPackages.contains(p))
                .forEach(p -> {
                    ServiceFactory.scanPackagesForBs(p);
                    bsPackages.add(p);
                });
    }

    public void setListerners(List<Object> curListerners) {
        curListerners.stream()
                .filter(l -> !listerners.contains(l))
                .forEach(l-> listerners.add(l));
    }
    public void setCrossDomain(String crossDomain) {
        RemexWebConfig.crossDomain = crossDomain;
    }
    public static String getCrossDomain() {
        return crossDomain;
    }

    public static List<String> getBsPackages() {
        return bsPackages;
    }

    @Override
    public void refresh() {
        ServiceFactory.getBsMap().clear();
        RemexWebConfig.getBsPackages().stream().forEach(ServiceFactory::scanPackagesForBs);
    }
}
