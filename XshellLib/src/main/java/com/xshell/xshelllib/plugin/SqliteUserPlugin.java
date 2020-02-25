//package com.xinyusoft.xshelllib.plugin;
//
//import com.alibaba.fastjson.JSON;
//import com.xinyusoft.xshelllib.sqlite.UnReadMessage;
//import com.xinyusoft.xshelllib.sqlite.UnReadMessageNum;
//import com.xinyusoft.xshelllib.sqlite.UserBean;
//import com.xinyusoft.xshelllib.sqlite.UserService;
//
//import org.apache.cordova.CallbackContext;
//import org.apache.cordova.CordovaPlugin;
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.util.ArrayList;
//
//public class SqliteUserPlugin extends CordovaPlugin {
//	@Override
//	public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
//		try {
//			if ("Update".equals(action)) {// 更新
//				String jsonString = args.getString(0);
//				JSONArray jsonArray = new JSONArray(jsonString);
//				UserService us = new UserService(cordova.getActivity(),"ab");
//				for (int i = 0; i < jsonArray.length(); i++) {
//					JSONObject json = jsonArray.getJSONObject(i);
//					UserBean ub = us.find(json.getString("taskGoal"));
//					if (ub == null) {
//						UserBean user = new UserBean();
//						user = us.jsonToUserBean(json, ub);
//						us.save(user);
//					} else {
//						UserBean user = new UserBean();
//						user = us.jsonToUserBean(json, ub);
//						us.update(user);
//					}
//				}
//				callbackContext.success("1");
//				return true;
//			} else if ("DeleteFramTaskGoal".equals(action)) {// 未读数量清0
//				JSONObject json = args.getJSONObject(0);
//				String taskGoal = json.getString("taskGoal");
//				UserService us = new UserService(cordova.getActivity());
//				int i = us.updateFramTaskGoal(taskGoal);
//				callbackContext.success(i);
//				return true;
//			} else if ("SelectFramTaskGoalToUrm".equals(action)) {// 查询未读数量
//				JSONObject json = args.getJSONObject(0);
//				UserService us = new UserService(cordova.getActivity());
//				UserBean ub = us.find(json.getString("taskGoal"));
//				callbackContext.success(ub.getUnReadMessageNum());
//				return true;
//
//			} else if ("SelectToLastMessage".equals(action)) {// 查询最后一条信息内容
//				JSONObject json = args.getJSONObject(0);
//				UserService us = new UserService(cordova.getActivity());
//				UserBean ub = us.find(json.getString("taskGoal"));
//				callbackContext.success(ub.getLastMessage());
//				return true;
//
//			} else if ("SelectToSomeMessage".equals(action)) {// 查询多个为读数量
//
//				JSONObject json = args.getJSONObject(0);
//				String sarray = json.getString("taskGoalList");
//				JSONArray jarray = new JSONArray(sarray);
//				UserService us = new UserService(cordova.getActivity());
//				UnReadMessageNum urmn = new UnReadMessageNum();
//				urmn.setUnReadMessageNumList(new ArrayList<UnReadMessage>());
//				for (int i = 0; i < jarray.length(); i++) {
//					JSONObject obj = jarray.getJSONObject(i);
//					try {
//						String taskGoal = obj.getString("taskGoal");
//						UserBean ub = us.find(taskGoal);
//						if (ub != null) {
//							UnReadMessage urm = new UnReadMessage();
//							urm.setTaskGoal(ub.getTaskGoal());
//							urm.setUnReadMessageNum(ub.getUnReadMessageNum());
//							urmn.getUnReadMessageNumList().add(urm);
//						} else {
//							UnReadMessage urm = new UnReadMessage();
//							urm.setTaskGoal(taskGoal);
//							urm.setUnReadMessageNum(0);
//							urmn.getUnReadMessageNumList().add(urm);
//						}
//					} catch (Exception e) {
//						// TODO: handle exception
//					}
//				}
//				String returnjson = JSON.toJSONString(urmn);
//				callbackContext.success(returnjson);
//			}
//		} catch (Exception e) {
//			// TODO: handle exception
//			callbackContext.success(action + "方法参数格式不正确。。");
//			return false;
//		}
//		return false;
//	}
//}
