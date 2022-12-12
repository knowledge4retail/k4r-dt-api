package org.knowledge4retail.api.store.converter;

import org.knowledge4retail.api.store.dto.StoreAggregateDto;
import org.knowledge4retail.api.store.dto.StoreDto;
import org.knowledge4retail.api.store.model.Store;
import org.knowledge4retail.api.store.model.StoreAggregate;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface StoreConverter {

    StoreConverter INSTANCE = Mappers.getMapper(StoreConverter.class);

    StoreDto storeToDto(Store store);
    Store dtoToStore(StoreDto dto);
    List<StoreDto> storesToDtos(List<Store> stores);
    List<StoreAggregateDto> storeAggregatesToDtos(List<StoreAggregate> aggregations);

    @Mapping(target="id", source="store.id")
    @Mapping(target="storeName", source="store.storeName")
    @Mapping(target="storeNumber", source="store.storeNumber")
    @Mapping(target="addressCountry", source="store.addressCountry")
    @Mapping(target="addressState", source="store.addressState")
    @Mapping(target="addressCity", source="store.addressCity")
    @Mapping(target="addressPostcode", source="store.addressPostcode")
    @Mapping(target="addressStreet", source="store.addressStreet")
    @Mapping(target="addressStreetNumber", source="store.addressStreetNumber")
    @Mapping(target="addressAdditional", source="store.addressAdditional")
    @Mapping(target="longitude", source="store.longitude")
    @Mapping(target="latitude", source="store.latitude")
    @Mapping(target="cadPlanId", source="store.cadPlanId")
    @Mapping(target="externalReferenceId", source="store.externalReferenceId")
    @Mapping(target="shelfCount", source="shelfCount")
    @Mapping(target="shelfLayerCount", source="shelfLayerCount")
    @Mapping(target="barcodeCount", source="barcodeCount")
    @Mapping(target="productCount", source="productCount")
    StoreAggregateDto storeAggregateToDto(StoreAggregate aggregation);
}
