package com.sc.sample.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.SqlExplainInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@Configuration
@MapperScan("com.sc.sample.mapper")
public class MybatisPlusConfig {

    /**
     * 分页插件启用
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        // paginationInterceptor.setLimit(你的最大单页限制数量，默认 500 条，小于 0 如 -1 不受限制);
        return paginationInterceptor;
    }

    /**
     * mybatis-plus SQL执行效率插件【生产环境需要关闭，否则影响系统性能】
     */
    @Profile({"dev", "test"})
    @Bean
    public SqlExplainInterceptor performanceInterceptor() {
        return new SqlExplainInterceptor();
    }
}
