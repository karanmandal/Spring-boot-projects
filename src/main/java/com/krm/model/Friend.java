package com.krm.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Date;
import java.util.Objects;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("friend")
public class Friend {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter
    @Getter
    private String id;

    @Setter
    @Getter
    private Date createdAt = new Date();

    @Setter
    @Getter
    private Date updateAt = new Date();

    @Setter
    @Getter
    private boolean accepted = false;

    @JsonIgnore
    @DBRef(lazy = true)
    private User senderUser;

    @JsonIgnore
    @DBRef(lazy = true)
    private User receiverUser;

    public Friend() {
    }

    public Friend(User senderUser, User receiverUser) {
        this.senderUser = senderUser;
        this.receiverUser = receiverUser;
    }

    public String getSenderFirstName() {
        return senderUser.getFirstName();
    }

    public String getSenderLastName() {
        return senderUser.getLastName();
    }

    public String getSenderId() {
        return senderUser.getId();
    }

    public String getReceiverFirstName() {
        return receiverUser.getFirstName();
    }

    public String getReceiverLastName() {
        return receiverUser.getLastName();
    }

    public String getReceiverId() {
        return receiverUser.getId();
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 53 * hash + Objects.hashCode(getSenderId() + getReceiverId());
        return hash;
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        Friend compareFriend = ((Friend) obj);

        return Objects.equals(
                getSenderId() + getReceiverId(),
                compareFriend.getSenderId() + compareFriend.getReceiverId());
    }

}
