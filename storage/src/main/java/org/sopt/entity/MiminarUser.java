package org.sopt.entity;

import jakarta.persistence.*;
import lombok.*;
import org.sopt.entity.base.BaseEntity;
import org.sopt.entity.enums.RegisterPlatform;


@Getter
@Entity
@Builder
@Table(schema = "miminar-demo", name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MiminarUser extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "username")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "platform")
    @Enumerated(EnumType.STRING)
    private RegisterPlatform platform; // INTERNAL, KAKAO, NAVER, GOOGLE
}
