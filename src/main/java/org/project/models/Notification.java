package org.project.models;

import javafx.beans.property.*;
import java.time.LocalDateTime;

public class Notification {
    private final IntegerProperty notificationId = new SimpleIntegerProperty();
    private final IntegerProperty userId = new SimpleIntegerProperty();
    private final StringProperty message = new SimpleStringProperty();
    private final ObjectProperty<LocalDateTime> timestamp = new SimpleObjectProperty<>();
    private final BooleanProperty is_read = new SimpleBooleanProperty();

    public Notification(){
        this(-1,-1,"", LocalDateTime.now(), false);
    }

    public Notification(int notificationId, int userId, String message, LocalDateTime timestamp, boolean is_read){
        setNotificationId(notificationId);
        setUserId(userId);
        setMessage(message);
        setTimestamp(timestamp);
        setRead(is_read);
    }

    //Property accessors
    public IntegerProperty notificationIdProperty(){
        return notificationId;
    }
    public IntegerProperty userIdProperty(){
        return userId;
    }
    public StringProperty messageProperty(){
        return message;
    }
    public ObjectProperty<LocalDateTime> timestampProperty(){
        return timestamp;
    }
    public BooleanProperty is_readProperty(){
        return is_read;
    }

    //getters
    public int getNotificationId() {
        return notificationId.get();
    }
    public int getUserId() {
        return userId.get();
    }
    public String getMessage(){
        return message.get();
    }
    public LocalDateTime getTimestamp(){
        return timestamp.get();
    }
    public boolean isRead(){
        return is_read.get();
    }

    //setters
    public void setNotificationId(int id){
        notificationId.set(id);
    }
    public void setUserId(int id){
        userId.set(id);
    }
    public void setMessage(String msg){
        message.set(msg);
    }
    public void setTimestamp(LocalDateTime time){
        timestamp.set(time);
    }
    public void setRead(boolean read){
        is_read.set(read);
    }

    @Override
    public String toString(){
        return String.format("Notification[id=%d, user=%d, message=%s, time=%s, read=%b]",getNotificationId(), getUserId(), getMessage(), getTimestamp().toString(), isRead());
    }
}
