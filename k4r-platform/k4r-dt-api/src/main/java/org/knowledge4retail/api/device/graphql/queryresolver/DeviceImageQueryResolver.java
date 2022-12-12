package org.knowledge4retail.api.device.graphql.queryresolver;

import graphql.kickstart.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knowledge4retail.api.device.filter.DeviceImageFilter;
import org.knowledge4retail.api.device.model.DeviceImage;
import org.knowledge4retail.api.device.repository.DeviceImageRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.knowledge4retail.api.shared.util.SearchUtil.getSpecification;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeviceImageQueryResolver implements GraphQLQueryResolver {

    private final DeviceImageRepository deviceImageRepository;

    public List<DeviceImage> getDeviceImages (DeviceImageFilter filter, DataFetchingEnvironment environment) throws IllegalAccessException {

        if (filter == null) {

            log.info("Get all deviceImages with the desired attributes");
            return deviceImageRepository.findAll();
        }

        log.info("Get all deviceImages with the desired attributes and filtered with: {}", filter);
        Specification<DeviceImage> spec = getSpecification(filter, null);
        return deviceImageRepository.findAll(spec);
    }
}
