package cmpe275.vms.models;

import javax.persistence.*;
import java.util.UUID;

@Entity
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long tokenId;

    @OneToOne(targetEntity = User.class)
    @JoinColumn(name = "email")
    private User user;

    private String token;

    public Token() {
    }

    public Token(User user) {
        this.user = user;
        this.token = UUID.randomUUID().toString();
    }

    public long getTokenId() {
        return tokenId;
    }

    public void setTokenId(long tokenId) {
        this.tokenId = tokenId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
