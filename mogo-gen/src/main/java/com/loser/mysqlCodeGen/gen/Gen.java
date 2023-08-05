package com.loser.mysqlCodeGen.gen;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import com.loser.mysqlCodeGen.gen.handler.ReplaceHandler;
import com.loser.mysqlCodeGen.gen.inner.RunTimeParams;
import com.loser.mysqlCodeGen.gen.inner.CodeMapper;
import com.loser.mysqlCodeGen.gen.inner.CodePathConfig;

import java.io.File;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * 通用代码生成器
 *
 * @author loser
 * @date 2023-01-06  12:15
 */
public class Gen {

    /**
     * 生成文件
     *
     * @param handlers      自定义关键期替换器
     * @param runTimeParams 运行时参数
     * @param mapperClass   模板配置类
     */
    public static void genFile(List<ReplaceHandler> handlers, RunTimeParams runTimeParams, Class<? extends MapperConfig> mapperClass) {

        try {

            System.out.println("=============================== gen code start ============================================================");
            if (CollectionUtil.isNotEmpty(handlers)) {
                for (ReplaceHandler handler : handlers) {
                    handler.putData(runTimeParams);
                }
            }
            List<CodePathConfig> configs = buildConfigs(mapperClass, runTimeParams);
            if (configs.size() == 0) {
                throw new RuntimeException("模板配置为空");
            }
            AtomicInteger count = new AtomicInteger(0);
            for (CodePathConfig config : configs) {
                genFile(count, config.getPath(), config.getFileName(), runTimeParams, getTemplateFile(config.getFile()), handlers, config.getSuffix());
            }
            System.out.println();
            System.out.println("============================================================================================================");
            System.out.println();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 构建一个文件路径配置集合
     *
     * @param mapperClass   文件路径配置类
     * @param runTimeParams 运行时参数
     * @return 文件路径配置集合
     */
    private static List<CodePathConfig> buildConfigs(Class mapperClass, RunTimeParams runTimeParams) {

        List<CodePathConfig> configs = new ArrayList<>();
        List<String> list = runTimeParams.getPairs().stream().map(item -> String.format("#<%s>", item.getKey())).collect(Collectors.toList());
        Field[] fields = mapperClass.getDeclaredFields();
        for (Field field : fields) {
            CodeMapper annotation = field.getAnnotation(CodeMapper.class);
            if (Objects.isNull(annotation)) {
                continue;
            }
            String path = annotation.basePath() + annotation.path();
            if (path == null) {
                continue;
            }
            String fileName = annotation.file();
            String suffix = annotation.suffix();
            String file = annotation.tem();
            CodePathConfig config = new CodePathConfig();
            for (String key : list) {
                String keyWord = key.replace("#<", "").replace(">", "");
                if (path.contains(key)) {
                    String tPath = path.replaceAll(key, runTimeParams.getValueByKey(keyWord, false));
                    path = tPath;
                    config.setPath(tPath);
                }
                if (fileName.contains(key)) {
                    String tName = fileName.replaceAll(key, runTimeParams.getValueByKey(keyWord, false));
                    fileName = tName;
                    config.setFileName(tName);
                }
                config.setSuffix(suffix);
                config.setFile(file);
            }
            configs.add(config);
        }

        return configs;

    }

    /**
     * 获取项目的在windows下的绝对根路径
     * mac 暂未测试
     */
    private static String getModelDir() {
        try {
            return new File("").getCanonicalPath();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 根据classPath下的模板生成文件
     */
    private static void genFile(AtomicInteger count, String path, String fileName, RunTimeParams config, File file, List<ReplaceHandler> handlers, String suffix) {

        List<String> writes = new ArrayList<>();
        List<String> list = FileUtil.readLines(file, "UTF-8");
        for (String s : list) {
            List<String> result = replace(s, config, handlers);
            if (CollectionUtil.isNotEmpty(result)) {
                writes.addAll(result);
            }
        }
        File temp = new File(getModelDir() + path + fileName + suffix);
        FileUtil.writeLines(writes, temp, "UTF-8");
        String absolutePath = temp.getAbsolutePath();
        System.out.println(DateUtil.now() + " " + count.incrementAndGet() + " 生成文件成功: " + absolutePath);

    }

    /**
     * 获取类路径下的模板文件
     */
    private static File getTemplateFile(String fileName) {

        ClassLoader classLoader = Gen.class.getClassLoader();
        URL url = classLoader.getResource(fileName);
        assert url != null;
        return new File(url.getFile());

    }

    /**
     * 自定义占位符替换变量
     */
    private static List<String> replace(String line, RunTimeParams config, List<ReplaceHandler> handlers) {

        if (CollectionUtil.isNotEmpty(handlers)) {
            for (ReplaceHandler handler : handlers) {
                if (handler.match(line.trim())) {
                    return handler.work();
                }
            }
        }
        List<String> list = config.getPairs().stream().map(item -> String.format("#<%s>", item.getKey())).collect(Collectors.toList());
        for (String key : list) {
            String replace = key.replace("#<", "").replace(">", "");
            line = line.replaceAll(key, config.getValueByKey(replace, false));
        }
        return Collections.singletonList(line);

    }

}
