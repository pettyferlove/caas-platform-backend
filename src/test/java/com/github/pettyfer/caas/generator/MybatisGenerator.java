package com.github.pettyfer.caas.generator;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

/**
 * Mybatis相关资源生成器
 *
 * @author Petty
 */
public class MybatisGenerator {
    public static void main(String[] args) {
        String outputDir = "D:/Generator";
        AutoGenerator mpg = new AutoGenerator();
        mpg.setTemplateEngine(new FreemarkerTemplateEngine());
        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        gc.setSwagger2(true);
        gc.setOutputDir(outputDir);
        gc.setFileOverride(true);
        gc.setActiveRecord(true);
        gc.setIdType(IdType.ASSIGN_ID);
        gc.setDateType(DateType.TIME_PACK);
        // XML 二级缓存
        gc.setEnableCache(false);
        // XML ResultMap
        gc.setBaseResultMap(true);
        // XML columList
        gc.setBaseColumnList(true);
        gc.setAuthor("Petty");
        mpg.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setDbType(DbType.MYSQL);
        dsc.setUrl("jdbc:mysql://127.0.0.1:3306/caas?characterEncoding=utf8&zeroDateTimeBehavior=CONVERT_TO_NULL&useSSL=false&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true");
        dsc.setDriverName("com.mysql.cj.jdbc.Driver");
        dsc.setUsername("root");
        dsc.setPassword("root");
        mpg.setDataSource(dsc);


        TemplateConfig tc = new TemplateConfig();
        tc.setController("/templates/controller.java");
        tc.setEntity("/templates/entity.java");
        tc.setService("/templates/service.java");
        tc.setServiceImpl("/templates/serviceImpl.java");
        mpg.setTemplate(tc);

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setSuperEntityClass("com.github.pettyfer.caas.global.entity.BaseEntity");
        strategy.setSuperEntityColumns("id", "del_flag", "creator", "create_time", "modifier", "modify_time");
        // 乐观锁字段
        //strategy.setVersionFieldName("VERSION");
        strategy.setLogicDeleteFieldName("del_flag");
        // strategy.setCapitalMode(true);// 全局大写命名 ORACLE 注意
        // 表名生成策略
        strategy.setInclude("biz_config");
        strategy.setEntityLombokModel(true);
        strategy.setRestControllerStyle(true);
        strategy.setEntityBuilderModel(false);
        strategy.setNaming(NamingStrategy.underline_to_camel);
        //strategy.setTablePrefix("system_");
        mpg.setStrategy(strategy);
        // 配置包路径
        PackageConfig pc = new PackageConfig();
        pc.setParent("com.github.pettyfer.caas.framework.biz");
        mpg.setPackageInfo(pc);
        mpg.execute();
    }
}

