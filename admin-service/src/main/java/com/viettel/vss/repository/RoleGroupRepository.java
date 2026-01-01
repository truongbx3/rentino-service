package com.viettel.vss.repository;

import com.viettel.vss.base.BaseRepository;
import com.viettel.vss.dto.roleGroup.RoleGroupResponseDto;
import com.viettel.vss.dto.roleGroup.RoleGroupSearchRequest;
import com.viettel.vss.dto.roleGroup.RoleGroupSearchResponse;
import com.viettel.vss.entity.RoleGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleGroupRepository extends BaseRepository<RoleGroup, Long>{
    @Query("select r from RoleGroup r where r.id in :listRole ")
    List<RoleGroup> findByListRolesId(@Param("listRole") List<Long> roleGroupIds);

    @Query("select rg from RoleGroup rg " +
            "where rg.defaultGroup = true " +
            "and rg.isDeleted = 0")
    RoleGroup findDefaultRoleGroup(Long storeId, Long positionId);

    @Query("select rg from RoleGroup rg " +
            "where rg.defaultGroup = true " +
            "and rg.isDeleted = 0")
    List<RoleGroup> findAllDefaultRoleGroup();

    @Query("select rg.id from RoleGroup rg where rg.isAdmin = true and rg.isDeleted = 0 and rg.isActive = 1")
    Long getIdRoleGroupAdmin();

    @Query("select rg from RoleGroup rg where rg.roleGroupName = ?1 and rg.isDeleted = 0")
    RoleGroup findByRoleGroupName(String roleGroupName);

    @Query(value = "select rgo.organization_id from role_group_organization rgo where rgo.role_group_id = ?1", nativeQuery = true)
    List<Long> findOrganizationRoleGroup(Long roleGroupId);

    @Query(value = "select rgp.position_id from role_group_position rgp where rgp.role_group_id = ?1", nativeQuery = true)
    List<Long> findPositionRoleGroup(Long roleGroupId);

    @Modifying
    @Query(value = "delete from user_role_group where role_group_id = ?1", nativeQuery = true)
    Integer deleteUserRoleGroup(Long roleGroupId);

    @Modifying
    @Query(value = "delete from user_role_group where role_group_id = ?1 and user_id in (?2)", nativeQuery = true)
    Integer deleteUserRoleGroup(Long roleGroupId, List<Long> staffIds);

    @Query(value = "select count(sr.role_group_id) from user_role_group sr where sr.role_group_id = ?1 and sr.user_id = ?2", nativeQuery = true)
    Integer countUserRoleGroup(Long roleGroupId, Long staffId);

    @Modifying
    @Query(value = "insert into user_role_group(role_group_id, user_id) values (?1, ?2)", nativeQuery = true)
    Integer insertUserRoleGroup(Long roleGroupId, Long staffId);

    @Query("select distinct new com.viettel.vss.dto.roleGroup.RoleGroupResponseDto(" +
            "  rg.id, " +
            "  rg.roleGroupName, " +
            "  rg.roleGroupCode, " +
            "  rg.description, " +
            "  (select count(u.id) " +
            "     from UserEntity u " +
            "     join u.roleGroups rgu " +
            "    where u.isDeleted = 0 " +
            "      and rgu.id = rg.id" +
            "  ) " +
            ") " +
            "from RoleGroup rg " +
            "where rg.isDeleted = 0 " +
            "order by rg.createdDate desc")
    List<RoleGroupResponseDto> getListRoleGroup();

    @Query("select distinct new com.viettel.vss.dto.roleGroup.RoleGroupResponseDto(" +
            "  rg.id, " +
            "  rg.roleGroupName, " +
            "  rg.roleGroupCode, " +
            "  rg.description) " +
            "from RoleGroup rg " +
            "join rg.users u " +
            "where rg.isDeleted = 0 " +
            "      and u.id = :userId " +
            "order by rg.createdDate desc")
    List<RoleGroupResponseDto> findByUserId(Long userId);

    @Query("select distinct new com.viettel.vss.dto.roleGroup.RoleGroupSearchResponse(" +
            "  rg.id, " +
            "  rg.roleGroupName, " +
            "  rg.roleGroupCode, " +
            "  rg.description) " +
            "from RoleGroup rg " +
            "left join rg.users u " +
            "where rg.isDeleted = 0 " +
            " and (:#{#roleGroupSearchRequest.userId} is null or u.id = :#{#roleGroupSearchRequest.userId}) " +
            " and (:#{#roleGroupSearchRequest.roleGroupName} is null or lower(rg.roleGroupName) like lower(concat('%',:#{#roleGroupSearchRequest.roleGroupName},'%'))) " +
            " and (:#{#roleGroupSearchRequest.isActive} is null or rg.isActive = :#{#roleGroupSearchRequest.isActive}) " )
    Page<RoleGroupSearchResponse> searchRoleGroup(RoleGroupSearchRequest roleGroupSearchRequest, Pageable pageable);
}