package com.viettel.vss.repository;

import com.viettel.vss.base.BaseRepository;
import com.viettel.vss.entity.Module;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ModuleRepository extends BaseRepository<Module, Long>{
    @Query("select m from Module m where m.isDeleted = 0 " +
            " order by -m.indexOrder desc , m.createdDate desc")
    List<Module> findAll();

    @Query("select m.moduleName from Module m where m.isDeleted = 0 " +
            " and upper(m.moduleName) = :name ")
    List<String> findModuleByModuleName(String name);


    @Query(value = "with recursive ancestor as (\n" +
            "select\n" +
            "m.*\n" +
            "from\n" +
            "module m where m.id in (:listModuleId)\n" +
            "union all\n" +
            "select\n" +
            "m2.*\n" +
            "from\n" +
            "module m2,\n" +
            "ancestor a where m2.id =a.parent_id\n" +
            ") select * from ancestor a1 order by a1.index_order  desc , a1.created_date  desc", nativeQuery = true)
    List<Module> findByModuleId(List<Long> listModuleId);
}