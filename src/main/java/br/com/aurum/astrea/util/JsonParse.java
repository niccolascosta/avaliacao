package br.com.aurum.astrea.util;

import java.lang.reflect.Type;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.Gson;

public class JsonParse {

	public static Object convertJsonToObject(String json, Class<?> objectClass) {
		if (StringUtils.isEmpty(json)) {
			return null;
		}
		return new Gson().fromJson(json, objectClass);
	}

	public static Object convertJsonToObject(String json, Type objectType) {
		if (StringUtils.isEmpty(json)) {
			return null;
		}
		return new Gson().fromJson(json, objectType);
	}

	public static String ObjectToJson(Object object) {
		if (object == null) {
			return null;
		}
		return new Gson().toJson(object);
	}
}
