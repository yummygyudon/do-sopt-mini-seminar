package org.sopt.entity.base;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.sopt.entity.enums.RegisterPlatform;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {


    @Column(name = "username")
    private String name;

    @Column(name = "platform")
    @Enumerated(EnumType.STRING)
    private RegisterPlatform platform; // INTERNAL, KAKAO, NAVER, GOOGLE

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;


    @LastModifiedDate
    @Column(name = "updated_at")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

}
