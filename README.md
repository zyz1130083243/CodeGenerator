# 基于Freemarker模板引擎的代码生成器

### 一、前言
&emsp;&emsp;在Java日常开发中，为了快速开发，减少大量crud的时间，我们通常会使用一些主流方案去优化工作，例如
JPA、mybatis plus和模板生成代码等方法。这些方法各有优略，本文不做探讨。


### 二、Freemarker简介
&emsp;&emsp;FreeMarker是一款模板引擎，即一种基于模板和要改变的数据，并用来生成输出文本(HTML网页，电子邮件，配置文件，源代码等)的通用工具。 
它不是面向最终用户的，而是一个Java类库，是一款程序员可以嵌入他们所开发产品的组件。

官方文档：http://freemarker.foofun.cn/index.html

### 三、快速入门
#### 1. 引入依赖
```pom
<dependency>
    <groupId>org.freemarker</groupId>
    <artifactId>freemarker</artifactId>
    <version>2.3.31</version>
</dependency>
```
#### 2. 创建Configuration实例

&emsp;&emsp;实际开发中，应当注意创建的configuration应当是**单例**的。
```
// 创建Configuration对象，注意这里的VERSION_2_3_31对应maven依赖的版本号2.3.31。
Configuration cfg = new Configuration(Configuration.VERSION_2_3_31);
// 设置模板文件存放路径
cfg.setDirectoryForTemplateLoading(new File("/templates"));
// 指定字符集
cfg.setDefaultEncoding("UTF-8");
// 生成模板异常处理器
cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
cfg.setCacheStorage(NullCacheStorage.INSTANCE);

```

#### 3. 创建数据模型

&emsp;&emsp;即将我们要用到的数据封装成Map集合或者java对象，以供后面的模板填充使用。

#### 4. 获取模板

&emsp;&emsp;由于我们在配置configuration对象时，指定了模板的存放路径，所以现在我们可以直接从实例中根据模板名称获取模板对象。 
注意：再次获取同一模板时，返回的其实是第一次解析后缓存里的模板。
```
Template temp = cfg.getTemplate("mapper.ftl");
```

#### 5. 调用模板对象的process方法生成文件
&emsp;&emsp;
模板对象的process方法包含两个参数，一个是数据模型data，一个是Writer流。我们需要把要输出到的文件对象放到Writer流中。

```
Writer out = new OutputStreamWriter(new File("/genCode"));
temp.process(data, out);
```

### 四、自制模板
模板语言ftl通常有这几部分：
* 文本：原样输出
* 插值：${data}
* FTL标签：有点类似html标签
* 注释：<#-- 里面的内容不会输出 -->

#### 常用模板语法
```
字符串操作：
    字符串输出：${str}
    字符串判空：${str!}
    字符串判空后填充默认值：${str!defalutValue}
    字符串连接：${str1 + str2}
    字符串长度：${str?length}
    字符串全大写：${str?upper_case}
    字符串全小写：${str?lower_case}
    字符串首字母大写：${str?cap_first}
    字符串首字母小写：${str?uncap_first}
    字符串拆分：${pics?split(",")}
    字符串替换：${(str)?replace('o','xx')}
    字符串截取：${key?substring(3)}
    字符串截取：${str?substring(3,6)}
    字符串截取：${key?substring(0,key?index_of('_'))}
    字符串常指定字符的位置：${(str)?index_of('w')}
    字符串常最后一个指定字符的位置：${(str)?last_index_of('o')}

if判断：
    <#if str == "aaa">
        
    </#if>

遍历数组list：
    <#list arrary! as item>
        ${item}
    </#list>

遍历map集合：
    <#list map.keySet() as key>
        ${key} : ${map.get(key)}
    </#list>
```
