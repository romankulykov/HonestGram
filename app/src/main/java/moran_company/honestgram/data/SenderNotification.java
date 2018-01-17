package moran_company.honestgram.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by roman on 16.01.2018.
 */

public class SenderNotification {
    @SerializedName("to")
    @Expose
    private String to;
    //@SerializedName("notification")
    @SerializedName("data")
    @Expose
    private NotificationBody notification;
    @SerializedName("priority")
    @Expose
    private int priority;

    public SenderNotification(){

    }

    public SenderNotification(String to, NotificationBody notification, int priority) {
        this.to = to;
        this.notification = notification;
        this.priority = priority;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public SenderNotification withTo(String to) {
        this.to = to;
        return this;
    }

    public NotificationBody getNotification() {
        return notification;
    }

    public void setNotification(NotificationBody notification) {
        this.notification = notification;
    }

    public SenderNotification withNotification(NotificationBody notification) {
        this.notification = notification;
        return this;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public SenderNotification withPriority(int priority) {
        this.priority = priority;
        return this;
    }
}
