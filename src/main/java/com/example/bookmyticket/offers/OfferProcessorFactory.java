package com.example.bookmyticket.offers;

import com.example.bookmyticket.model.OfferType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class OfferProcessorFactory {

    private final List<OfferProcessor> offerProcessorList;
    private final Map<OfferType, OfferProcessor> offerProcessorMap = new HashMap<>();

    @PostConstruct
    public void init() {
        offerProcessorList.forEach(bean -> offerProcessorMap.put(bean.getOfferType(), bean));
    }

    public OfferProcessor getOfferProcessor(OfferType offerType) {
        return offerProcessorMap.get(offerType);
    }
}
