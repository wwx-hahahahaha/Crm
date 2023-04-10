package com.rose.workbench.domain;

public class TranHistory {
	
	private String id;
	private String stage;
	private String money;
	private String expectedDate;
	private String createTime;
	private String createBy;
	private String tranId;

	//交易这条线索,添加到这个里面,方便传到前端
	private String Kn;

	public String getKn() {
		return Kn;
	}

	public void setKn(String kn) {
		Kn = kn;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getStage() {
		return stage;
	}
	public void setStage(String stage) {
		this.stage = stage;
	}
	public String getMoney() {
		return money;
	}
	public void setMoney(String money) {
		this.money = money;
	}
	public String getExpectedDate() {
		return expectedDate;
	}
	public void setExpectedDate(String expectedDate) {
		this.expectedDate = expectedDate;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getCreateBy() {
		return createBy;
	}
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}
	public String getTranId() {
		return tranId;
	}
	public void setTranId(String tranId) {
		this.tranId = tranId;
	}

	@Override
	public String toString() {
		return "TranHistory{" +
				"id='" + id + '\'' +
				", stage='" + stage + '\'' +
				", money='" + money + '\'' +
				", expectedDate='" + expectedDate + '\'' +
				", createTime='" + createTime + '\'' +
				", createBy='" + createBy + '\'' +
				", tranId='" + tranId + '\'' +
				", Kn='" + Kn + '\'' +
				'}';
	}
}
