package com.cml.learning.framework.mybatis;

import com.cml.learning.framework.constant.ModuleConst;
import com.cml.learning.framework.mybatis.marker.ThirdDataSourceMapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Configuration;

/**
 * Created by dudu on 2019/9/30.
 */
@Configuration
@AutoConfigureAfter(MybatisThirdConfig.class)//数据源等配置好了后： 再进行配置mapper
@MapperScan(
        markerInterface = ThirdDataSourceMapper.class,
        basePackages = {
                ModuleConst.Framwwork.MAPPER_SCAN_THIRD_PACKAGE
        },
        sqlSessionFactoryRef = "thirdSqlSessionFactory"
)
public class MybatisThirdScanConfig {
    protected static Log log = LogFactory.getLog(MybatisThirdScanConfig.class);

    public MybatisThirdScanConfig() {
        log.info("*************************MybatisThirdScanConfig第三个数据源及mapper加载配置完成***********************");
    }
}
