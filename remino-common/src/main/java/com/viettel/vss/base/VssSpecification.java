package com.viettel.vss.base;

import com.viettel.vss.constant.CommonConstants;
import com.viettel.vss.util.DataUtil;
import com.viettel.vss.util.DateUtil;
import com.viettel.vss.util.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.CollectionUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * author truongbx7
 */
public class VssSpecification<T extends BaseEntity> implements Specification<T> {
    private final List<Condition> conditionList;

    public VssSpecification() {
        this.conditionList = new ArrayList<>();
    }

    public void add(Condition condition) {
        conditionList.add(condition);
    }

    public void add(List<Condition> conditions) {
        Condition conditionDefault = new Condition();
        conditionDefault.setProperty("isDeleted");
        conditionDefault.setPropertyType("integer");
        conditionDefault.setOperator("EQUAL");
        conditionDefault.setValue(0);
        conditions.add(conditionDefault);
        conditionList.addAll(conditions);
    }

    public void addNoDefault(List<Condition> conditions) {
        conditionList.addAll(conditions);
    }


    /**
     * @param root
     * @param query
     * @param criteriaBuilder
     * @return
     */
    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {

        //create a new predicate list
        List<Predicate> predicates = new ArrayList<>();

        //convert list condition
        if (!CollectionUtils.isEmpty(conditionList)) {
            List<Condition> conditionListRemove = conditionList.stream().filter(x -> "LIKE".equalsIgnoreCase(x.getOperator()) &&
                    StringUtils.isNullOrEmpty(x.getValue().toString())).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(conditionListRemove)) {
                conditionList.removeAll(conditionListRemove);
            }
        }
        //add criteria to predicates
        if (!CollectionUtils.isEmpty(conditionList)) {
            for (Condition criteria : conditionList) {
                predicates.add(buildQueryCriterion(criteria, root, criteriaBuilder));
            }
        }
        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }


    public Predicate buildQueryCriterion(Condition condition, Root<T> root, CriteriaBuilder criteriaBuilder) {
        if (CommonConstants.SQL_LOGIC.OR.equalsIgnoreCase(condition.getExpType()) || CommonConstants.SQL_LOGIC.AND.equalsIgnoreCase(condition.getExpType())) {
            return buildOrCriterion(condition.getLstCondition(), root, condition.getExpType(), criteriaBuilder);
        } else {
            return buildPredicate(root, criteriaBuilder, condition);
        }
    }

    public Predicate buildOrCriterion(List<Condition> lstCondition, Root<T> root, String expType, CriteriaBuilder criteriaBuilder) {
        List<Predicate> lstPredicate = new ArrayList<>();
        for (Condition con : lstCondition) {
            lstPredicate.add(buildQueryCriterion(con, root, criteriaBuilder));
        }
        Predicate[] predicatesArray = lstPredicate.toArray(Predicate[]::new);
        if (CommonConstants.SQL_LOGIC.OR.equalsIgnoreCase(expType)) {
            return criteriaBuilder.or(predicatesArray);
        } else {
            return criteriaBuilder.and(predicatesArray);
        }
    }

    public Predicate buildPredicate(Root<T> root, CriteriaBuilder criteriaBuilder, Condition criteria) {
        switch (criteria.getOperator()) {
            case "GREATER":
                return buildPredicateGreater(root, criteriaBuilder, criteria);
            case "LOWER":
                return buildPredicateLower(root, criteriaBuilder, criteria);
            case "GREATER_EQUAL":
                return buildPredicateGreaterEqual(root, criteriaBuilder, criteria);
            case "LOWER_EQUAL":
                return buildPredicateLowerEqual(root, criteriaBuilder, criteria);
            case "NOT_EQUAL":
                return criteriaBuilder.notEqual(
                        root.get(criteria.getProperty()), criteria.getValue());
            case "EQUAL":
                return buildPredicateEqual(root, criteriaBuilder, criteria);
            case "LIKE":
                return criteriaBuilder.like(
                        criteriaBuilder.lower(root.get(criteria.getProperty())),
                        "%" + criteria.getValue().toString().toLowerCase() + "%");
            case "IN":
                return criteriaBuilder.in(root.get(criteria.getProperty())).value(getValue(criteria.getPropertyType(), criteria.getOperator(), criteria.getValue()));
            case "NOT_IN":
                return criteriaBuilder.not(root.get(criteria.getProperty())).in(getValue(criteria.getPropertyType(), criteria.getOperator(), criteria.getValue()));
            default:
                break;
        }
        return null;
    }

    public Object getValue(String propertyType, String operator, Object value) {
        if (CommonConstants.SQL_OPERATOR.IN.equalsIgnoreCase(operator)) {
            if (CommonConstants.SQL_PRO_TYPE.LONG.equalsIgnoreCase(propertyType)) {
                value = DataUtil.convertLongArr(value);
                return value;
            } else if (CommonConstants.SQL_PRO_TYPE.INT.equalsIgnoreCase(propertyType)) {
                value = DataUtil.convertIntegerArr(value);
                return value;
            } else if (CommonConstants.SQL_PRO_TYPE.DOUBLE.equalsIgnoreCase(propertyType)) {
                value = DataUtil.convertDoubleArr(value);
                return value;
            } else if (CommonConstants.SQL_PRO_TYPE.STRING.equalsIgnoreCase(propertyType)) {
                value = DataUtil.convertToStringArr(value);
                return value;
            }
        }
        return value;
    }

    private Predicate buildPredicateGreater(Root<T> root, CriteriaBuilder criteriaBuilder, Condition criteria) {
        if (criteria.getPropertyType().toLowerCase().equals(CommonConstants.SQL_PRO_TYPE.DATE)) {
            var dateValue = DateUtil.parseStringToDatetime(criteria.getValue());
            return criteriaBuilder.greaterThan(criteriaBuilder.function("DATE", Date.class, root.get(criteria.getProperty())), dateValue);
        } else {
            return criteriaBuilder.greaterThan(root.get(criteria.getProperty()), criteria.getValue().toString());
        }
    }

    private Predicate buildPredicateLower(Root<T> root, CriteriaBuilder criteriaBuilder, Condition criteria) {
        if (criteria.getPropertyType().toLowerCase().equals(CommonConstants.SQL_PRO_TYPE.DATE)) {
            var dateValue = DateUtil.parseStringToDatetime(criteria.getValue());
            return criteriaBuilder.lessThan(criteriaBuilder.function("DATE", Date.class, root.get(criteria.getProperty())), dateValue);
        } else {
            return criteriaBuilder.lessThan(root.get(criteria.getProperty()), criteria.getValue().toString());
        }
    }

    private Predicate buildPredicateGreaterEqual(Root<T> root, CriteriaBuilder criteriaBuilder, Condition criteria) {
        if (criteria.getPropertyType().toLowerCase().equals(CommonConstants.SQL_PRO_TYPE.DATE)) {
            var dateValue = DateUtil.parseStringToDatetime(criteria.getValue());
            return criteriaBuilder.greaterThanOrEqualTo(root.get(criteria.getProperty()), dateValue);
        } else {
            return criteriaBuilder.greaterThanOrEqualTo(root.get(criteria.getProperty()), criteria.getValue().toString());
        }
    }

    private Predicate buildPredicateLowerEqual(Root<T> root, CriteriaBuilder criteriaBuilder, Condition criteria) {
        if (criteria.getPropertyType().toLowerCase().equals(CommonConstants.SQL_PRO_TYPE.DATE)) {
            var dateValue = DateUtil.parseStringToDatetime(criteria.getValue());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dateValue);
            calendar.add(Calendar.MINUTE, 1);
            return criteriaBuilder.lessThanOrEqualTo(root.get(criteria.getProperty()), calendar.getTime());
        } else {
            return criteriaBuilder.lessThanOrEqualTo(root.get(criteria.getProperty()), criteria.getValue().toString());
        }
    }

    private Predicate buildPredicateEqual(Root<T> root, CriteriaBuilder criteriaBuilder, Condition criteria) {
        if (criteria.getPropertyType().toLowerCase().equals(CommonConstants.SQL_PRO_TYPE.DATE)) {
            var dateValue = DateUtil.parseStringToDatetime(criteria.getValue());
            return criteriaBuilder.equal(criteriaBuilder.function("DATE", Date.class, root.get(criteria.getProperty())), dateValue);
        } else {
            return criteriaBuilder.equal(
                    root.get(criteria.getProperty()), criteria.getValue());
        }
    }
}
