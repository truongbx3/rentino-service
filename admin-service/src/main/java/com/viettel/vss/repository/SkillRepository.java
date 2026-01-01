package com.viettel.vss.repository;

import com.viettel.vss.base.BaseRepository;
import com.viettel.vss.entity.Skill;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface SkillRepository extends JpaRepository<Skill, Long>, JpaSpecificationExecutor<Skill> {

    Boolean existsByNameAndIsDeleted(String name, int isDeleted);

    Optional<Skill> findByIdAndIsDeleted(Long id, int isDeleted);

    Boolean existsByNameAndIdNot(String name, Long skillId);

    @EntityGraph(attributePaths = {"skillActions"})
    Optional<Skill> findWithActionsById(Long id);

    @EntityGraph(attributePaths = {"agentSkills"})
    Optional<Skill> findWithAgentsById(Long id);

    Optional<Skill> findDetailByIdAndIsDeleted(Long id,int isDeleted );

    // Lấy tất cả không bị xóa
    List<Skill> findAllByIsDeleted(int isDeleted);

    // Lấy theo list id + không bị xóa
    List<Skill> findAllByIdInAndIsDeleted(List<Long> ids, int isDeleted);

    @Query(value =
       " SELECT * "+
        " FROM SKILLS s "+
                " WHERE s.is_deleted = 0 "+
                " AND (:name IS NULL OR LOWER(s.name) LIKE CONCAT('%', LOWER(:name), '%')) "+
                " AND (:description IS NULL OR LOWER(s.description) LIKE CONCAT('%', LOWER(:description), '%')) "+
                " AND (:createdFrom IS NULL OR s.created_date >= :createdFrom) "+
                " AND (:createdTo IS NULL OR s.created_date <= :createdTo) "+
                " AND (:updatedFrom IS NULL OR s.updated_date >= :updatedFrom) "+
                " AND (:updatedTo IS NULL OR s.updated_date <= :updatedTo) "+
                " ORDER BY s.created_date DESC",
            countQuery =
        " SELECT COUNT(*) "+
                " FROM SKILLS s"+
                " WHERE s.is_deleted = 0"+
                "   AND (:name IS NULL OR LOWER(s.name) LIKE CONCAT('%', LOWER(:name), '%'))"+
                "   AND (:description IS NULL OR LOWER(s.description) LIKE CONCAT('%', LOWER(:description), '%'))"+
                "   AND (:createdFrom IS NULL OR s.created_date >= :createdFrom)"+
                "  AND (:createdTo IS NULL OR s.created_date <= :createdTo)"+
                "  AND (:updatedFrom IS NULL OR s.updated_date >= :updatedFrom)"+
                "  AND (:updatedTo IS NULL OR s.updated_date <= :updatedTo)",
            nativeQuery = true)
    Page<Object[]> searchSkills(
            @Param("name") String name,
            @Param("description") String description,
            @Param("createdFrom") Date createdFrom,
            @Param("createdTo") Date createdTo,
            @Param("updatedFrom") Date updatedFrom,
            @Param("updatedTo") Date updatedTo,
            Pageable pageable
    );
}