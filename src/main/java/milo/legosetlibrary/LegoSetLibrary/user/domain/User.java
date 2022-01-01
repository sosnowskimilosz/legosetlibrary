package milo.legosetlibrary.LegoSetLibrary.user.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import milo.legosetlibrary.LegoSetLibrary.jpa.BaseEntity;
import milo.legosetlibrary.LegoSetLibrary.set.domain.LegoSet;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(name = "users")
@ToString(exclude = "legoSets")
@EntityListeners(AuditingEntityListener.class)
public class User extends BaseEntity {

    @Column(unique = true)
    private String login;

    @CreatedDate
    private LocalDateTime createdAt;

    @ManyToMany(cascade = {CascadeType.PERSIST,CascadeType.MERGE})
    @JsonIgnoreProperties("users")
    @JoinTable
    private Set<LegoSet> legoSets = new HashSet<>();

    public User(String login) {
        this.login = login;
    }

    public void addLegoSet(LegoSet legoSet) {
        legoSets.add(legoSet);
        legoSet.getUsers().add(this);
    }

    public void removeLegoSet(LegoSet legoSet) {
        legoSets.remove(legoSet);
        legoSet.getUsers().remove(this);
    }

    public void removeLegoSets() {
        User user = this;
        legoSets.forEach(legoSet -> legoSet.getUsers().remove(this));
        legoSets.clear();
    }

}
