package com.keita.pinganautoinsurance;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.app.Application;
import com.keita.painganautoinsurance.entity.Template;
/*
 * 全局应用类 储存全局变量
 */
public class MyApplication extends Application {
	private ArrayList<String> carNoList = null;
	private ArrayList<String> caseReasonList = null;
	private ArrayList<String> accidentList = null;
	private ArrayList<String> carTypeList = null;
	private ArrayList<String> carLossList = null;
	private ArrayList<String> accidentDetialList = null;
	private ArrayList<Template> templateList = null;
	private ArrayList<Activity> activityList = null;
	private SimpleDateFormat dateformat = null;


	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		activityList = new ArrayList<Activity>();
		// 车牌数据源
		String[] carNoArray = { "粤", "京", "津", "沪", "渝", "冀", "豫", "云",
				"辽", "黑", "湘", "皖", "鲁", "新", "苏", "浙", "赣", "鄂", "桂", "甘" };
		String[] caseReasonArray = { "碰撞", "倾覆", "火灾", "爆炸", "自燃", "盗抢",
				"外界物体坠落", "外界物体倒塌", "雷击", "暴风", "暴雨", "洪水", "雹灾", "玻璃延烧",
				"行驶中玻璃爆裂", "被砸" };
		String[] accidentReasonArray = { "制动失灵", "转向失灵", "其它机械故障",
				"疲劳驾驶", "闯红灯", "逆向行驶", "安全距离不够", "超速行驶", "违章装载", "违章并线",
				"其它违章行驶", "疏忽大意、措施失当" };
		String[] carTypeArray = { "丰田", "本田", "大众", "奥迪", "奔驰", "宝马",
				"斯巴鲁", "雪铁龙", "日产" };
		String[] carLossArray = { "左前灯", "右前灯", "左尾灯", "右尾灯", "前杠", "车顶",
				"左前胎", "右前胎", "左后胎", "右后胎" ,"挡风玻璃"};
		String[] accidentDetailArray = {"驾驶员于00时00分，从XX出发,准备XX,行至事发地点时，因上述事故原因，于上述时间立即报交警和保险"};
		//格式化时间
		dateformat =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//添加默认情景模版
		Template template = new Template();
		template.setTemplateName("默认模板");
		template.setLicence("441723231300000");
		template.setHurtNum("0");
		template.setDeadNum("0");
		template.setPeopleName("李四");
		template.setPhone("1380000000");
		template.setCarNo("粤A0000");
		template.setCarType(carTypeArray[0]);
		template.setCarReason(caseReasonArray[0]);
		template.setCaseLoss(carLossArray[0]);
		template.setAccidentReason(accidentReasonArray[0]);
		template.setAccidentDetail(accidentDetailArray[0]);
		template.setDate(dateformat.format(new Date()));

		carNoList = new ArrayList<String>();
		caseReasonList = new ArrayList<String>();
		accidentList = new ArrayList<String>();
		carTypeList = new ArrayList<String>();
		carLossList = new ArrayList<String>();
		accidentDetialList =  new ArrayList<String>();
		templateList =  new ArrayList<Template>(); 
		templateList.add(template);
		
		for (String str : carNoArray) {
			carNoList.add(str);
		}

		for (String str : caseReasonArray) {
			caseReasonList.add(str);
		}

		for (String str : accidentReasonArray) {
			accidentList.add(str);
		}
		for (String str : carTypeArray) {
			carTypeList.add(str);
		}
		for (String str : carLossArray) {
			carLossList.add(str);
		}
		for (String str : accidentDetailArray) {
			accidentDetialList.add(str);
		}

	}

	public ArrayList<Activity> getActivityList() {
		return activityList;
	}

	public void setActivityList(ArrayList<Activity> activityList) {
		this.activityList = activityList;
	}

	public ArrayList<String> getCarNoList() {
		return carNoList;
	}

	public void setCarNoList(ArrayList<String> carNoList) {
		this.carNoList = carNoList;
	}

	public ArrayList<String> getCaseReasonList() {
		return caseReasonList;
	}

	public void setCaseReasonList(ArrayList<String> caseReasonList) {
		this.caseReasonList = caseReasonList;
	}

	public ArrayList<String> getAccidentList() {
		return accidentList;
	}

	public void setAccidentList(ArrayList<String> accidentList) {
		this.accidentList = accidentList;
	}

	public ArrayList<String> getCarTypeList() {
		return carTypeList;
	}

	public void setCarTypeList(ArrayList<String> carTypeList) {
		this.carTypeList = carTypeList;
	}

	public ArrayList<String> getCarLossList() {
		return carLossList;
	}

	public void setCarLossList(ArrayList<String> carLossList) {
		this.carLossList = carLossList;
	}

	public ArrayList<String> getAccidentDetialList() {
		return accidentDetialList;
	}

	public void setAccidentDetialList(ArrayList<String> accidentDetialList) {
		this.accidentDetialList = accidentDetialList;
	}
	public ArrayList<Template> getTemplateList() {
		return templateList;
	}

	public void setTemplateList(ArrayList<Template> templateList) {
		this.templateList = templateList;
	}
}
