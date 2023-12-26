package org.sopt.entity;


import lombok.*;
import org.sopt.entity.base.BaseEntity;

import javax.persistence.*;

@Getter
@Entity
@Builder
@Table(schema = "miminar_demo", name ="auth_tokens")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthorizationToken extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "atk")
    private String accessToken; // 액세스 토큰

    @Column(name = "rtk")
    private String refreshToken; // 리프레시 토큰


    public void updateAccessToken(final String newAccessToken) {
        this.accessToken = newAccessToken;
    }
    public void updateRefreshToken(final String newRefreshToken) {
        this.refreshToken = newRefreshToken;
    }
}
