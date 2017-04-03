package com.bergcomputers.bciweb.data.mappers;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;

import com.bergcomputers.bcibweb.config.Config;
import com.bergcomputers.domain.Currency;
import com.bergcomputers.domain.IBaseEntity;
import com.bergcomputers.domain.ICurrency;

public class CurrencyMapper implements IMapper<Currency> {
	public Currency fromJSON(JSONObject jsonObject) throws Exception {
		Currency currency = null;
		if (null != jsonObject) {
			currency = new Currency();
			currency.setId(jsonObject.optLong(IBaseEntity.id));
			currency.setCreationDate(Config.DATE_FORMAT_FULL.parse(jsonObject.optString(IBaseEntity.creationDate)));
			currency.setSymbol(jsonObject.getString(ICurrency.symbol));
			currency.setExchangerate(jsonObject.getDouble(ICurrency.exchangerate));
		}
		return currency;
	}

	public List<Currency> fromJSONArray(JSONArray jsonArray) throws Exception {
		List<Currency> list = null;
		if (null != jsonArray) {
			list = new ArrayList<Currency>();
			for (int i = 0; i < jsonArray.length(); i++) {
				list.add(fromJSON((JSONObject) jsonArray.get(i)));
			}
		}
		return list;
	}

}
