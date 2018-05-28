package com.nucleustech.mymentor.model;


/**
 * Created by ritwik.rai on 17/06/2016.
 */
public class ChatModel {

    private String id;
    private UserModel userModel;
    private String message;
    private String timeStamp;
    private String name;
    private String keyMap;
    private String from_firebaseId;
    private String to_firebaseId;
    private String instanceId;
    private String senderEmail;
    private FileModel file;
    private MapModel mapModel;


    public ChatModel() {
    }

    //Student Chat
    public ChatModel(UserModel userModel, String message, String to_firebaseId, String from_firebaseId, String instanceId, String senderEmail, String timeStamp, FileModel file) {
        this.userModel = userModel;
        this.name = userModel.getName();
        this.keyMap = "admin" + senderEmail;
        this.message = message;
        this.timeStamp = timeStamp;
        this.to_firebaseId = to_firebaseId;
        this.from_firebaseId = from_firebaseId;
        this.instanceId = instanceId;
        this.senderEmail = senderEmail;
        this.file = file;
    }

    // Admin Chat
    public ChatModel(UserModel userModel, String message, String to_firebaseId, String from_firebaseId, String instanceId, String senderEmail, String timeStamp, FileModel file, String studentName) {
        this.userModel = userModel;
        this.name = userModel.getName();
        this.keyMap = "admin" + studentName;
        this.message = message;
        this.timeStamp = timeStamp;
        this.to_firebaseId = to_firebaseId;
        this.from_firebaseId = from_firebaseId;
        this.instanceId = instanceId;
        this.senderEmail = senderEmail;
        this.file = file;
    }

    public ChatModel(UserModel userModel, String timeStamp, MapModel mapModel) {
        this.userModel = userModel;
        this.timeStamp = timeStamp;
        this.mapModel = mapModel;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public UserModel getUserModel() {
        return userModel;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setKeyMap(String keyMap) {
        this.keyMap = keyMap;
    }

    public String getKeyMap() {
        return keyMap;
    }

    public void setFrom_firebaseId(String from_firebaseId) {
        this.from_firebaseId = from_firebaseId;
    }

    public String getFrom_firebaseId() {
        return from_firebaseId;
    }

    public void setTo_firebaseId(String to_firebaseId) {
        this.to_firebaseId = to_firebaseId;
    }

    public String getTo_firebaseId() {
        return to_firebaseId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public void setUserModel(UserModel userModel) {
        this.userModel = userModel;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public FileModel getFile() {
        return file;
    }

    public void setFile(FileModel file) {
        this.file = file;
    }

    public MapModel getMapModel() {
        return mapModel;
    }

    public void setMapModel(MapModel mapModel) {
        this.mapModel = mapModel;
    }

    @Override
    public String toString() {
        return "ChatModel{" +
                "mapModel=" + mapModel +
                ", file=" + file +
                ", timeStamp='" + timeStamp + '\'' +
                ", name='" + name + '\'' +
                ", keyMap='" + keyMap + '\'' +
                ", message='" + message + '\'' +
                ", userModel=" + userModel +
                '}';
    }
}
