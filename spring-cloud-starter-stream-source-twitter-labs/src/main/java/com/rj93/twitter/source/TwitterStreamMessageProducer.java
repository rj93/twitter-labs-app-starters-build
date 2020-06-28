package com.rj93.twitter.source;

import com.rj93.twitter.core.query.StreamParameters;
import com.rj93.twitter.core.service.TwitterLabsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.endpoint.MessageProducerSupport;
import org.springframework.messaging.support.MessageBuilder;
import reactor.core.Disposable;

@Slf4j
@RequiredArgsConstructor
public class TwitterStreamMessageProducer extends MessageProducerSupport {
    private final TwitterLabsService twitterLabsService;
    private Disposable subscriber;

    @Override
    protected void doStart() {
        subscriber = twitterLabsService.sample(new StreamParameters())
                .map(MessageBuilder::withPayload)
                .map(MessageBuilder::build)
                .subscribe(this::sendMessage);
    }

    @Override
    protected void doStop() {
        subscriber.dispose();
    }
}
