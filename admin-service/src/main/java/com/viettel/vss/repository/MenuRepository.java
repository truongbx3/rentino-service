package com.viettel.vss.repository;

import java.util.List;
import java.util.Optional;

import com.viettel.vss.base.BaseRepository;
import com.viettel.vss.entity.Menu;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuRepository extends BaseRepository<Menu, Long>{
    List<Menu> findByIdIn(List<Long> parentId);
    List<Menu> findByIsDeleted(int deleted);

    @Query("select m.menuName from Menu m where upper(m.menuName) = :menuName " +
            "and m.isDeleted = 0 and m.module.id = :moduleId")
    List<String> checkExistMenuName(String menuName, Long moduleId);

    @Query("select m.menuName from Menu m where upper(m.menuName) = :menuName " +
            "and m.isDeleted = 0")
    List<String> checkExistMenuName(String menuName);

    @Query(value = "select count(f) from Menu m join m.functions f " +
            "where m.id = ?1 and f.isDeleted = 0")
    Integer countFunctionOfMenu(Long menuId);

    @Query(value = "select * from menu where menu.module_id = :moduleId and menu.menu_name = :menuName", nativeQuery = true)
    Optional<Menu> getMenuByNameAndModuleId(Long moduleId, String menuName);


    @Query(value = "select me from Menu me where me.module.id = :moduleId and me.isDeleted = 0 " +
            "order by -me.indexOrder desc, me.createdDate desc ")
    List<Menu> getMenuByModuleId(Long moduleId);

    @Query(value = "select me from Menu me where  me.isDeleted = 0 " +
            "order by -me.indexOrder desc, me.createdDate desc ")
    List<Menu> findAllByIsActive();

    @Query(value = "select m.* from menu m\n" +
            "where (:isAdmin = true or m.id in (\n" +
            " select mf.menu_id from user u \n" +
            " left join user_role_group sr on sr.user_id = u.id \n" +
            " left join function_role_group frg on frg.role_group_id = sr.role_group_id\n" +
            " left join menu_function mf on mf.function_id = frg.function_id\n" +
            " where \n" +
            "   sr.role_group_id in (\n" +
            "       select id from role_group rg where rg.is_deleted = 0 and rg.is_active = 1 \n" +
            "   )\n" +
            "   and mf.function_id in (\n" +
            "       select id from `function` f where f.is_deleted = 0 and f.is_active = 1\n" +
            "   )\n" +
            "   and u.email = :email \n" +
            ")) and m.is_show = 1 and m.is_active = 1 and m.is_deleted = 0 order by m.index_order asc", nativeQuery = true)
    List<Menu> getMenuUser(@Param("isAdmin") boolean isAdmin,
                           @Param("email") String email);

    @Query(value = "select * from menu where is_deleted = 0 and is_active = 1 and is_show = 1 order by index_order asc", nativeQuery = true)
    List<Menu> getByApplication();

    @Query(value = "select * from menu where is_deleted = 0 and is_active = 1 order by index_order asc", nativeQuery = true)
    List<Menu> getAll();

    @Query(value = "delete from menu_function where function_id in :functionIds and menu_id = :menuId", nativeQuery = true)
    @Modifying
    Integer removeFunction(List<Long> functionIds, Long menuId);

    List<Menu> findAllByIsDeletedAndIsActive(int isDeleted, int isActive);
}