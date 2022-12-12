package org.knowledge4retail.api.device.graphql.queryresolver;

import graphql.kickstart.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knowledge4retail.api.device.filter.DeviceStatusFilter;
import org.knowledge4retail.api.device.model.DeviceStatus;
import org.knowledge4retail.api.device.repository.DeviceStatusRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.knowledge4retail.api.shared.util.SearchUtil.getSpecification;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeviceStatusQueryResolver implements GraphQLQueryResolver {

    private final DeviceStatusRepository deviceStatusRepository;

    public List<DeviceStatus> getDeviceStatus (DeviceStatusFilter filter, DataFetchingEnvironment environment) throws IllegalAccessException {

        if (filter == null) {

            log.info("Get all deviceStatus with the desired attributes");
            return deviceStatusRepository.findAll();
        }

        log.info("Get all deviceStatus with the desired attributes and filtered with: {}", filter);
        Specification<DeviceStatus> spec = getSpecification(filter, null);
        return deviceStatusRepository.findAll(spec);
    }
}
