package com.flower.sea.authservice.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.flower.sea.authservice.auth.service.IApiService;
import com.flower.sea.authservice.auth.service.IAppService;
import com.flower.sea.authservice.auth.service.IMenuService;
import com.flower.sea.authservice.pojo.bo.JwtTokenBO;
import com.flower.sea.authservice.pojo.dto.out.JwtTokenOut;
import com.flower.sea.authservice.service.IAuthorityService;
import com.flower.sea.authservice.utils.JwtUtils;
import com.flower.sea.commonservice.exception.DbOperationException;
import com.flower.sea.commonservice.recurrence.ResponseObject;
import com.flower.sea.commonservice.utils.IdUtils;
import com.flower.sea.entityservice.auth.Api;
import com.flower.sea.entityservice.auth.App;
import com.flower.sea.entityservice.auth.Menu;
import com.flower.sea.commonservice.core.AuthorityApi;
import com.flower.sea.commonservice.core.AuthorityApp;
import com.flower.sea.commonservice.core.AuthorityMenu;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 鉴权实现类
 *
 * @author zhangLei
 * @serial 2019/11/6
 */
@Service
@Slf4j
public class AuthorityServiceImpl implements IAuthorityService {

    private final IAppService appService;
    private final IMenuService menuService;
    private final IApiService apiService;

    @Autowired
    public AuthorityServiceImpl(IAppService appService, IMenuService menuService, IApiService apiService) {
        this.appService = appService;
        this.menuService = menuService;
        this.apiService = apiService;
    }

    @Override
    public ResponseObject generateUserToken(Long invalidTime, Long userId) {
        return ResponseObject.success(JwtUtils.generateToken(userId, invalidTime));
    }

    @Override
    public ResponseObject analysisUserToken(String userToken) {
        ResponseObject<JwtTokenBO> validTokenResponse = JwtUtils.analysisToken(userToken);
        if (HttpStatus.OK.value() != validTokenResponse.getCode()) {
            return validTokenResponse;
        }
        JwtTokenBO jwtToken = validTokenResponse.getData();
        return ResponseObject.success(JwtTokenOut.builder().userId(jwtToken.getUid())
                .crateTime(LocalDateTime.ofEpochSecond(jwtToken.getSta() / 1000, 0, ZoneOffset.ofHours(8)))
                .expireTime(LocalDateTime.ofEpochSecond(jwtToken.getExp() / 1000, 0, ZoneOffset.ofHours(8)))
                .build());
    }

    @Override
    public Integer uploadAuthApi(AuthorityApp authorityApp) {
        Integer count = 0;
        if (null != authorityApp) {
            String appName = authorityApp.getAppName();
            App appValid = appService.selectOne(new EntityWrapper<App>().eq("app_name", appName));
            Long appId;
            if (null != appValid) {
                appId = appValid.getId();
                appValid.setExplain(authorityApp.getAppExplain());
                appValid.setUpdateTime(LocalDateTime.now());
                try {
                    appService.updateById(appValid);
                } catch (Exception e) {
                    throw new DbOperationException("更新APP表数据失败" + e.getMessage());
                }
            } else {
                appId = IdUtils.generateId();
                App app = new App();
                BeanUtils.copyProperties(authorityApp, app);
                app.setExplain(authorityApp.getAppExplain());
                app.setId(appId);
                try {
                    appService.insert(app);
                } catch (Exception e) {
                    throw new DbOperationException("新增APP表数据失败" + e.getMessage());
                }
            }

            List<Menu> menuList = menuService.selectList(new EntityWrapper<Menu>().eq("app_id", appId));
            if (CollUtil.isNotEmpty(menuList)) {
                List<Long> delMenuIdList = new ArrayList<>();
                List<Long> delApiList = new ArrayList<>();
                for (Menu menu : menuList) {
                    List<Api> apiList = apiService.selectList(new EntityWrapper<Api>().eq("menu_id", menu.getId()));
                    if (CollUtil.isNotEmpty(apiList)) {
                        delApiList.addAll(apiList.stream().map(Api::getId).collect(Collectors.toList()));
                    }
                    delMenuIdList.add(menu.getId());
                }
                if (CollUtil.isNotEmpty(delMenuIdList)) {
                    try {
                        menuService.deleteBatchIds(delMenuIdList);
                    } catch (Exception e) {
                        throw new DbOperationException("删除Menu表数据失败" + e.getMessage());
                    }
                }
                if (CollUtil.isNotEmpty(delApiList)) {
                    try {
                        apiService.deleteBatchIds(delApiList);
                    } catch (Exception e) {
                        throw new DbOperationException("删除API表数据失败" + e.getMessage());
                    }
                }
            }

            List<AuthorityMenu> authorityMenuList = authorityApp.getAuthorityMenu();
            if (CollUtil.isNotEmpty(authorityMenuList)) {
                List<Menu> menuInsertList = new ArrayList<>();
                List<Api> apisInsertList = new ArrayList<>();
                for (AuthorityMenu authorityMenu : authorityMenuList) {
                    Long menuId = IdUtils.generateId();
                    Menu menu = new Menu();
                    BeanUtils.copyProperties(authorityMenu, menu);
                    menu.setId(menuId);
                    menu.setAppId(appId);
                    menuInsertList.add(menu);
                    List<AuthorityApi> authorityApiList = authorityMenu.getAuthorityApi();
                    if (CollUtil.isNotEmpty(authorityApiList)) {
                        for (AuthorityApi authorityApi : authorityApiList) {
                            Api api = new Api();
                            BeanUtils.copyProperties(authorityApi, api);
                            api.setId(IdUtils.generateId());
                            api.setMenuId(menuId);
                            apisInsertList.add(api);
                            count++;
                        }
                    }
                }
                if (CollUtil.isNotEmpty(menuInsertList)) {
                    try {
                        menuService.insertBatch(menuInsertList);
                    } catch (Exception e) {
                        throw new DbOperationException("插入Menu表数据失败" + e.getMessage());
                    }
                }
                if (CollUtil.isNotEmpty(apisInsertList)) {
                    try {
                        apiService.insertBatch(apisInsertList);
                    } catch (Exception e) {
                        throw new DbOperationException("插入API表数据失败" + e.getMessage());
                    }
                }
            }
        }
        return count;
    }
}
