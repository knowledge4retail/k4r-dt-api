package org.knowledge4retail.api.device.graphql.mutation;

import graphql.GraphQLException;
import graphql.kickstart.tools.GraphQLMutationResolver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knowledge4retail.api.device.filter.DeviceFilter;
import org.knowledge4retail.api.device.model.Device;
import org.knowledge4retail.api.device.repository.DeviceRepository;
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
public class DeviceMutation implements GraphQLMutationResolver {

    private final DeviceRepository deviceRepository;

    public Device updateDevice (Device updateDevice, DeviceFilter filter) throws IllegalAccessException{

        log.info("update oder post a single device using graphQL");
        if (filter != null) {

            Specification<Device> spec = getSpecification(filter, null);
            try {

                Device originalDevice = deviceRepository.findOne(spec).orElseThrow(NoSuchElementException::new);
                updateDevice.setId(originalDevice.getId());
                copyNonNullProperties(updateDevice, originalDevice);
                return deviceRepository.save(originalDevice);
            } catch (IncorrectResultSizeDataAccessException | NoSuchElementException e) {

                throw new GraphQLException("Filtering yields more than one result or no results");
            }
        }

        return deviceRepository.save(updateDevice);
    }

    public List<Device> postDevices (List<Device> devices){

        log.info("create a list of devices using graphQL");
        return deviceRepository.saveAll(devices);
    }
}
