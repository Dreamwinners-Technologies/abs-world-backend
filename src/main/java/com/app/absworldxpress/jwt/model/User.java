package com.app.absworldxpress.jwt.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    private String id;

    @NotBlank
    @Size(min = 11, max = 11)
    String phoneNo;

    @NotBlank
    @Size(min = 3, max = 50)
    private String fullName;

    @Column(unique = true)
    @NotBlank
    @NaturalId
    @Size(min = 3, max = 100)
    private String username;

    @Column(unique = true)
    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    @NotBlank
    @Size(min = 6, max = 100)
    private String password;

    @JsonIgnore
    int generatedOTP=1234;

    @JsonIgnore
    boolean passwordReset;


    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    private String referralUsername;
    private String isActiveIncomeAccount;

    private String createdBy;
    private String createdOn;
    private String joiningDate;
    private String ExpiryDate;

    private double regBalance;
    private double IncomeBalance;
    private double shopBalance;

    private String deliveryAddress;
    private String profileImage;

    @OneToMany
    @JsonIgnore
    private List<User> myTeam;
}