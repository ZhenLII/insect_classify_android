package cn.edu.fafu.se3166016001.butterflyclassify.Model;


import java.sql.Timestamp;


public class InsectOrder {
    private int orderId;
    private int type;
    private int state;

    private String typeName;
    private String stateName;

    private int insectId;
    private int classId;
    private String insectName;
    private String insectAlias;
    private String scientificName;
    private String className;
    private String order;
    private String family;
    private String genus;
    private String description;
    private String feature;
    private String pic;
    private String userPic;

    private String creator;
    private int supportCount;
    private Timestamp createTime;
    private String createTimeStr;

    private boolean hasSupported;

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public int getSupportCount() {
        return supportCount;
    }

    public void setSupportCount(int supportCount) {
        this.supportCount = supportCount;
    }

    public InsectOrder() {

    }

    public InsectOrder(Insect insect) {
        this.insectId = insect.getInsectId();
        this.insectName = insect.getInsectName();
        this.insectAlias = insect.getInsectAlias();
        this.scientificName = insect.getScientificName();
        this.order = insect.getOrder();
        this.family = insect.getFamily();
        this.genus = insect.getGenus();
        this.description = insect.getDescription();
        this.feature = insect.getFeature();
        this.pic = insect.getPic();
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getInsectId() {
        return insectId;
    }

    public void setInsectId(int insectId) {
        this.insectId = insectId;
    }

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public String getInsectName() {
        return insectName;
    }

    public void setInsectName(String insectName) {
        this.insectName = insectName;
    }

    public String getInsectAlias() {
        return insectAlias;
    }

    public void setInsectAlias(String insectAlias) {
        this.insectAlias = insectAlias;
    }

    public String getScientificName() {
        return scientificName;
    }

    public void setScientificName(String scientificName) {
        this.scientificName = scientificName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getFamily() {
        return family;
    }

    public void setFamily(String family) {
        this.family = family;
    }

    public String getGenus() {
        return genus;
    }

    public void setGenus(String genus) {
        this.genus = genus;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFeature() {
        return feature;
    }

    public void setFeature(String feature) {
        this.feature = feature;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }


    public String getCreateTimeStr() {
        return createTimeStr;
    }

    public void setCreateTimeStr(String createTimeStr) {
        this.createTimeStr = createTimeStr;
    }

    public String getUserPic() {
        return userPic;
    }

    public void setUserPic(String userPic) {
        this.userPic = userPic;
    }

    public boolean isHasSupported() {
        return hasSupported;
    }

    public void setHasSupported(boolean hasSupported) {
        this.hasSupported = hasSupported;
    }
}
