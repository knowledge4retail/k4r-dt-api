package org.knowledge4retail.api.shared.filter;

import graphql.GraphQLException;
import lombok.Data;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;

import static java.lang.Boolean.parseBoolean;

@Data
public class FilterField {

    private String operator;
    private String value;
    private String type;

    @SuppressWarnings("unchecked")
    public <T> Predicate generateCriteria(CriteriaBuilder builder, Path<T> field) {

        return switch (type.toLowerCase()) {
            case "float" -> handleFloatFilter(builder, (Path<Number>) field);
            case "int" -> handleIntFilter(builder, (Path<Number>) field);
            case "boolean" -> handleBooleanFilter(builder, (Path<Number>) field);
            case "string" -> handleStringFilter(builder, (Path<String>) field);
            default -> throw new GraphQLException("DataType not found");
        };
    }

    private Predicate handleFloatFilter(CriteriaBuilder builder, Path<Number> field) {

        float v = Float.parseFloat(value);
        return switch (operator) {
            case "lt" -> builder.lt(field, v);
            case "le" -> builder.le(field, v);
            case "gt" -> builder.gt(field, v);
            case "ge" -> builder.ge(field, v);
            case "eq" -> builder.equal(field, v);
            case "ne" -> builder.notEqual(field, v);
            default -> throw new GraphQLException("Float operator not found");
        };
    }

    private Predicate handleIntFilter(CriteriaBuilder builder, Path<Number> field) {

        int v = Integer.parseInt(value);
        return switch (operator) {
            case "lt" -> builder.lt(field, v);
            case "le" -> builder.le(field, v);
            case "gt" -> builder.gt(field, v);
            case "ge" -> builder.ge(field, v);
            case "eq" -> builder.equal(field, v);
            case "ne" -> builder.notEqual(field, v);
            default -> throw new GraphQLException("Int operator not found");
        };
    }

    private Predicate handleBooleanFilter(CriteriaBuilder builder, Path<Number> field) {

        if (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false")) {

            return builder.equal(field, parseBoolean(value));
        } else {

            throw new GraphQLException("Value is not a boolean");
        }
    }

    private Predicate handleStringFilter(CriteriaBuilder builder, Path<String> field) {

        return switch (operator) {
            case "endsWith" -> builder.like(field, "%" + value);
            case "startsWith" -> builder.like(field, value + "%");
            case "contains" -> builder.like(field, "%" + value + "%");
            case "eq" -> builder.equal(field, value);
            case "ne" -> builder.notEqual(field, value);
            default -> throw new GraphQLException("String operator not found");
        };
    }
}