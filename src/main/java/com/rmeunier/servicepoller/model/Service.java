package com.rmeunier.servicepoller.model;

import com.fasterxml.jackson.annotation.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "services")
public class Service implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String url;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private LocalDateTime timestamp;
    private String status;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Service() {
        // empty
    }

    public Service(String name, String url, LocalDateTime timestamp, String status) {
        this.name = name;
        this.url = url;
        this.timestamp = timestamp;
        this.status = status;
    }

    public Service(String name, String url, LocalDateTime timestamp, String status, User user) {
        this.name = name;
        this.url = url;
        this.timestamp = timestamp;
        this.status = status;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Service{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", creationTime=" + timestamp +
                ", status='" + status + '\'' +
                ", user=" + user +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Service))
            return false;

        Service other = (Service)o;

        if (Objects.equals(id, other.getId())) return true;
        if (id == null) return false;

        return id.equals(other.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, url, timestamp, status, user);
    }
}
