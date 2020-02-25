//package com.xshell.xshelllib.plugin;
//
//import com.alibaba.fastjson.JSON;
//import com.xshell.xshelllib.sqlite.UnReadMessage;
//import com.xshell.xshelllib.sqlite.UnReadMessageNum;
//import com.xshell.xshelllib.sqlite.UserBean;
//import com.xshell.xshelllib.sqlite.UserService;
//import com.xshell.xshelllib.tools.socketutil.SocketUtil;
//
//import org.apache.cordova.CallbackContext;
//import org.apache.cordova.CordovaPlugin;
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.util.ArrayList;
//
//public class DataManagementPlugin extends CordovaPlugin {
//    @Override
//    public boolean execute(String action, JSONArray args,
//                           CallbackContext callbackContext) throws JSONException {
//        // TODO Auto-generated method stub
//
//        try {
//            if ("update".equals(action)) {// 更新
//                String jsonString = args.getString(0);
//                UserService us = new UserService(cordova.getActivity(), "datamanage");
//                /*JSONArray jsonArray = new JSONArray(jsonString);
//				for (int i = 0; i < jsonArray.length(); i++) {
//					JSONObject json = jsonArray.getJSONObject(i);*/
//                JSONObject json = new JSONObject(jsonString);
//                UserBean ub = us.find(json.getString("url"));
//                if (ub == null) {
//                    UserBean user = new UserBean();
//                    ub = new UserBean();
//                    user = us.jsonToUserBean(json, ub);
//                    us.save(user);
//                } else {
//                    UserBean user = new UserBean();
//                    user = us.jsonToUserBean(json, ub);
//                    us.update(user);
//                }
//				/*}*/
//                callbackContext.success("1");
//                return true;
//            } else if ("dataRequest".equals(action)) {// 普通存储
//                UserService us = new UserService(cordova.getActivity(), "datamanage");
//                String requesturl = args.getString(0);
//                String socketurl = args.getString(1);
//                requesturl = requesturl + "?" + socketurl + "&onlymd5=Y&requestseq=aaaa";
//                socketurl = socketurl.replace("command", "cmd") + "&requestseq=aaaa";
//                String callback = args.getString(2);
//                UserBean ub = null;
//                String strmd5 = "";
//                if (null != us.find(requesturl)) { //数据库存在该项
//                    ub = us.find(requesturl);
//                    strmd5 = us.getMd5(requesturl);
//                    if (ub.getMd5().equals(strmd5)) {//md5值相同
//
//                    } else {                         //md5值不相同
//                        if (SocketUtil.hasConnected()) {
//                            us.websocketrequest(socketurl, callback, us, ub, webView);
//                        }
//                    }
//                } else { //数据库不存在该项
//                    ub = new UserBean();
//                    strmd5 = us.getMd5(requesturl);
//                    ub.setMd5(strmd5);
//                    us.websocketrequest(socketurl, callback, us, ub, webView);
//
//                }
//                return true;
//            }/*else if ("dataRequest".equals(action)) {// 普通存储
//				JSONObject json = args.getJSONObject(0);
//				UserService us = new UserService(cordova.getActivity(),"datamanage");
//				String requesturl = json.getString("url");
//				String socketurl = json.getString("socketurl");
//				UserBean ub = null;
//				String strmd5 = "";
//				if (null != us.find(requesturl)) { //数据库存在该项
//					ub = us.find(requesturl);
//					strmd5 = us.getMd5(requesturl);
//					if (ub.getMd5().equals(strmd5)){//md5值相同
//
//					}else{                         //md5值不相同
//						//String result = us.getData(requesturl);
//						String result = us.websocketrequest(socketurl);
//						ub = us.jsonToUserBean(new JS ONObject(result),ub);
//						us.update(ub);
//					}
//				}else {                           //数据库不存在该项
//					ub = new UserBean();
//					//String result = us.getData(requesturl);
//					String result = us.websocketrequest(socketurl);
//					ub = us.jsonToUserBean(new JSONObject(result),ub);
//					us.update(ub);
//				}
//				callbackContext.success(ub.getRequestcontent());
//				return true;
//			}*/ else if ("DeleteFramTaskGoal".equals(action)) {// 未读数量清0
//                JSONObject json = args.getJSONObject(0);
//                String url = json.getString("url");
//                UserService us = new UserService(cordova.getActivity(), "datamanage");
//                int i = us.updateFramUrl(url);
//                callbackContext.success(i);
//                return true;
//            } else if ("SelectToLastMessage".equals(action)) {// 查询最后一条信息内容
//                JSONObject json = args.getJSONObject(0);
//                UserService us = new UserService(cordova.getActivity(), "datamanage");
//                UserBean ub = us.find(json.getString("url"));
//                //callbackContext.success(ub.getLastMessage());
//                return true;
//
//            } else if ("SelectToSomeMessage".equals(action)) {// 查询多个为读数量
//
//                JSONObject json = args.getJSONObject(0);
//                String sarray = json.getString("taskGoalList");
//                JSONArray jarray = new JSONArray(sarray);
//                UserService us = new UserService(cordova.getActivity(), "datamanage");
//                UnReadMessageNum urmn = new UnReadMessageNum();
//                urmn.setUnReadMessageNumList(new ArrayList<UnReadMessage>());
//                for (int i = 0; i < jarray.length(); i++) {
//                    JSONObject obj = jarray.getJSONObject(i);
//                    try {
//                        String taskGoal = obj.getString("url");
//                        UserBean ub = us.find(taskGoal);
//                        if (ub != null) {
//                            UnReadMessage urm = new UnReadMessage();
//                            //urm.setTaskGoal(ub.getTaskGoal());
//                            //urm.setUnReadMessageNum(ub.getUnReadMessageNum());
//                            urmn.getUnReadMessageNumList().add(urm);
//                        } else {
//                            UnReadMessage urm = new UnReadMessage();
//                            urm.setTaskGoal(taskGoal);
//                            urm.setUnReadMessageNum(0);
//                            urmn.getUnReadMessageNumList().add(urm);
//                        }
//                    } catch (Exception e) {
//                        // TODO: handle exception
//                    }
//                }
//                String returnjson = JSON.toJSONString(urmn);
//                callbackContext.success(returnjson);
//            }
//        } catch (Exception e) {
//            // TODO: handle exception
//            callbackContext.success(action + "方法参数格式不正确。。");
//            return false;
//        }
//        return false;
//    }
//}
