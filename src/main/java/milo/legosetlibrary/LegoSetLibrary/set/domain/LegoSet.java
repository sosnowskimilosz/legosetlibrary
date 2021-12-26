package milo.legosetlibrary.LegoSetLibrary.set.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import milo.legosetlibrary.LegoSetLibrary.user.domain.User;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@ToString(exclude = "users")
@Entity
@RequiredArgsConstructor
public class LegoSet {

    @Id
    @GeneratedValue
    private Long id;
    private String catalogNumber;
    private String title;
    @Enumerated(value = EnumType.STRING)
    private LegoSetStatus status;
    private LocalDate purchasingDate;
    @Enumerated(value = EnumType.STRING)
    private LegoCategory category;
    private Integer numberOfPieces;
    private BigDecimal price;
    private Long coverOfBoxId;

    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "legoSets")
    @JsonIgnoreProperties("legoSets")
    private Set<User> users = new HashSet<>();

    public LegoSet(String catalogNumber, String title, LegoCategory category, Integer numberOfPieces, BigDecimal price) {
        this.catalogNumber = catalogNumber;
        this.title = title;
        this.status = LegoSetStatus.ON_DREAM_LIST;
        this.category = category;
        this.numberOfPieces = numberOfPieces;
        this.price = price;
    }

    public void addUser(User user){
        users.add(user);
        user.getLegoSets().add(this);
    }

    public void removeUser(User user){
        users.remove(user);
        user.getLegoSets().remove(this);
    }

    public void removeUsers(){
        LegoSet self = this;
        users.forEach(user -> user.getLegoSets().remove(self));
        users.clear();
    }
}
