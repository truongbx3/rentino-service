package com.viettel.vss.repository;

import com.viettel.vss.base.BaseRepository;
import com.viettel.vss.entity.OtpEntity;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OtpRepository extends BaseRepository<OtpEntity, Long>{
    @Query("SELECT count(*) FROM OtpEntity o " +
            "WHERE o.userId =:userId AND o.otp=:otp AND o.expiredAt>=:expiredAt " +
            "ORDER BY o.id DESC")
    List<OtpEntity> otpValid(Long userId, String otp, LocalDateTime expiredAt);

    @Query("SELECT count(*) FROM OtpEntity o " +
            "WHERE o.userId =:userId AND o.otp=:otp AND o.expiredAt>=:expiredAt ")
    Long checkOtpExpChangePw(Long userId, String otp, LocalDateTime expiredAt);

    @Query("SELECT o FROM OtpEntity o " +
            "WHERE o.userId =:userId  AND o.expiredAt>=:expiredAt AND o.isDeleted = 0 order by o.id DESC ")
    List<OtpEntity> findByOtpAndIsDeleted(Long userId, LocalDateTime expiredAt );
}
