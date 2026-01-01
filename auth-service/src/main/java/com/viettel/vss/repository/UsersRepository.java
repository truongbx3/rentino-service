package com.viettel.vss.repository;

import com.viettel.vss.base.BaseRepository;
import com.viettel.vss.entity.UserEntity;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UsersRepository extends BaseRepository<UserEntity, Long>{
    @Query("SELECT u FROM UserEntity u " +
            "WHERE u.phone =:phone  and u.isDeleted=0 ")
    Optional<UserEntity> findByPhoneLoginWeb(String phone);
//

//    @Query("SELECT new com.viettel.vss.dto.InforAppDto(usr.id, t.code, v.code, sr.expiredDate  ) from UsersEntity u " +
//            "JOIN UserServiceRegistrationEntity  usr ON usr.userId=u.id and usr.isDeleted=0 and usr.isActive=1 " +
//            "JOIN ServiceRegistrationEntity sr ON sr.id = usr.serviceRegistrationId AND sr.isDeleted=0 " +
//            "JOIN ServicesEntity s ON s.id = sr.serviceId AND s.isDeleted=0 " +
//            "JOIN ToolVersionEntity tv ON tv.id = s.toolVersionId AND tv.isDeleted=0 " +
//            "JOIN ToolsEntity t ON t.id = tv.toolId AND t.isDeleted=0 " +
//            "JOIN VersionsEntity v ON v.id = tv.versionId AND v.isDeleted=0 " +
//            "WHERE u.id=:userId AND lower(t.code)= lower(:typeApp) AND sr.status=2 AND sr.expiredDate >= NOW() ORDER BY sr.id DESC")
//    List<InforAppDto> getInforApp(Long userId, String typeApp);


    @Query(value = "SELECT u from UserEntity u where u.id = :id and u.isDeleted = 0")
    Optional<UserEntity> getUsersById(Long id);
//
//    @Query(value = "SELECT u from UserEntity u where u.id = :id and u.isDeleted = 0 AND u.isAdmin = :isAdmin")
//    Optional<UserEntity> findUserAdmin(Long id, Integer isAdmin);

//    Optional<UserEntity> findUsersEntityByEmailAndCompanyCodeAndIsDeleted(String email, String companyCode, Integer isDeleted);
//
//    Optional<UserEntity> findUsersEntityByEmailAndCompanyCodeAndIsDeletedAndIsAdmin(String email, String companyCode, Integer isDeleted, Integer isAdmin);
//
//    Optional<UserEntity> findByIdAndIsDeleted(Long id, Integer isDeleted);
}
