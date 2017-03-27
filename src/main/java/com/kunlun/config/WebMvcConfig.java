package com.kunlun.config;

import com.google.common.collect.Lists;
import org.n3r.diamond.client.Miner;
import org.n3r.diamond.client.Minerable;
import org.n3r.diamond.sdk.DiamondSDK;
import org.n3r.diamond.sdk.domain.DiamondConf;
import org.n3r.diamond.sdk.domain.DiamondSDKConf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import org.springframework.web.servlet.view.freemarker.FreeMarkerView;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

import java.util.*;

import static java.util.Arrays.asList;

/**
 * Created by kunlun on 2017/3/24.
 */
@Configuration
@EnableWebMvc
public class WebMvcConfig extends WebMvcConfigurerAdapter {

    private Logger logger = LoggerFactory.getLogger(WebMvcConfig.class);

    /***
     * 持久化配置 diamond
     * @return
     */
    @Bean
    public DiamondSDK diamondSDK() {
        Minerable minerable = new Miner().getMiner("hcon.diamond", "diamondConfig");
        DiamondSDK diamondSDK = new DiamondSDK(new DiamondSDKConf(Lists.newArrayList(
                new DiamondConf(minerable.getString("diamondIp"), minerable.getInt("diamondPort"),
                        minerable.getString("diamondUsername"),
                        minerable.getString("diamondPassword")))));

        logger.info("持久化配置服务加载完成 ...>");
        return diamondSDK;
    }

    /**
     * messageConvert
     *
     * @param converters
     */
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        List<MediaType> mediaTypes = new ArrayList(converter.getSupportedMediaTypes());
        mediaTypes.addAll(asList(MediaType.TEXT_PLAIN, MediaType.TEXT_HTML, MediaType.TEXT_XML));
        converter.setSupportedMediaTypes(mediaTypes);
        converters.add(converter);
        super.configureMessageConverters(converters);
    }

    /**
     * Freemarker 视图配置
     * @return
     */
    @Bean
    public FreeMarkerConfigurer freeMarkerConfigurer() {
        FreeMarkerConfigurer freeMarkerConfigurer = new FreeMarkerConfigurer();
        freeMarkerConfigurer.setTemplateLoaderPath("classpath:/views/");
        Properties properties = new Properties();
        properties.setProperty("template_update_delay", "0");
        properties.setProperty("default_encoding", "UTF-8");
        properties.setProperty("number_format", "yyyy-MM-dd HH:mm:ss");
        properties.setProperty("classic_compatible", "true");
        properties.setProperty("template_exception_handler", "ignore");
        freeMarkerConfigurer.setFreemarkerSettings(properties);
        //Map<String,Object> variables = new HashMap<String,Object>();
        return freeMarkerConfigurer;

    }

    /**
     * Freemarker  视图解析
     * @return
     */
    @Bean
    public FreeMarkerViewResolver freeMarkerViewResolver(){
        FreeMarkerViewResolver template = new FreeMarkerViewResolver();
        template.setViewClass(FreeMarkerView.class);
        template.setPrefix("");
        template.setSuffix(".html");
        template.setCache(true);
        template.setContentType("text/html;charset=UTF-8");
        template.setExposeSpringMacroHelpers(true);
        template.setExposeRequestAttributes(true);
        template.setExposeSessionAttributes(true);
        template.setRequestContextAttribute("rc");
        template.setAllowSessionOverride(true);
        return template;
    }


    /**
     * 静态资源处理
     *
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/css/**").addResourceLocations("classpath:/css/");
        registry.addResourceHandler("/img/**").addResourceLocations("classpath:/img/");
        registry.addResourceHandler("/js/**").addResourceLocations("classpath:/js/");
        super.addResourceHandlers(registry);
    }
}
