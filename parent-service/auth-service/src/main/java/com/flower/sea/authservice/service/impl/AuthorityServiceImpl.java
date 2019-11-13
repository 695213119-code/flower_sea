package com.flower.sea.authservice.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.flower.sea.authservice.auth.service.IApiService;
import com.flower.sea.authservice.auth.service.IAppService;
import com.flower.sea.authservice.auth.service.IMenuService;
import com.flower.sea.authservice.mapper.AuthorityMapper;
import com.flower.sea.authservice.pojo.bo.authentication.AuthorityApi;
import com.flower.sea.authservice.pojo.bo.authentication.AuthorityApp;
import com.flower.sea.authservice.pojo.bo.authentication.AuthorityMenu;
import com.flower.sea.authservice.pojo.bo.token.JwtTokenBO;
import com.flower.sea.authservice.pojo.bo.verification.ApiBO;
import com.flower.sea.authservice.pojo.bo.verification.Gateway;
import com.flower.sea.authservice.pojo.dto.out.AuthOut;
import com.flower.sea.authservice.pojo.dto.out.JwtTokenOut;
import com.flower.sea.authservice.service.IAuthorityService;
import com.flower.sea.authservice.utils.JwtUtils;
import com.flower.sea.commonservice.constant.AuthorityConstant;
import com.flower.sea.commonservice.exception.DbOperationException;
import com.flower.sea.commonservice.recurrence.ResponseObject;
import com.flower.sea.commonservice.utils.IdUtils;
import com.flower.sea.entityservice.auth.Api;
import com.flower.sea.entityservice.auth.App;
import com.flower.sea.entityservice.auth.Menu;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

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
    private final AuthorityMapper authorityMapper;

    @Autowired
    public AuthorityServiceImpl(IAppService appService, IMenuService menuService, IApiService apiService, AuthorityMapper authorityMapper) {
        this.appService = appService;
        this.menuService = menuService;
        this.apiService = apiService;
        this.authorityMapper = authorityMapper;
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
            Long appId = createOrUpdateApp(authorityApp);
            deleteOutmoded(appId);
            count = freshData(authorityApp, appId);
        }
        return count;
    }

    @Override
    public ResponseObject verificationIsToken(Gateway gateway, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseObject.failure(HttpStatus.BAD_REQUEST.value(), bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        ApiBO api = authorityMapper.findApi(gateway);
        if (ObjectUtil.isNull(api)) {
            return ResponseObject.failure(HttpStatus.UNAUTHORIZED.value(), "无效的请求");
        }
        AuthOut authOut = new AuthOut();
        BeanUtils.copyProperties(api, authOut);
        return ResponseObject.success(authOut);
    }

    @Override
    public ResponseObject verificationTokenIsCorrect(String userToken) {
        ResponseObject<JwtTokenBO> jwtTokenResponseObject = JwtUtils.analysisToken(userToken);
        if (HttpStatus.OK.value() != jwtTokenResponseObject.getCode()) {
            return ResponseObject.failure(HttpStatus.UNAUTHORIZED.value(), "无效的Token");
        }
        return ResponseObject.success();
    }

    /**
     * 刷新数据
     *
     * @param authorityApp 权限上传的数据
     * @param appId        APP主键id
     */
    private Integer freshData(AuthorityApp authorityApp, Long appId) {
        Integer count = 0;
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
                        api.setStatus(authorityApi.isToken() ? AuthorityConstant.AUTHENTICATION_VERIFICATION_NEED_TOKEN : AuthorityConstant.AUTHENTICATION_VERIFICATION_NOT_NEED_TOKEN);
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
        return count;
    }

    /**
     * 删除app过时的权限的数据
     *
     * @param appId app主键id
     */
    private void deleteOutmoded(Long appId) {
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
    }

    /**
     * 新建或者更新app表数据
     *
     * @param authorityApp 获取到的上传数据
     * @return Long
     */
    private Long createOrUpdateApp(AuthorityApp authorityApp) {
        App appValid = appService.selectOne(new EntityWrapper<App>().eq("app_name", authorityApp.getAppName()));
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
        return appId;
    }
}
