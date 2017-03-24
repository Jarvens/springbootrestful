package com.kunlun.config;

import com.google.common.collect.Lists;
import org.n3r.diamond.client.Miner;
import org.n3r.diamond.client.Minerable;
import org.n3r.diamond.sdk.DiamondSDK;
import org.n3r.diamond.sdk.domain.DiamondConf;
import org.n3r.diamond.sdk.domain.DiamondSDKConf;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

/**
 * Created by kunlun on 2017/3/24.
 */
@Configurable
@EnableWebMvc
public class WebMvcConfig extends WebMvcConfigurerAdapter {

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
}
