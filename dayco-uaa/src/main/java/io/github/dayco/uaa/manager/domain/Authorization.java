package io.github.dayco.uaa.manager.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "uaa_authorization")
public class Authorization {
    @Id
    @Column(name = "authorization_id")
    @GeneratedValue
    private Integer id;
    private String name;
    private String description;
    private LocalDateTime createTime;
    private LocalDateTime modifyTime;
    @ManyToMany(mappedBy = "authorizations")
    private List<Resource> resources = new ArrayList<>();

    private Authorization() {}

    public Authorization(String name, String description) {
        this.name = name;
        this.description = description;
        this.createTime = LocalDateTime.now();
    }

    public void addResource(Resource resource) {
        this.resources.add(resource);
        if(resource.getAuthorizations().contains(this)){
            resource.getAuthorizations().add(this);
        }
    }

    public List<Resource> getResources() {
        return resources;
    }

    public String getName() {
        return name;
    }
}
