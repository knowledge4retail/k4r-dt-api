package org.knowledge4retail.api.shared.util;

import org.knowledge4retail.api.shared.filter.FilterField;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.data.jpa.domain.Specification;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

public class SearchUtil {

    public static <T, F> Specification<T> getSpecification(F filter, Specification<T> spec) throws IllegalAccessException {
        Field[] allFields = filter.getClass().getDeclaredFields();
        for (Field field : allFields) {

            field.setAccessible(true);
            if(field.get(filter) != null) {

                FilterField filterField = (FilterField) field.get(filter);
                spec = (spec == null ? filterByAttributes(filterField, field.getName()) :
                        spec.and(filterByAttributes(filterField, field.getName())));
            }
        }
        return spec;
    }

    private static <T> Specification<T> filterByAttributes(FilterField filterField, String filterFieldName) {
        return (root, query, builder) -> filterField.generateCriteria(builder, root.get(filterFieldName));
    }

    public static void copyNonNullProperties(Object src, Object target) {
        BeanUtils.copyProperties(src, target, getNullPropertyNames(src));
    }

    public static String[] getNullPropertyNames (Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<>();
        for(java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) emptyNames.add(pd.getName());
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }
}
