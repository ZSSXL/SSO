package com.zss.sso.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Table;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * @author zhoushs@dist.com.cn
 * @date 2020/11/19 17:14
 * @desc 用户实体
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "sso_user")
@Table(appliesTo = "sso_user", comment = "用户")
public class User implements Serializable {

    @Id
    @Column(columnDefinition = "varchar(100)")
    private String userId;

    @Column(columnDefinition = "varchar(50)")
    private String username;

    @Column(columnDefinition = "varchar(255)")
    private String password;

    @Column(columnDefinition = "varchar(20)")
    private String number;

    @Column(columnDefinition = "varchar(3)")
    private String age;

    @Column(columnDefinition = "varchar(100)")
    private String address;
}
