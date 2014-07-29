package com.travelsky.acms.resources;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;

import com.travelsky.acms.db.ConnectionManager;
import com.travelsky.acms.utils.DateUtils;

@Path("/groundServiceTicket")
public class GroundServiceTicket {
	
	@GET
	@Produces(MediaType.APPLICATION_JSON + "; charset=utf-8")
	public String get(@QueryParam("date") String dateString,
			@QueryParam("flightNumber") String flightNumber) {
		try {
			String filterCondition = " where t.aflerc != 'E' and rownum <= 1";
			Date date = DateUtils.parseDate(dateString, "yyyy-MM-dd");
			if(date != null) {
				filterCondition += " and t.aflfld = " + DateUtils.dateToString(date, "yyyyMMdd");
			}
			if(flightNumber != null && !flightNumber.trim().equals("")) {
				filterCondition += " and t.aflfln = '" + flightNumber + "'";
			}
			StringBuilder sqlBuilder = new StringBuilder();
			sqlBuilder.append("with base_record as (                   ");
			sqlBuilder.append("select t.aflfld,                        ");
			sqlBuilder.append("       t.aflfln,                        ");
			sqlBuilder.append("       t.AFLEQY as 牵引车,                 ");
			sqlBuilder.append("       t.AFLEQS as 加水车,                 ");
			sqlBuilder.append("       t.AFLEWS as 污水车,                 ");
			sqlBuilder.append("       t.AFLELJ as 垃圾车,                 ");
			sqlBuilder.append("       t.AFLEKC as 客梯使用时间,              ");
			sqlBuilder.append("       t.AFLEK1 as 客梯开始时间,              ");
			sqlBuilder.append("       t.AFLEK2 as 客梯结束时间,              ");
			sqlBuilder.append("       t.AFLESJ as 旅客用车,                ");
			sqlBuilder.append("       t.AFLEJZ as 机组用车,                ");
			sqlBuilder.append("       t.AFLEDY as 地面电源车使用时间,           ");
			sqlBuilder.append("       t.AFLED1 as 地面电源车开始时间,           ");
			sqlBuilder.append("       t.AFLED2 as 地面电源车结束时间,           ");
			sqlBuilder.append("       t.AFLEKT as 空调车,                 ");
			sqlBuilder.append("       t.AFLELC as 升降平台车使用时间,           ");
			sqlBuilder.append("       t.AFLEB1 as 升降平台车开始时间,           ");
			sqlBuilder.append("       t.AFLEB2 as 升降平台车结束时间,           ");
			sqlBuilder.append("       t.AFLEPD as 皮带传送车,               ");
			sqlBuilder.append("       t.AFLEQC as 气源车使用时间,             ");
			sqlBuilder.append("       t.AFLEQ1 as 气源车开始时间,             ");
			sqlBuilder.append("       t.AFLEQ2 as 气源车结束时间,             ");
			sqlBuilder.append("       t.AFLEG1 as 拖棒,                  ");
			sqlBuilder.append("       t.AFLEQJ as 清洁,                  ");
			sqlBuilder.append("       case when t.AFLEQW = 'Y' then '1' else null end as 勤务   ");
			sqlBuilder.append("from AIMPVCH t                                                 ");
			sqlBuilder.append(filterCondition + " ");
			sqlBuilder.append(")                                                              ");
			sqlBuilder.append("                                                               ");
			sqlBuilder.append("select '牵引车' as serviceItem,                                   ");
			sqlBuilder.append("       null as startTime,                                      ");
			sqlBuilder.append("       null as endTime,                                        ");
			sqlBuilder.append("       t.牵引车 as amount,                                        ");
			sqlBuilder.append("       '次' as unit                                             ");
			sqlBuilder.append("from base_record t                                             ");
			sqlBuilder.append("union all                                                      ");
			sqlBuilder.append("select '加水车' as serviceItem,                                   ");
			sqlBuilder.append("       null as startTime,                                      ");
			sqlBuilder.append("       null as endTime,                                        ");
			sqlBuilder.append("       t.加水车 as amount,                                        ");
			sqlBuilder.append("       '次' as unit                                             ");
			sqlBuilder.append("from base_record t                                             ");
			sqlBuilder.append("union all                                                      ");
			sqlBuilder.append("select '污水车' as serviceItem,                                   ");
			sqlBuilder.append("       null as startTime,                                      ");
			sqlBuilder.append("       null as endTime,                                        ");
			sqlBuilder.append("       t.污水车 as amount,                                        ");
			sqlBuilder.append("       '次' as unit                                             ");
			sqlBuilder.append("from base_record t                                             ");
			sqlBuilder.append("union all                                                      ");
			sqlBuilder.append("select '垃圾车' as serviceItem,                                   ");
			sqlBuilder.append("       null as startTime,                                      ");
			sqlBuilder.append("       null as endTime,                                        ");
			sqlBuilder.append("       t.垃圾车 as amount,                                        ");
			sqlBuilder.append("       '次' as unit                                             ");
			sqlBuilder.append("from base_record t                                             ");
			sqlBuilder.append("union all                                                      ");
			sqlBuilder.append("select '旅客用车' as serviceItem,                                  ");
			sqlBuilder.append("       null as startTime,                                      ");
			sqlBuilder.append("       null as endTime,                                        ");
			sqlBuilder.append("       t.旅客用车 as amount,                                       ");
			sqlBuilder.append("       '次' as unit                                             ");
			sqlBuilder.append("from base_record t                                             ");
			sqlBuilder.append("union all                                                      ");
			sqlBuilder.append("select '机组用车' as serviceItem,                                  ");
			sqlBuilder.append("       null as startTime,                                      ");
			sqlBuilder.append("       null as endTime,                                        ");
			sqlBuilder.append("       t.机组用车 as amount,                                       ");
			sqlBuilder.append("       '次' as unit                                             ");
			sqlBuilder.append("from base_record t                                             ");
			sqlBuilder.append("union all                                                      ");
			sqlBuilder.append("select '空调车' as serviceItem,                                   ");
			sqlBuilder.append("       null as startTime,                                      ");
			sqlBuilder.append("       null as endTime,                                        ");
			sqlBuilder.append("       t.空调车 as amount,                                        ");
			sqlBuilder.append("       '次' as unit                                             ");
			sqlBuilder.append("from base_record t                                             ");
			sqlBuilder.append("union all                                                      ");
			sqlBuilder.append("select '皮带传送车' as serviceItem,                                 ");
			sqlBuilder.append("       null as startTime,                                      ");
			sqlBuilder.append("       null as endTime,                                        ");
			sqlBuilder.append("       t.皮带传送车 as amount,                                      ");
			sqlBuilder.append("       '次' as unit                                             ");
			sqlBuilder.append("from base_record t                                             ");
			sqlBuilder.append("union all                                                      ");
			sqlBuilder.append("select '拖棒' as serviceItem,                                    ");
			sqlBuilder.append("       null as startTime,                                      ");
			sqlBuilder.append("       null as endTime,                                        ");
			sqlBuilder.append("       t.拖棒 as amount,                                         ");
			sqlBuilder.append("       '次' as unit                                             ");
			sqlBuilder.append("from base_record t                                             ");
			sqlBuilder.append("union all                                                      ");
			sqlBuilder.append("select '清洁' as serviceItem,                                    ");
			sqlBuilder.append("       null as startTime,                                      ");
			sqlBuilder.append("       null as endTime,                                        ");
			sqlBuilder.append("       t.清洁 as amount,                                         ");
			sqlBuilder.append("       '次' as unit                                             ");
			sqlBuilder.append("from base_record t                                             ");
			sqlBuilder.append("union all                                                      ");
			sqlBuilder.append("select '勤务' as serviceItem,                                    ");
			sqlBuilder.append("       null as startTime,                                      ");
			sqlBuilder.append("       null as endTime,                                        ");
			sqlBuilder.append("       t.勤务 as amount,                                         ");
			sqlBuilder.append("       '次' as unit                                             ");
			sqlBuilder.append("from base_record t                                             ");
			sqlBuilder.append("union all                                                      ");
			sqlBuilder.append("select '客梯' as serviceItem,                                    ");
			sqlBuilder.append("       t.客梯开始时间 as startTime,                                  ");
			sqlBuilder.append("       t.客梯结束时间 as endTime,                                    ");
			sqlBuilder.append("       t.客梯使用时间 as amount,                                     ");
			sqlBuilder.append("       '小时' as unit                                            ");
			sqlBuilder.append("from base_record t                                             ");
			sqlBuilder.append("union all                                                      ");
			sqlBuilder.append("select '升降平台车' as serviceItem,                                 ");
			sqlBuilder.append("       t.升降平台车开始时间 as startTime,                               ");
			sqlBuilder.append("       t.升降平台车结束时间 as endTime,                                 ");
			sqlBuilder.append("       t.升降平台车使用时间 as amount,                                  ");
			sqlBuilder.append("       '小时' as unit                                            ");
			sqlBuilder.append("from base_record t                                             ");
			sqlBuilder.append("union all                                                      ");
			sqlBuilder.append("select '地面电源车' as serviceItem,                                 ");
			sqlBuilder.append("       t.地面电源车开始时间 as startTime,                               ");
			sqlBuilder.append("       t.地面电源车结束时间 as endTime,                                 ");
			sqlBuilder.append("       t.地面电源车使用时间 as amount,                                  ");
			sqlBuilder.append("       '小时' as unit                                            ");
			sqlBuilder.append("from base_record t                                             ");
			sqlBuilder.append("union all                                                      ");
			sqlBuilder.append("select '气源车' as serviceItem,                                   ");
			sqlBuilder.append("       t.气源车开始时间 as startTime,                                 ");
			sqlBuilder.append("       t.气源车结束时间 as endTime,                                   ");
			sqlBuilder.append("       t.气源车使用时间 as amount,                                    ");
			sqlBuilder.append("       '小时' as unit                                            ");
			sqlBuilder.append("from base_record t                                             ");
			Connection conn = ConnectionManager.getConnection();
			Statement stmt = conn.createStatement();
			String sql = sqlBuilder.toString();
			System.out.println(sql);
			ResultSet resultSet = stmt.executeQuery(sql);
			JSONObject object = new JSONObject();
			object.put("success", true);
			JSONArray array = new JSONArray();
			while(resultSet.next()) {
				String serviceItem = resultSet.getString("serviceItem");
				Date startTime = DateUtils.parseDate(
						resultSet.getString("startTime"), "yyyyMMddHHmmss");
				Date endTime = DateUtils.parseDate(
						resultSet.getString("endTime"), "yyyyMMddHHmmss");
				String amount = resultSet.getString("amount");
				String unit = resultSet.getString("unit");
				
				JSONObject item = new JSONObject();
				item.put("serviceItem", serviceItem);
				item.put("startTime", startTime != null ?
						DateUtils.dateToString(startTime, "yyyy-MM-dd HH:mm:ss") : null);
				item.put("endTime", endTime != null ?
						DateUtils.dateToString(endTime, "yyyy-MM-dd HH:mm:ss") : null);
				item.put("amount", amount);
				item.put("unit", unit);
				array.put(item);
			}
			object.put("serviceItemList", array);
			resultSet.close();
			stmt.close();
			return object.toString();
		} catch(Exception e) {
			e.printStackTrace();
			return "{\"success\": false}";
		}
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON + "; charset=utf-8")
	public String add(@QueryParam("date") String dateString,
			@QueryParam("flightNumber") String flightNumber,
			@QueryParam("serviceItem") String serviceItem,
			@QueryParam("startTime") String startTimeString,
			@QueryParam("endTime") String endTimeString,
			@QueryParam("amount") int amount) {
		try {
			return "{\"success\": true}";
		} catch(Exception e) {
			e.printStackTrace();
			return "{\"success\": false}";
		}
	}
	
	@PUT
	@Produces(MediaType.APPLICATION_JSON + "; charset=utf-8")
	public String update(@QueryParam("date") String dateString,
			@QueryParam("flightNumber") String flightNumber,
			@QueryParam("serviceItem") String serviceItem,
			@QueryParam("startTime") String startTimeString,
			@QueryParam("endTime") String endTimeString,
			@QueryParam("amount") int amount) {
		try {
			return "{\"success\": true}";
		} catch(Exception e) {
			e.printStackTrace();
			return "{\"success\": false}";
		}
	}
	
	@DELETE
	@Produces(MediaType.APPLICATION_JSON + "; charset=utf-8")
	public String delete(@QueryParam("date") String dateString,
			@QueryParam("flightNumber") String flightNumber,
			@QueryParam("serviceItem") String serviceItem) {
		try {
			return "{\"success\": true}";
		} catch(Exception e) {
			e.printStackTrace();
			return "{\"success\": false}";
		}
	}
}
