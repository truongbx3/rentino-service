package com.viettel.vss.repository;

import com.viettel.vss.base.BaseRepository;
import com.viettel.vss.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends BaseRepository<UserEntity, Long>{



    @Query(value = "SELECT u FROM UserEntity u WHERE (" +
            "u.phone = :phone OR u.email = :email " +
            ") AND (COALESCE(:id, NULL) IS NULL OR u.id <> :id ) and u.isDeleted = 0 ")
    List<UserEntity> findByEmpPhoneOrEmail(@Param("phone") String phone, @Param("email") String email, @Param("id") Long id);

    @Query(value = "SELECT u FROM UserEntity u WHERE u.isDeleted = 0 AND u.id = ?1")
    UserEntity getUserById(Long id);
//
//    @Query(value = "SELECT new com.viettel.vss.dto.user.UserListDto(u.id as id, u.username as username, " +
//            "u.empCode as empCode, u.fullName as fullName, " +
//            "u.email as email, u.statusId as statusId, " +
//            "us.name as statusName, u.createdDate as createdDate, u.updatedDate as updatedDate ) " +
//            "FROM UserEntity u " +
//            "JOIN UserStatusEntity us ON u.statusId = us.id " +
//            "WHERE u.isDeleted = 0" +
//            "AND (:#{#request.username} IS NULL OR LOWER(u.username) LIKE LOWER(concat('%', :#{#request.username}, '%')) )" +
//            "AND (:#{#request.fullName} IS NULL OR LOWER(u.fullName) LIKE LOWER(concat('%', :#{#request.fullName}, '%')) )" +
//            "AND (:#{#request.email} IS NULL OR LOWER(u.email) LIKE LOWER(concat('%', :#{#request.email}, '%')) )" +
//            "AND (:#{#request.statusId} IS NULL OR u.statusId = :#{#request.statusId})" +
//            "AND (:#{#request.createdDate.start} IS NULL OR u.createdDate >= :#{#request.createdDate.start} ) " +
//            "AND (:#{#request.createdDate.end} IS NULL OR u.createdDate <= :#{#request.createdDate.end} )" +
//            "AND (:#{#request.updatedDate.start} IS NULL OR u.updatedDate >= :#{#request.updatedDate.start} ) " +
//            "AND (:#{#request.updatedDate.end} IS NULL OR u.updatedDate <= :#{#request.updatedDate.end} )" +
//            "")
//    Page<UserListDto> filterUsers(FilterUserRequest request, Pageable pageable);
//
//    @Query(value = "SELECT new com.viettel.vss.dto.user.UserListDto(u.id as id, u.username as username, " +
//            "u.empCode as empCode, u.fullName as fullName, " +
//            "u.email as email, u.statusId as statusId, " +
//            "us.name as statusName, u.createdDate as createdDate, u.updatedDate as updatedDate ) " +
//            "FROM UserEntity u " +
//            "JOIN UserStatusEntity us ON u.statusId = us.id " +
//            "WHERE u.isDeleted = 0 AND u.id = :userId")
//    UserListDto getDetailUser(@Param("userId") Long userId);
//
//    @Query(value = "SELECT new com.viettel.vss.dto.user.UserExportDto(u.id as id, u.username as username, u.fullName as fullName, " +
//            "u.email as email, u.statusId as statusId, " +
//            "us.name as statusName, u.description as description, u.createdDate as createdDate, u.updatedDate as updatedDate ) " +
//            "FROM UserEntity u " +
//            "JOIN UserStatusEntity us ON u.statusId = us.id " +
//            "WHERE u.isDeleted = 0" +
//            "AND (:#{#request.username} IS NULL OR LOWER(u.username) LIKE LOWER(concat('%', :#{#request.username}, '%')) )" +
//            "AND (:#{#request.fullName} IS NULL OR LOWER(u.fullName) LIKE LOWER(concat('%', :#{#request.fullName}, '%')) )" +
//            "AND (:#{#request.email} IS NULL OR LOWER(u.email) LIKE LOWER(concat('%', :#{#request.email}, '%')) )" +
//            "AND (:#{#request.statusId} IS NULL OR u.statusId = :#{#request.statusId})" +
//            "AND (:#{#request.createdDate.start} IS NULL OR u.createdDate >= :#{#request.createdDate.start} ) " +
//            "AND (:#{#request.createdDate.end} IS NULL OR u.createdDate <= :#{#request.createdDate.end} )" +
//            "AND (:#{#request.updatedDate.start} IS NULL OR u.updatedDate >= :#{#request.updatedDate.start} ) " +
//            "AND (:#{#request.updatedDate.end} IS NULL OR u.updatedDate <= :#{#request.updatedDate.end} )" +
//            "")
//    List<UserExportDto> exportUser(FilterUserRequest request, Sort sort);

    @Query(value = "SELECT u FROM UserEntity u WHERE u.isDeleted = 0")
    List<UserEntity> getExistingUsers();



}