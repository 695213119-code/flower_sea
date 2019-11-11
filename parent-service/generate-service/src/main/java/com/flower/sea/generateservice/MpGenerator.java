package com.flower.sea.generateservice;


import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.DbType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 逆向代码生成器
 *
 * @author zhangLei
 * @serial 2019/11/11
 */
public class MpGenerator {

    private final static Logger LOGGER = LoggerFactory.getLogger(MpGenerator.class);


    public static void main(String[] args) {
        createTables();
    }

    /**
     * 代码生成方法
     */
    private static void createTables() {
        MpGenerator.createCode(
                MpGenerator.getGlobalConfig(),
                MpGenerator.getDataSourceConfig(
                        "49.232.143.52",
                        "33306",
                        "flower_sea_auth_db",
                        "root",
                        "123456"
                ),
                MpGenerator.getStrategyConfig(
                        //忽略表前缀
                        new String[]{"flower_sea_"},
                        null,
                        //设置需要生成代码的表
                        new String[]{"flower_sea_menu","flower_sea_app"}
                ),
                MpGenerator.getPackageConfig(
                        "com.flower.sea.authservice",
                        "auth"
                ),
                MpGenerator.getTemplateConfig(),
                MpGenerator.getInjectionConfig(null, null)
        );
    }


    /**
     * 默认数据库驱动
     */
    private static final DbType DEFAULT_DB_TYPE = DbType.MYSQL;
    private static final String DEFAULT_DRIVER_NAME = "com.mysql.jdbc.Driver";


    /**
     * 自动生成代码的方法
     *
     * @param globalConfig     全局策略配置
     * @param dataSourceConfig 数据库源配置，通过该配置，指定需要生成代码的具体数据库
     * @param strategyConfig   数据库表配置，通过该配置，可指定需要生成哪些表或者排除哪些表
     * @param packageConfig    包名配置，通过该配置，指定生成代码的包路径
     * @param templateConfig   模板配置，可自定义代码生成的模板，实现个性化操作
     * @param injectionConfig  自定义配置，通过该配置，可注入自定义参数等操作以实现个性化操作
     */
    public static void createCode(GlobalConfig globalConfig,
                                  DataSourceConfig dataSourceConfig,
                                  StrategyConfig strategyConfig,
                                  PackageConfig packageConfig,
                                  TemplateConfig templateConfig,
                                  InjectionConfig injectionConfig
    ) {
        //代码自动生成器对象
        AutoGenerator mpg = new AutoGenerator();
        mpg.setGlobalConfig(globalConfig);
        mpg.setDataSource(dataSourceConfig);
        mpg.setStrategy(strategyConfig);
        mpg.setPackageInfo(packageConfig);
        mpg.setTemplate(templateConfig);
        mpg.setCfg(injectionConfig);
        // 执行生成
        mpg.execute();
    }


    //region 生成全局策略配置对象

    /**
     * 生成全局策略配置对象
     *
     * @param idtype    主键ID生成模式
     * @param outputDir 自动生成的代码输出目录
     * @param author    开发人员信息备注
     * @return 全局策略配置对象
     */
    public static GlobalConfig getGlobalConfig(IdType idtype,
                                               String outputDir,
                                               String author) {
        GlobalConfig gc = new GlobalConfig();

        //region 设置是否使用xml二级缓存配置
        gc.setEnableCache(false);
        //endregion

        //region 设置开发人员,会被添加在生成的类的注释中
        gc.setAuthor(author);
        //endregion

        //region 设置主键ID生成方式
        gc.setIdType(idtype);
        //endregion

        //region 开启活动记录模式,默认true
        gc.setActiveRecord(true);
        //endregion

        //region  是否开启Base_Column_List
        //Base_Column_List是一个另外定义的数组,在sql作用相当于 * ，
        //Base_Column_List是固定的几个字段，而用*号的话会降低查询效率，因为后期数据库的字段会不断增加。
        gc.setBaseColumnList(true);
        //endregion

        //region 是否开启BaseResultMap
        //BaseResultMap里使用column和property对应。
        //column对应pojo字段，property对应数据库字段。
        //可避免pojo中的字段与数据库字段名称不一致取不到值.
        gc.setBaseResultMap(true);
        //endregion

        //region 设置自动生成的代码保存路径
        gc.setOutputDir(outputDir);
        LOGGER.info("自动生成代码输出目录:" + outputDir);
        //输出完毕后是否打开目录
        gc.setOpen(true);
        //endregion

        //region 是否覆盖已有文件
        gc.setFileOverride(true);
        //endregion

        //region 设置生成的controller类名后缀
        gc.setControllerName("%sAPIController");
        //endregion

        //region 设置生成的mapper类名后缀
        gc.setMapperName("%sMapper");
        //endregion

        //region 设置生成的XML文件名后缀
        gc.setXmlName("%sMapper");
        //endregion

        //region 设置生成的Service接口名后缀
        gc.setServiceName("I%sService");
        //endregion

        //region 设置生成的Service实现类名后缀
        gc.setServiceImplName("%sServiceImpl");
        //endregion
        return gc;
    }

    /**
     * 生成全局策略配置对象
     *
     * @param idtype    主键ID生成模式
     * @param outputDir 自动生成的代码输出目录
     * @return 全局策略配置对象
     */
    public static GlobalConfig getGlobalConfig(IdType idtype,
                                               String outputDir) {
        return getGlobalConfig(idtype, outputDir, System.getProperty("user.name"));
    }

    /**
     * 生成全局策略配置对象
     *
     * @param idtype 主键ID生成模式
     * @return 全局策略配置对象
     */
    public static GlobalConfig getGlobalConfig(IdType idtype) {
        String outputDir = System.getProperty("user.dir") + "/src/main/java";
        return getGlobalConfig(idtype, outputDir, System.getProperty("user.name"));
    }

    /**
     * 生成全局策略配置对象
     *
     * @param outputDir 自动生成的代码输出目录
     * @return 全局策略配置对象
     */
    private static GlobalConfig getGlobalConfig(String outputDir) {
        return getGlobalConfig(IdType.ID_WORKER, outputDir, System.getProperty("user.name"));
    }

    /**
     * 生成全局策略配置对象,不配置导出路径时自动导入到当前Module下
     *
     * @return 全局策略配置对象
     */
    private static GlobalConfig getGlobalConfig() {
        //TODO 默认代码输出位置在f盘根目录下
        String outputDir = "F:";
        return getGlobalConfig(IdType.ID_WORKER, outputDir, "zhangLei");
    }

    private static String getModuleRootPath() {
        String path = MpGenerator.class.getClassLoader().getResource("").getPath();
        return new File(path).getParentFile().getParentFile().getAbsolutePath();
    }


    //endregion

    //region 生成数据库源配置对象,通过该配置，指定需要生成代码的具体数据库

    /**
     * 生成数据库源配置对象
     *
     * @param dbType      数据库类型,默认MySQL
     * @param driverName  驱动名称,默认MySQL
     * @param typeConvert 字段转换映射
     * @param url         数据库连接Url
     * @param username    数据库账号
     * @param password    数据库密码
     * @return 数据库源配置对象
     */
    public static DataSourceConfig getDataSourceConfig(DbType dbType,
                                                       String driverName,
                                                       ITypeConvert typeConvert,
                                                       String url,
                                                       String username,
                                                       String password) {
        DataSourceConfig dc = new DataSourceConfig();
        dc.setDbType(dbType);
        dc.setDriverName(driverName);
        dc.setTypeConvert(typeConvert);
        dc.setUrl(url);
        dc.setUsername(username);
        dc.setPassword(password);
        return dc;
    }

    /**
     * 生成数据库源配置对象
     *
     * @param dbType     数据库类型,默认MySQL
     * @param driverName 驱动名称,默认MySQL
     * @param url        数据库连接Url
     * @param username   数据库账号
     * @param password   数据库密码
     * @return 数据库源配置对象
     */
    public static DataSourceConfig getDataSourceConfig(DbType dbType,
                                                       String driverName,
                                                       String url,
                                                       String username,
                                                       String password) {
        return getDataSourceConfig(dbType, driverName, null, url, username, password);
    }

    /**
     * 生成数据库源配置对象
     *
     * @param url      数据库连接Url
     * @param username 数据库账号
     * @param password 数据库密码
     * @return 数据库源配置对象
     */
    public static DataSourceConfig getDataSourceConfig(String url,
                                                       String username,
                                                       String password) {
        return getDataSourceConfig(DEFAULT_DB_TYPE, DEFAULT_DRIVER_NAME, null, url, username, password);
    }

    /**
     * 生成数据库源配置对象
     *
     * @param host         数据库域名
     * @param port         数据库端口
     * @param databaseName 数据库名称
     * @param username     数据库账号
     * @param password     数据库密码
     * @return 数据库源配置对象
     */
    public static DataSourceConfig getDataSourceConfig(String host,
                                                       String port,
                                                       String databaseName,
                                                       String username,
                                                       String password) {
        return getDataSourceConfig(DEFAULT_DB_TYPE, DEFAULT_DRIVER_NAME,
                null, "jdbc:mysql://" + host + ":" + port + "/" + databaseName + "?characterEncoding=utf8&serverTimezone=GMT%2B8", username, password);
    }

    //endregion

    //region 生成数据库表配置对象,通过该配置，可指定需要生成哪些表或者排除哪些表

    /**
     * 生成数据库表配置对象
     *
     * @param tablePrefix                   表前缀,生成类时,将不把前缀带入到类名中
     * @param fieldPrefix                   字段前缀,生成字段时,将不把前缀带入到字段名中
     * @param superEntityClassFullName      默认Entity对象继承的父类全名(含包名)
     * @param superMapperClassFullName      默认Mapper对象继承的父类全名(含包名)
     * @param superServiceClassFullName     默认Service对象继承的父类全名(含包名)
     * @param superServiceImplClassFullName 默认ServiceImpl对象继承的父类全名(含包名)
     * @param superControllerClassFullName  默认Controller对象继承的父类全名(含包名)
     * @param includeTables                 生成代码时要包含的表名数组,和要忽略的数组二选一配置
     * @param excludeTables                 生成代码时要忽略的表名数组,和要包含的数组二选一配置
     * @param versionFieldName              乐观锁字段名称
     * @param logicDeleteFieldName          逻辑删除字段名称
     * @return 数据库表配置对象
     */
    public static StrategyConfig getStrategyConfig(String[] tablePrefix,
                                                   String[] fieldPrefix,
                                                   String superEntityClassFullName,
                                                   String superMapperClassFullName,
                                                   String superServiceClassFullName,
                                                   String superServiceImplClassFullName,
                                                   String superControllerClassFullName,
                                                   String[] includeTables,
                                                   String[] excludeTables,
                                                   String versionFieldName,
                                                   String logicDeleteFieldName) {
        StrategyConfig sc = new StrategyConfig();
        //region 表名、字段名是否使用下划线命名
        sc.setDbColumnUnderline(false);
        //endregion
        //region 是否大写命名
        sc.setCapitalMode(false);
        //endregion
        //region 是否跳过视图
        sc.setSkipView(false);
        //endregion
        //region 数据库表映射到实体类的命名策略
        sc.setNaming(NamingStrategy.underline_to_camel);
        //endregion

        //region 数据库表字段映射到实体类属性的命名策略
        sc.setColumnNaming(NamingStrategy.underline_to_camel);
        //endregion

        //region 表前缀,将不会把前缀带到类名中
        if (tablePrefix != null) {
            sc.setTablePrefix(tablePrefix);
        }
        //endregion
        //region 字段前缀,将不会把前缀带到属性命名中.
        if (fieldPrefix != null) {
            sc.setFieldPrefix(fieldPrefix);
        }
        //endregion
        //region 自定义继承的Entity类全程,带包名
        if (superEntityClassFullName != null) {
            sc.setSuperEntityClass(superEntityClassFullName);
        }
        //endregion

        //region 自定义继承的Mapper类全称,带包名
        if (superMapperClassFullName != null) {
            sc.setSuperMapperClass(superMapperClassFullName);
        }
        //endregion

        //region 自定义继承的Service类全称,带包名
        if (superServiceClassFullName != null) {
            sc.setSuperServiceClass(superServiceClassFullName);
        }
        //endregion
        //region 自定义继承的ServiceImpl类全称,带包名
        if (superServiceImplClassFullName != null) {
            sc.setSuperServiceImplClass(superServiceImplClassFullName);
        }
        //endregion
        //region 自定义继承的Controller类全称,带包名
        if (superControllerClassFullName != null) {
            sc.setSuperControllerClass(superControllerClassFullName);
        }
        //endregion

        //region 需要包含的表名,与需要排除的表名二选一配置
        if (includeTables != null) {
            sc.setInclude(includeTables);
        }
        //endregion

        //region 需要排除的表名,与需要包含的表名二选一配置
        if (excludeTables != null) {
            sc.setExclude(excludeTables);
        }
        //endregion

        //region 【实体】是否生成字段常量
        sc.setEntityColumnConstant(false);
        //endregion

        //region 【实体】是否为构建者模型
        sc.setEntityBuilderModel(false);
        //endregion

        //region 【实体】是否为lombok模型
        sc.setEntityLombokModel(true);
        //endregion

        //region Boolean类型字段是否移除is前缀
        sc.setEntityBooleanColumnRemoveIsPrefix(false);
        //endregion

        //region 生成 @RestController 控制器
        sc.setRestControllerStyle(true);
        //endregion
        //region 驼峰转连字符
        sc.setControllerMappingHyphenStyle(false);
        //endregion
        //region 是否生成实体时，生成字段注解
        sc.entityTableFieldAnnotationEnable(false);
        //endregion

        //region 乐观锁属性名称
        if (versionFieldName != null) {
            sc.setVersionFieldName(versionFieldName);
        }
        //endregion
        //region 逻辑删除属性名称
        if (logicDeleteFieldName != null) {
            sc.setLogicDeleteFieldName(logicDeleteFieldName);
        }
        //endregion
        return sc;
    }

    /**
     * 生成数据库表配置对象
     *
     * @param tablePrefix   表前缀,生成类时,将不把前缀带入到类名中
     * @param fieldPrefix   字段前缀,生成字段时,将不把前缀带入到字段名中
     * @param includeTables 生成代码时要包含的表名数组,和要忽略的数组二选一配置
     * @param excludeTables 生成代码时要忽略的表名数组,和要包含的数组二选一配置
     * @return 数据库表配置对象
     */
    public static StrategyConfig getStrategyConfig(String[] tablePrefix,
                                                   String[] fieldPrefix,
                                                   String[] includeTables,
                                                   String[] excludeTables) {
        return getStrategyConfig(tablePrefix, fieldPrefix,
                null,
                null,
                null,
                null,
                null,
                includeTables, excludeTables,
                null,
                null);
    }

    /**
     * 生成数据库表配置对象
     *
     * @param tablePrefix   表前缀,生成类时,将不把前缀带入到类名中
     * @param fieldPrefix   字段前缀,生成字段时,将不把前缀带入到字段名中
     * @param includeTables 生成代码时要包含的表名数组,和要忽略的数组二选一配置
     * @return 数据库表配置对象
     */
    public static StrategyConfig getStrategyConfig(String[] tablePrefix,
                                                   String[] fieldPrefix,
                                                   String[] includeTables) {
        return getStrategyConfig(tablePrefix, fieldPrefix,
                null,
                null,
                null,
                null,
                null,
                includeTables, null,
                null,
                null);
    }

    //endregion

    //region 生成包配置，通过该配置，指定生成代码的包路径

    /**
     * 生成包配置
     *
     * @param parentPackageName 父类包名
     * @param parentModuleName  父类模块名
     * @return 包配置对象
     */
    public static PackageConfig getPackageConfig(String parentPackageName,
                                                 String parentModuleName) {
        PackageConfig pc = new PackageConfig();
        pc.setParent(parentPackageName);
        pc.setModuleName(parentModuleName);
        pc.setEntity("entity");
        pc.setService("service");
        pc.setServiceImpl("service.impl");
        pc.setMapper("mapper");
        pc.setXml("mapping.xml");
        pc.setController("api");
        return pc;
    }
    //endregion

    //region 生成模板配置，可自定义代码生成的模板，实现个性化操作

    /**
     * 生成模板配置
     *
     * @return 模板配置对象
     */
    public static TemplateConfig getTemplateConfig() {
        TemplateConfig tc = new TemplateConfig();
//        tc.setEntity(null);
        tc.setMapper("/templates/mapper.java.vm");
        tc.setXml("/templates/mapper.xml.vm");
        tc.setService("/templates/service.java.vm");
        tc.setServiceImpl("/templates/serviceImpl.java.vm");
        tc.setController("/templates/controller.java.vm");
        return tc;
    }
    //endregion

    //region 生成自定义配置，通过该配置，可注入自定义参数等操作以实现个性化操作

    /**
     * 生成自定义配置
     *
     * @param map     自定义返回配置 Map 对象
     * @param focList 自定义输出文件配置
     * @return 自定义配置对象
     */
    public static InjectionConfig getInjectionConfig(Map<String, Object> map, List<FileOutConfig> focList) {
        // 注入自定义配置，可以在 VM 中使用 cfg.abc 【可无】
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                if (map != null) {
                    this.setMap(map);
                }
            }
        };
        if (focList != null) {
            cfg.setFileOutConfigList(focList);
        } else {
            focList = new ArrayList<FileOutConfig>();
            // 调整 xml 生成目录
            focList.add(new FileOutConfig("/templates/mapper.xml.vm") {
                @Override
                public String outputFile(TableInfo tableInfo) {
                    return getModuleRootPath() + "/src/main/resources/mapping/" +
                            tableInfo.getEntityName() + "Mapper.xml";
                }
            });
            cfg.setFileOutConfigList(focList);
        }
        return cfg;
    }
    //endregion
}
