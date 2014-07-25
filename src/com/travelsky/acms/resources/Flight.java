package com.travelsky.acms.resources;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;

import com.travelsky.acms.db.ConnectionManager;
import com.travelsky.acms.utils.DateUtils;

@Path("/flight")
public class Flight {
	
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON + "; charset=utf-8")
	public String get(@PathParam("id") String id) {
		try {
			JSONObject json = new JSONObject();
			json.put("success", true);
			json.put("id", id);
			return json.toString();
		} catch(Exception e) {
			e.printStackTrace();
			return "{\"success\": false}";
		}
	}
	
	@GET
	@Path("/search/")
	@Produces(MediaType.APPLICATION_JSON + "; charset=utf-8")
	public String search(
			@QueryParam("limit") int limit, 
			@QueryParam("page") int page,
            @QueryParam("date") String dateString,
            @QueryParam("flightNumber") String flightNumber) {
		try {
			String filterCondition = " where t.aflerc != 'E'";
			Date date = DateUtils.parseDate(dateString, "yyyy-MM-dd");
			if(date != null) {
				filterCondition += " and t.aflfld = " + DateUtils.dateToString(date, "yyyyMMdd");
			}
			if(flightNumber != null && !flightNumber.trim().equals("")) {
				filterCondition += " and t.aflfln = '" + flightNumber + "'";
			}
			limit = limit > 0 ? limit : 20;
			page = page > 0 ? page : 1;
			int start = (page - 1) * limit;
			int end = page * limit;
			Connection conn = ConnectionManager.getConnection();
			Statement stmt = conn.createStatement();
			String countSql = "select count(*) from aimpflt t" + filterCondition;
			System.out.println(countSql);
			ResultSet countSet = stmt.executeQuery(countSql);
			countSet.next();
			int total = countSet.getInt(1);
			String listSql = "select * from ( "
						+ " select t.*, rownum as rn from aimpflt t "
						+ filterCondition
						+ " and rownum <= " + end
						+ " ) t "
						+ " where t.rn > " + start;
			System.out.println(listSql);
			ResultSet listSet = stmt.executeQuery(listSql);
			JSONObject json = new JSONObject();
			json.put("success", true);
			json.put("total", total);
			json.put("limit", limit);
			json.put("page", page);
			JSONArray array = new JSONArray();
			while(listSet.next()) {
				String actionIdentity = listSet.getString("afltol");
				if("A".equals(actionIdentity) || "D".equals(actionIdentity)) {
					Date time = "A".equals(actionIdentity)
							? DateUtils.parseDate(listSet.getString("aflltm"), "yyyyMMddHHmmss")
							: DateUtils.parseDate(listSet.getString("aflttm"), "yyyyMMddHHmmss");
					JSONObject flight = new JSONObject();
					flight.put("flightNumber", 
							listSet.getObject("aflfln") != null ? listSet.getObject("aflfln") : "");
					flight.put("planeNumber", 
							listSet.getObject("aflacc") != null ? listSet.getObject("aflacc") : "");
					flight.put("planeType", 
							listSet.getObject("aflcrt") != null ? listSet.getObject("aflcrt") : "");
					flight.put("airCode",
							listSet.getObject("aflacd") != null ? listSet.getObject("aflacd") : "");
					flight.put("airName", 
							listSet.getObject("aflanm") != null ? listSet.getObject("aflanm") : "");
					flight.put("flightNature", 
							listSet.getObject("aflflt") != null ? listSet.getObject("aflflt") : "");
					flight.put("action", "A".equals(actionIdentity) ? "land" : "takeoff");
					flight.put("time", DateUtils.dateToString(time, "yyyy-MM-dd HH:mm:ss"));
					array.put(flight);
				}
			}
			json.put("flightList", array);
			countSet.close();
			listSet.close();
			stmt.close();
			return json.toString();
		} catch(Exception e) {
			e.printStackTrace();
			return "{\"success\": false}";
		}
	}
}
