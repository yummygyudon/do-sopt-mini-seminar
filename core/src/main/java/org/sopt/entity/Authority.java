package org.sopt.entity;

import lombok.*;
import org.sopt.entity.base.BaseEntity;
import org.sopt.entity.enums.Role;

import javax.persistence.*;

@Getter
@Entity
@Builder
@Table(schema = "miminar_demo", name ="authorities")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Authority extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Role role;

}
