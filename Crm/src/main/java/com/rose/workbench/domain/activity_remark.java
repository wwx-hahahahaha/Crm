package com.rose.workbench.domain;

public class activity_remark {
//    private;
    private String id ;
    private String noteContent ;
    private String createTime  ;
    private String createBy    ;
    private String editTime    ;
    private String editBy      ;
    private String editFlag    ;
    private String activityId  ;


    public activity_remark() {
    }

    public activity_remark(String id, String noteContent, String createTime, String createBy, String editTime, String editBy, String editFlag, String activityId) {
        this.id = id;
        this.noteContent = noteContent;
        this.createTime = createTime;
        this.createBy = createBy;
        this.editTime = editTime;
        this.editBy = editBy;
        this.editFlag = editFlag;
        this.activityId = activityId;
    }

    /**
     * 获取
     * @return id
     */
    public String getId() {
        return id;
    }

    /**
     * 设置
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 获取
     * @return noteContent
     */
    public String getNoteContent() {
        return noteContent;
    }

    /**
     * 设置
     * @param noteContent
     */
    public void setNoteContent(String noteContent) {
        this.noteContent = noteContent;
    }

    /**
     * 获取
     * @return createTime
     */
    public String getCreateTime() {
        return createTime;
    }

    /**
     * 设置
     * @param createTime
     */
    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取
     * @return createBy
     */
    public String getCreateBy() {
        return createBy;
    }

    /**
     * 设置
     * @param createBy
     */
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    /**
     * 获取
     * @return editTime
     */
    public String getEditTime() {
        return editTime;
    }

    /**
     * 设置
     * @param editTime
     */
    public void setEditTime(String editTime) {
        this.editTime = editTime;
    }

    /**
     * 获取
     * @return editBy
     */
    public String getEditBy() {
        return editBy;
    }

    /**
     * 设置
     * @param editBy
     */
    public void setEditBy(String editBy) {
        this.editBy = editBy;
    }

    /**
     * 获取
     * @return editFlag
     */
    public String getEditFlag() {
        return editFlag;
    }

    /**
     * 设置
     * @param editFlag
     */
    public void setEditFlag(String editFlag) {
        this.editFlag = editFlag;
    }

    /**
     * 获取
     * @return activityId
     */
    public String getActivityId() {
        return activityId;
    }

    /**
     * 设置
     * @param activityId
     */
    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public String toString() {
        return "activity_remark{id = " + id + ", noteContent = " + noteContent + ", createTime = " + createTime + ", createBy = " + createBy + ", editTime = " + editTime + ", editBy = " + editBy + ", editFlag = " + editFlag + ", activityId = " + activityId + "}";
    }
}
