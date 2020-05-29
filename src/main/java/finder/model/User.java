package finder.model;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "customUser")
public class User {
    @Id
    @GeneratedValue
    private Long id;
    private Long chatId;
    private Integer stateId;
    private Boolean isNewUser=true;
    private Boolean admin;
    private Boolean notified = false;

    @ManyToMany(mappedBy = "users",fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    List<Vacancy> vacancyList = new ArrayList<>();


    public User() {
    }

    public User(Long chatId, Integer state) {
        this.chatId = chatId;
        this.stateId = state;

    }

    public User(Long chatId, Integer stateId, Boolean admin) {
        this.chatId = chatId;
        this.stateId = stateId;
        this.admin = admin;
    }






    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public Integer getStateId() {
        return stateId;
    }

    public void setStateId(Integer stateId) {
        this.stateId = stateId;
    }


    public Boolean getIsNewUser() {
        return isNewUser;
    }

    public void setIsNewUser(Boolean newUser) {
        isNewUser = newUser;
    }


    public List<Vacancy> getVacancyList() {
        return vacancyList;
    }

    public void setVacancyList(List<Vacancy> vacancyList) {
        this.vacancyList.addAll(vacancyList);
    }

    public Boolean getAdmin() {
        return admin;
    }

    public void setAdmin(Boolean admin) {
        this.admin = admin;
    }

    public Boolean getNotified() {
        return notified;
    }

    public void setNotified(Boolean notified) {
        this.notified = notified;
    }

    public void addToVacancyList(List<Vacancy> vacancyList) {
        this.vacancyList.addAll(vacancyList);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return Objects.equals(getChatId(), user.getChatId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getChatId());
    }


}
