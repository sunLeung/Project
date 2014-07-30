package bean;

import java.sql.Timestamp;

import utils.Def;
import utils.UUID;

/**
 * @author L
 *
 */
public class PayBean extends Pojo{
	private long uuid;
	private int loginid;
	private String serverid;
	/**平台自动订单号*/
	private long orderid;
	/**游戏方订单号*/
	private String cporderid;
	/**渠道方订单号*/
	private String sporderid;
	private float money;
	private String moneyType;
	private String unionid;
	private Timestamp insertTime;
	private String remark;
	private int isOk;
	
	public PayBean(){
		
	}
	
	public PayBean(UserBean user,String cporderid,String sporderid,float money,String moneyType,String unionid,String remark){
		this.isOk=Def.PaySucceed;
		this.orderid=UUID.genOrderid();
		this.insertTime=new Timestamp(System.currentTimeMillis());
		this.uuid=user.getUuid();
		this.loginid=user.getLoginid();
		this.serverid=user.getServerid();
		this.cporderid=cporderid;
		this.sporderid=sporderid;
		this.money=money;
		this.moneyType=moneyType;
		this.unionid=unionid;
		this.remark=remark;
	}

	public long getUuid() {
		return uuid;
	}

	public void setUuid(long uuid) {
		this.uuid = uuid;
	}

	public int getLoginid() {
		return loginid;
	}

	public void setLoginid(int loginid) {
		this.loginid = loginid;
	}

	public String getServerid() {
		return serverid;
	}

	public void setServerid(String serverid) {
		this.serverid = serverid;
	}

	public long getOrderid() {
		return orderid;
	}

	public void setOrderid(long orderid) {
		this.orderid = orderid;
	}

	public String getCporderid() {
		return cporderid;
	}

	public void setCporderid(String cporderid) {
		this.cporderid = cporderid;
	}

	public float getMoney() {
		return money;
	}

	public void setMoney(float money) {
		this.money = money;
	}

	public String getMoneyType() {
		return moneyType;
	}

	public void setMoneyType(String moneyType) {
		this.moneyType = moneyType;
	}

	public String getUnionid() {
		return unionid;
	}

	public void setUnionid(String unionid) {
		this.unionid = unionid;
	}

	public Timestamp getInsertTime() {
		return insertTime;
	}

	public void setInsertTime(Timestamp insertTime) {
		this.insertTime = insertTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public int getIsOk() {
		return isOk;
	}

	public void setIsOk(int isOk) {
		this.isOk = isOk;
	}

	public String getSporderid() {
		return sporderid;
	}

	public void setSporderid(String sporderid) {
		this.sporderid = sporderid;
	}
	
}
