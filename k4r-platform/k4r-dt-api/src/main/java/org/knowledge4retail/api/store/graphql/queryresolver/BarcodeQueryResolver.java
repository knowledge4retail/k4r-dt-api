package org.knowledge4retail.api.store.graphql.queryresolver;

import graphql.kickstart.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knowledge4retail.api.store.filter.BarcodeFilter;
import org.knowledge4retail.api.store.model.Barcode;
import org.knowledge4retail.api.store.repository.BarcodeRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.knowledge4retail.api.shared.util.SearchUtil.getSpecification;

@Slf4j
@Component
@RequiredArgsConstructor
public class BarcodeQueryResolver implements GraphQLQueryResolver {

    private final BarcodeRepository barcodeRepository;

    public List<Barcode> getBarcodes (BarcodeFilter filter, DataFetchingEnvironment environment) throws IllegalAccessException {

        if (filter == null) {

            log.info("Get all barcodes with the desired attributes");
            return barcodeRepository.findAll();
        }

        log.info("Get all barcodes with the desired attributes and filtered with: {}", filter);
        Specification<Barcode> spec = getSpecification(filter, null);
        return barcodeRepository.findAll(spec);
    }
}
