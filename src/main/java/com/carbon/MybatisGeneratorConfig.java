package com.carbon;

import lombok.Data;

/**
 * 类<code>MybatisGeneratorConfig</code>说明：
 *
 * @author kaki
 * @email chenjiaqi@mabangerp.com
 * @since 4/8/2023
 */
@Data
public class MybatisGeneratorConfig {


    private DatabaseGenerator databaseGenerator;


    @Data
    static class DatabaseGenerator {

        /**
         * 数据库地址 jdbc:mysql://127.0.0.1:3306/database
         */
        private String url;

        /**
         * 账户名称
         */
        private String userName;

        /**
         * 密码
         */
        private String password;

        private String author = "carbon";

        /**
         * 表名
         */
        private String tables;

        private PackageConfig packageConfig;
    }


    @Data
    static class PackageConfig {

        /**
         * xml 文件所在绝对路径
         */
        private String xmlTargetProject;

        /**
         * java文件所在绝对路径
         */
        private String javaTargetProject;

        /**
         * 父包
         */
        private String parent;

        /**
         * entity所在包
         */
        private String entity;

        /**
         * mapper类所在包
         */
        private String mapper;

    }


}
