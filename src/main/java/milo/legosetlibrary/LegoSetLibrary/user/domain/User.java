package milo.legosetlibrary.LegoSetLibrary.user.domain;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue
    private Long id;
    private String login;

//    @ManyToMany(fetch = FetchType.EAGER)
//    private transient Set<Long> legoSets;

    public User(String login){
        this.login = login;
    }
}
