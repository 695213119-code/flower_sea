package com.flower.sea.authservice.auth.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.flower.sea.authservice.auth.mapper.MenuMapper;
import com.flower.sea.authservice.auth.service.IMenuService;
import com.flower.sea.entityservice.auth.Menu;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 菜单表 服务实现类
 * </p>
 *
 * @author zhangLei
 * @since 2019-11-11
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements IMenuService {

}
