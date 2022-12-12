package org.knowledge4retail.api.device.graphql.queryresolver;

import graphql.kickstart.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knowledge4retail.api.device.filter.DeviceFilter;
import org.knowledge4retail.api.device.model.Device;
import org.knowledge4retail.api.device.repository.DeviceRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.knowledge4retail.api.shared.util.SearchUtil.getSpecification;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeviceQueryResolver implements GraphQLQueryResolver {

    private final DeviceRepository deviceRepository;

    public List<Device> getDevices (DeviceFilter filter, DataFetchingEnvironment environment) throws IllegalAccessException {

        if (filter == null) {

            log.info("Get all devices with the desired attributes");
            return deviceRepository.findAll();
        }

        log.info("Get all devices with the desired attributes and filtered with: {}", filter);
        Specification<Device> spec = getSpecification(filter, null);
        return deviceRepository.findAll(spec);
    }
}
