package cn.jbone.cms.portal.service;

import cn.jbone.cms.api.CategoryApi;
import cn.jbone.cms.api.SettingsApi;
import cn.jbone.cms.common.dataobject.CategoryDO;
import cn.jbone.cms.common.dataobject.CategoryRequestDO;
import cn.jbone.cms.common.dataobject.SettingsDO;
import cn.jbone.cms.common.enums.BooleanEnum;
import cn.jbone.cms.common.enums.StatusEnum;
import cn.jbone.common.rpc.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CommonService {

    @Autowired
    private SettingsApi settingsApi;

    @Autowired
    private CategoryApi categoryApi;

    public void setCommonProperties(ModelMap modelMap){
        setSettings(modelMap);
        setMenus(modelMap);
    }

    private void setMenus(ModelMap modelMap){
        CategoryRequestDO categoryRequestDO = new CategoryRequestDO();
        categoryRequestDO.setInMenu(BooleanEnum.TRUE);
        categoryRequestDO.setStatus(StatusEnum.PUBLISH);
        Result<List<CategoryDO>> result = categoryApi.requestCategorysTree(categoryRequestDO);
        if(result.isSuccess()){
            modelMap.addAttribute("menus",result.getData());
        }
    }

    private void setSettings(ModelMap modelMap){
        Result<Map<String, SettingsDO>> settingMap =  settingsApi.getMap();
        if(settingMap.isSuccess()){
            if(!CollectionUtils.isEmpty(settingMap.getData())){
                Map<String,String> settings = new HashMap<>();
                for (Map.Entry<String,SettingsDO> entry : settingMap.getData().entrySet()) {
                    settings.put(entry.getKey(),entry.getValue().getSettingValue());
                }
                modelMap.addAttribute("settings",settings);
            }
        }
    }

}
