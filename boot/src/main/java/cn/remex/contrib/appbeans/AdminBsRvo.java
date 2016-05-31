package cn.remex.contrib.appbeans;

import cn.remex.db.model.SysMenu;
import cn.remex.db.model.cert.AuthUser;

import java.util.List;


public class AdminBsRvo extends DataRvo {
    private SysMenu sysMenu;
    private List<SysMenu> sysMenus;
    private AuthUser user;

    public AuthUser getUser() {
        return user;
    }

    public void setUser(AuthUser user) {
        this.user = user;
    }

    public SysMenu getSysMenu() {
        return sysMenu;
    }

    public void setSysMenu(SysMenu sysMenu) {
        this.sysMenu = sysMenu;
    }

    public List<SysMenu> getSysMenus() {
        return sysMenus;
    }

    public void setSysMenus(List<SysMenu> sysMenus) {
        this.sysMenus = sysMenus;
    }
}
