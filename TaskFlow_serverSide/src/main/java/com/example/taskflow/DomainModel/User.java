package com.example.taskflow.DomainModel;
import java.util.UUID;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;

@Document
public class User {
    /*
        FIXME: si potrebbe mantenere lo username in questa classe e le info sensibili in 
               userInfo? Lo username dovrebbe essere una cosa pubblica mi immagino. 
               Anche perché sennò a che serve questa classe?
    */
    @Id
    private String id;

    @DBRef
    private UserInfo userInfo;

    private UUID uuid;

    // costruttore di default
    public User(){
    }

    public User(UserInfo userInfo){
        this.userInfo = userInfo;
        this.uuid = UUID.randomUUID();
    }

    @Override
    public boolean equals(Object obj) {
        boolean value = false;

        if (obj != null && obj instanceof User){
            value = (this.uuid == ((User)obj).getUuid());  
        }

        return value;
    }


    // getter e setter

    public String getId() {
        return id;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }
    
    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public UUID getUuid() {
        return uuid;
    }
    
}
