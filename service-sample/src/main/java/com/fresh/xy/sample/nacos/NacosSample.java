package com.fresh.xy.sample.nacos;

public class NacosSample {

    /**
     * 使用Nacos开发时隔离方案
     *
     * 第一: 修改spring.application.name
     *  1.修改spring.application.name的值
     *  2.增加修改后名称的配置文件
     *  eg:
     *   spring:
     *     application:
     *       name: service-sample  ##service-sample.yaml
     *   spring:
     *     application:
     *       name: service-sample-ymz  ##service-sample-ymz.yaml
     *  3.每个开发者注册的服务名称都是不同的
     *   1).本地调试时，直接请求本地机器节点
     *   2).本地启动网关，除非修改路由，否则找不到自定义名称的服务
     *   3).公用网关，除非修改路由，否则找不到自定义名称的服务
     *
     * 第二: 通过namespace
     *  1.自定义namespace,在nacos控制台定义namespace,同时将配置clone到自定义namespace
     *  2.指定使用哪一个namespace
     *   spring:
     *     cloud:
     *       nacos:
     *         discovery:
     *           namespace: 579e1018-46bd-40e2-8802-98f69d98f24e
     *         config:
     *           namespace: 579e1018-46bd-40e2-8802-98f69d98f24e
     * 3.每个开发者使用自己的namespace
     *  1).每一个namespace有自己的一套config，同一服务名称注册时隶属于自己的namespace
     *  2).本地调试，直接请求本地机器节点
     *  3).公共网关，不额外配置namespace(默认使用public)，则其只会从public中加载配置，并发现服务
     *  4).本地启动网关，可以指定namespace，则其将会从自定义namespace加载配置，并发现服务
     *
     */




}
