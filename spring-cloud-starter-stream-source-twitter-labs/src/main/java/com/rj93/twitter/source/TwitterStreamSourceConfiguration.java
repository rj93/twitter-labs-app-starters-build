package com.rj93.twitter.source;

import com.rj93.twitter.core.service.TwitterLabsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.core.MessageProducer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@RequiredArgsConstructor
@EnableBinding(Source.class)
@Slf4j
public class TwitterStreamSourceConfiguration {

    private final Source source;

    @Bean
    public MessageProducer twitterStream(TwitterLabsService twitterLabsService) {
        TwitterStreamMessageProducer messageProducer = new TwitterStreamMessageProducer(twitterLabsService);
        messageProducer.setOutputChannel(source.output());
        return messageProducer;
    }

    @Bean
    public WebSecurityConfigurerAdapter webSecurityConfigurerAdapter() {
        return new WebSecurityConfigurerAdapter() {
            @Override
            public void configure(HttpSecurity http) throws Exception {
                http
                        .authorizeRequests()
                        .antMatchers("/actuator/**")
                        .permitAll()
                        .and()
                        .csrf().disable();
            }
        };
    }

}
