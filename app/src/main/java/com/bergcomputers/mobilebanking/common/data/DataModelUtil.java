package com.bergcomputers.mobilebanking.common.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.bergcomputers.mobilebanking.common.Util;
import com.bergcomputers.mobilebanking.common.activity.ContextUtil;

public final class DataModelUtil {
	public static final String DATAEN = "dataen";

	public static final String STATUS = "status";
	public static final String STATUS_SUCCESS = "success";
	public static final String STATUS_ERROR = "error";
	public static final String ERROR_CODE = "code";
	public static final String ERROR = "error";
	public static final String ERROR_DETAILS = "error_details";
	public static final String DOMAIN = "domain";

	public static final String RESPONSE = "response";


	public static final String JUNK_DATA = "junk_data";

	private DataModelUtil() {
	}

	/**
	 * @param pJSONString
	 *            json object
	 * @return error code or -1 for success
	 * @throws JSONException
	 */
	public static int handleJSONResponse(String pJSONString) {
		try {
			JSONObject jsonObject = new JSONObject(pJSONString);
			String status = jsonObject.getString(STATUS);
			if (status.equals(STATUS_SUCCESS)) {
				return -1;
			}
			// status error
			return jsonObject.getInt(ERROR_CODE);
		} catch (JSONException e) {
			// unable to read the status
			return 0;
		}
	}


	private static String getNonMandatoryStringProperty(JSONObject json,
			String property) {
		try {
			String result = json.getString(property);
			if ("null".equals(result)) {
				return null;
			}
			return result;
		} catch (JSONException e) {
			return null;
		}
	}

	private static JSONObject getNonMandatoryProperty(JSONObject json,
			String property) {
		try {
			return json.getJSONObject(property);
		} catch (JSONException e) {
			return null;
		}
	}

	private static Integer getNonMandatoryIntProperty(JSONObject json,
			String property) {
		try {
			return json.getInt(property);
		} catch (JSONException e) {
			return null;
		}
	}

	private static Long getNonMandatoryLongProperty(JSONObject json,
			String property) {
		try {
			return json.getLong(property);
		} catch (JSONException e) {
			return null;
		}
	}

	private static Double getNonMandatoryDoubleProperty(JSONObject json,
			String property) {
		try {
			return json.getDouble(property);
		} catch (JSONException e) {
			return null;
		}
	}

	private static Date getDateFromMillisProperty(JSONObject json,
			String property) {
		try {
			long millis = json.getLong(property);
			return Util.getDate(millis);
		} catch (JSONException e) {
			return null;
		}
	}




}
