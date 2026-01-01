package com.viettel.vss.repository;

import com.viettel.vss.base.BaseRepository;
import com.viettel.vss.dto.function.FilterFunctionRequest;
import com.viettel.vss.dto.function.FunctionResponseDto;
import com.viettel.vss.dto.function.FunctionResponseProjection;
import com.viettel.vss.entity.Function;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FunctionRepository extends BaseRepository<Function, Long>{
    Optional<Object> findByFunctionCode(String functionCode);

    List<Function> findAllByIdInAndIsDeleted(List<Long> ids, int isDeleted);

    @Query(value = "select count(*) from menu_function mn where mn.function_id = ?1", nativeQuery = true)
    Integer countFunction(Long id);

    @Query("select distinct new com.viettel.vss.dto.function.FunctionResponseDto(" +
            "  f.id, " +
            "  f.functionCode, " +
            "  f.description, " +
            "  f.isActive, " +
            "  f.createdBy," +
            "  f.createdDate," +
            "  f.updatedBy," +
            "  f.updatedDate) " +
            "from Function f " +
            "where (f.isDeleted = :#{#filterFunctionRequest.getIsDeleted()}) " +
            "      and (:#{#filterFunctionRequest.getIsActive()} is null or f.isActive = :#{#filterFunctionRequest.getIsActive()}) " +
            "      and (:#{#filterFunctionRequest.functionCode} is null or lower(f.functionCode) like lower(concat('%',:#{#filterFunctionRequest.functionCode},'%'))) " +
            "      and (:#{#filterFunctionRequest.description} is null or lower(f.description) like lower(concat('%',:#{#filterFunctionRequest.description},'%'))) " +
            "      and (:#{#filterFunctionRequest.createdBy} is null or lower(f.createdBy) like lower(concat('%',:#{#filterFunctionRequest.createdBy},'%'))) " +
            "      and (:#{#filterFunctionRequest.updatedBy} is null or lower(f.updatedBy) like lower(concat('%',:#{#filterFunctionRequest.updatedBy},'%'))) " +
            "      and (:#{#filterFunctionRequest.createdDate?.start} is null or f.createdDate >=  :#{#filterFunctionRequest.createdDate?.start}) " +
            "      and (:#{#filterFunctionRequest.createdDate?.end}   is null or f.createdDate <=  :#{#filterFunctionRequest.createdDate?.end}) " +
            "      and (:#{#filterFunctionRequest.updatedDate?.start} is null or f.updatedDate >=  :#{#filterFunctionRequest.updatedDate?.start}) " +
            "      and (:#{#filterFunctionRequest.updatedDate?.end}   is null or f.updatedDate <=  :#{#filterFunctionRequest.updatedDate?.end}) " +
            "order by f.createdDate desc")
    Page<FunctionResponseDto> findAllByIsDeleted(FilterFunctionRequest filterFunctionRequest, Pageable pageable);

    Function findByIdAndIsDeleted(Long id, int isDeleted);

    @Query("select f from Function f where f.id in ?1 and f.isDeleted = 0 "
    )
    List<Function> findByIds(List<Long> functionIds);

    @Query("select f from Function f where f.isDeleted = 0")
    List<Function> getAdminFunction();

    @Query(value = "SELECT " +
            "    f.id, f.function_code functionCode, f.description, f.is_active isActive, " +
            "    f.created_by createdBy, f.created_date createdDate, f.updated_by updatedBy, " +
            "    f.updated_date updatedDate, m.menu_name menuName, m.id menuId \n" +
            "FROM menu_function mf " +
            "    JOIN function f " +
            "        ON mf.function_id = f.id " +
            "    JOIN menu m " +
            "        ON m.id = mf.menu_id \n" +
            "WHERE " +
            "    f.is_deleted = 0 \n" +
            "ORDER BY f.created_date DESC", nativeQuery = true)
    List<FunctionResponseProjection> getAllFunctions();

    @Query(value = "SELECT \n" +
            "    f.id, f.function_code functionCode, f.description, f.is_active isActive, \n" +
            "    f.created_by createdBy, f.created_date createdDate, f.updated_by updatedBy, \n" +
            "    f.updated_date updatedDate, m.menu_name menuName, m.id menuId \n" +
            "FROM menu_function mf \n" +
            "    JOIN function f \n" +
            "        ON mf.function_id = f.id \n" +
            "    JOIN menu m \n" +
            "        ON m.id = mf.menu_id \n" +
            "WHERE \n" +
            "    mf.menu_id IN (?1) AND f.is_deleted = 0 \n" +
            "ORDER BY f.created_date DESC", nativeQuery = true)
    List<FunctionResponseProjection> getListFunctionByListMenuId(List<Long> menuId);

    List<Function> findAllByIdIn(List<Long> id);

    @Query(value = "select f from Function f where f.isDeleted = 0 and f.functionCode = ?1 ")
    Optional<Function> findFunctionByFunctionCode(String functionCode);

    @Query("select distinct new com.viettel.vss.dto.function.FunctionResponseDto(" +
            "  f.id, " +
            "  f.functionCode, " +
            "  f.description, " +
            "  f.createdBy," +
            "  f.createdDate," +
            "  f.updatedBy," +
            "  f.updatedDate," +
            "  rg.id ) " +
            "from Function f " +
            "join f.roleGroupEntities rg " +
            "join rg.users u " +
            "where rg.isDeleted = 0 and f.isDeleted = 0 " +
            "      and u.username = :username " +
            "order by f.createdDate desc")
    List<FunctionResponseDto> findByUsername(String username);

    List<Function> findAllByIsDeletedAndIsActive(int isDeleted, int isActive);

    @Query("select f " +
            "from Function f " +
            "left join f.roleGroupEntities rg " +
            "where f.isDeleted = 0 and f.isActive = 1 " +
            "      and rg.id IN ?1 " +
            "order by f.createdDate desc")
    List<Function> getFunctionByRole(List<Long> roleIds);

    @Query("select distinct f.functionCode " +
            "from Function f " +
            "join f.roleGroupEntities rg " +
            "join rg.users u " +
            "where rg.isDeleted = 0 and f.isDeleted = 0 " +
            "      and u.email = :email " +
            "order by f.createdDate desc")
    List<String> findByUserRole(String email);
}