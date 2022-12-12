package org.knowledge4retail.api.device.graphql.mutation;

import graphql.GraphQLException;
import graphql.kickstart.tools.GraphQLMutationResolver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knowledge4retail.api.device.filter.DeviceStatusFilter;
import org.knowledge4retail.api.device.model.DeviceStatus;
import org.knowledge4retail.api.device.repository.DeviceStatusRepository;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.NoSuchElementException;

import static org.knowledge4retail.api.shared.util.SearchUtil.copyNonNullProperties;
import static org.knowledge4retail.api.shared.util.SearchUtil.getSpecification;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeviceStatusMutation implements GraphQLMutationResolver {

    private final DeviceStatusRepository deviceStatusRepository;

    public DeviceStatus updateDeviceStatus (DeviceStatus updateDeviceStatus, DeviceStatusFilter filter) throws IllegalAccessException{

        log.info("update oder post a single deviceStatus using graphQL");
        if (filter != null) {

            Specification<DeviceStatus> spec = getSpecification(filter, null);
            try {

                DeviceStatus originalDeviceStatus = deviceStatusRepository.findOne(spec).orElseThrow(NoSuchElementException::new);
                updateDeviceStatus.setDeviceId(originalDeviceStatus.getDeviceId());
                copyNonNullProperties(updateDeviceStatus, originalDeviceStatus);
                return deviceStatusRepository.save(originalDeviceStatus);
            } catch (IncorrectResultSizeDataAccessException | NoSuchElementException e) {

                throw new GraphQLException("Filtering yields more than one result or no results");
            }
        }

        return deviceStatusRepository.save(updateDeviceStatus);
    }

    public List<DeviceStatus> postDeviceStatus (List<DeviceStatus> deviceStatus){

        log.info("create a list of deviceStatus using graphQL");
        return deviceStatusRepository.saveAll(deviceStatus);
    }
}
