package com.carbon;

/*
 * Copyright 2001-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.TemplateType;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import lombok.SneakyThrows;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.introspector.BeanAccess;
import java.io.File;
import java.io.FileReader;
import java.util.Collections;
import java.util.Optional;

/**
 * Goal which touches a timestamp file.
 *
 * @author kaki
 * @goal touch
 * @phase process-sources
 */

@Mojo(name = "code-generator", defaultPhase = LifecyclePhase.COMPILE)
public class CarbonCodeGenerator extends AbstractMojo {


    @Parameter(defaultValue = "${project.basedir}/src/main/resources/carbon-code-generator-config.yml", readonly = true)
    private File basedir;

    @SneakyThrows
    @Override
    public void execute() {

        if (basedir==null){
            throw new RuntimeException("不存在对应的文件carbon-code-generator-config.yml");
        }

        Yaml yaml = new Yaml();
        yaml.setBeanAccess(BeanAccess.FIELD);
        MybatisGeneratorConfig mybatisGeneratorConfig = yaml.loadAs(new FileReader(basedir), MybatisGeneratorConfig.class);

        MybatisGeneratorConfig.DatabaseGenerator databaseGenerator = mybatisGeneratorConfig.getDatabaseGenerator();

        FastAutoGenerator.create(databaseGenerator.getUrl(), databaseGenerator.getUserName(), databaseGenerator.getPassword())
                .globalConfig(builder -> {
                    MybatisGeneratorConfig.PackageConfig packageConfig = Optional.ofNullable(databaseGenerator.getPackageConfig())
                            .orElse(new MybatisGeneratorConfig.PackageConfig());
                    builder.author(databaseGenerator.getAuthor())
                            .outputDir(Optional.ofNullable(packageConfig.getJavaTargetProject()).orElseThrow(() -> new RuntimeException("请设置对应java文件存放地址")));
                })
                .packageConfig(builder -> {
                    MybatisGeneratorConfig.PackageConfig packageConfig = Optional.ofNullable(databaseGenerator.getPackageConfig())
                            .orElse(new MybatisGeneratorConfig.PackageConfig());
                    builder.parent(Optional.ofNullable(packageConfig.getParent()).orElse("com.carbon"))
                            .entity(Optional.ofNullable(packageConfig.getEntity()).orElse("entity"))
                            .mapper(Optional.ofNullable(packageConfig.getMapper()).orElse("mapper"))
                            .pathInfo(Collections.singletonMap(OutputFile.xml,
                                    Optional.ofNullable(packageConfig.getXmlTargetProject()).orElseThrow(() -> new RuntimeException("请设置对应xml的保存地址"))));
                }).strategyConfig(builder -> {
                    builder.addInclude(Optional.ofNullable(databaseGenerator.getTables()).orElseThrow(() -> new RuntimeException("请设置需要生成的表名")));
                    builder.entityBuilder()
                            .enableLombok()
                            .enableTableFieldAnnotation()
                            .enableRemoveIsPrefix()
                            .enableActiveRecord()
                            .fileOverride();
                    builder.mapperBuilder().enableBaseColumnList()
                            .enableBaseResultMap()
                            .enableMapperAnnotation()
                            .fileOverride();
                })
                .templateEngine(new FreemarkerTemplateEngine())
                .templateConfig(builder -> builder.disable(TemplateType.CONTROLLER, TemplateType.SERVICE, TemplateType.SERVICEIMPL))
                .execute();
    }

}
