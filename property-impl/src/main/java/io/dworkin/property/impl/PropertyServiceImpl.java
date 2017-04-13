package io.dworkin.property.impl;

import akka.NotUsed;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import io.dworkin.property.api.Property;
import io.dworkin.property.api.PropertyService;
import io.dworkin.property.api.PropertyValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.Optional;

/**
 * Created by yakov on 13.04.2017.
 */
public class PropertyServiceImpl implements PropertyService {

    private final PropertyRepository propertyRepository;

    private final Logger log = LoggerFactory.getLogger(PropertyServiceImpl.class);

    @Inject
    public PropertyServiceImpl(PropertyRepository propertyRepository) {
        this.propertyRepository = propertyRepository;
    }

    @Override
    public ServiceCall<NotUsed, Optional<PropertyValue>> getPropertyValueByName(String name) {
        return notUsed -> {
            log.info("Get property value by name method was invoked with {}", name);

            return propertyRepository.getPropertyValueByName(name);
        };
    }

    @Override
    public ServiceCall<NotUsed, Optional<Property>> getByName(String name) {
        return notUsed -> {
            log.info("Get property by name method was invoked with {}", name);

            return propertyRepository.getByName(name);
        };
    }
}
