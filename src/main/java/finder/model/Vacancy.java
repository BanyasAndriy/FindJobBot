package finder.model;

import java.util.*;
import javax.persistence.*;

@Entity
public class Vacancy {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String url;
    @ManyToMany(fetch = FetchType.EAGER)
    private List<User> users= new ArrayList<>();

    public Vacancy(String url, String name) {
        this.url = url;
        this.name = name;
    }

    public Vacancy() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public void addUser(User user){
        this.users.add(user);
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vacancy)) return false;
        Vacancy vacancy = (Vacancy) o;
        return Objects.equals(getName(), vacancy.getName()) &&
                Objects.equals(getUrl(), vacancy.getUrl());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getUrl());
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Vacancy{");
        sb.append("name='").append(name).append('\'');
        sb.append(", url='").append(url).append('\'');
        sb.append('}');
        return sb.toString();
    }
}

