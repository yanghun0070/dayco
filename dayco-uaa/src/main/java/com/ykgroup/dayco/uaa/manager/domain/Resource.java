package com.ykgroup.dayco.uaa.manager.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import lombok.ToString;

@Entity
@ToString
@Table(name = "uaa_resource")
public class Resource {
    @Id
    @Column(name = "resource_id")
    @GeneratedValue
    private Integer id;
    private String name;
    private String description;
    private LocalDateTime createTime;
    private LocalDateTime modifyTime;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "uaa_authorization_resource",
            joinColumns = @JoinColumn(name = "resource_id"),
            inverseJoinColumns = @JoinColumn(name = "authorization_id"))
    private List<Authorization> authorizations = new ArrayList<>();

    private Resource() {}

    public Resource(String name, String description) {
        this.name = name;
        this.description = description;
        this.createTime = LocalDateTime.now();
        this.modifyTime = LocalDateTime.now();
    }

    public void addAuthorization(Authorization authorization) {
        this.authorizations.add(authorization);
        if(authorization.getResources().contains(this)){
            authorization.getResources().add(this);
        }
    }

    public List<Authorization> getAuthorizations() {
        return authorizations;
    }

    public void setAuthorizations(List<Authorization> authorizations) {
        this.authorizations = authorizations;
    }

    public String getName() {
        return name;
    }
}
