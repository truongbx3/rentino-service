package com.viettel.vss.repository.specification;

import com.viettel.vss.dto.roleGroup.RoleGroupSearchRequest;
import com.viettel.vss.entity.RoleGroup;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.CollectionUtils;

import java.util.List;

public class RoleGroupSpecification {
    private RoleGroupSpecification() {
    }

    public static Specification<RoleGroup> filter(RoleGroupSearchRequest roleGroupSearchRequest) {
        return Specification.where(withRoleGroupName(roleGroupSearchRequest.getRoleGroupName()));
//                .and(withIsDeleted(roleGroupSearchRequest.getIsDeleted()));
//                .and(withIsActive(roleGroupSearchRequest.getIsActive()));
    }

    public static Specification<RoleGroup> withRoleGroupName(String roleGroupName) {
//        if (StringUtils.isBlank(roleGroupName)) return null;
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("roleGroupName"), "%" + roleGroupName + "%");
    }

    public static Specification<RoleGroup> withListDepartmentId(List<Long> departmentIds) {
        if (CollectionUtils.isEmpty(departmentIds))
            return null;
        return (root, query, criteriaBuilder) -> {
            query.distinct(true);
            return root.join("department").get("id").in(departmentIds);
        };
    }

    public static Specification<RoleGroup> withListPositionId(List<Long> positionIds) {
        if (CollectionUtils.isEmpty(positionIds))
            return null;
        return (root, query, criteriaBuilder) -> {
            query.distinct(true);
            return root.join("roleGroupPositions").get("position").get("id").in(positionIds);
        };
    }

    public static Specification<RoleGroup> withListStoreId(List<Long> storeIds) {
        if (CollectionUtils.isEmpty(storeIds))
            return null;
        return (root, query, criteriaBuilder) -> {
            query.distinct(true);
            return root.join("roleGroupOrganizations").get("organization").get("id").in(storeIds);
        };
    }

    public static Specification<RoleGroup> withIsActive(Boolean isActive) {
        if (isActive == null)
            return null;
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("isActive"), isActive);
    }

    public static Specification<RoleGroup> withIsDeleted(Boolean isDeleted) {
        if (isDeleted == null)
            return null;
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("isDeleted"), isDeleted);
    }
}
