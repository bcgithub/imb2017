package com.bergcomputers.bcibweb.delegate;

import java.util.List;
import java.util.logging.Logger;

import com.bergcomputers.bcibweb.config.Config;
import com.bergcomputers.bciweb.data.mappers.CurrencyMapper;
import com.bergcomputers.bciweb.data.mappers.IMapper;
import com.bergcomputers.domain.Currency;

public class CurrencyDelegate extends BaseDelegate<Currency>{
	private final static Logger logger = Logger.getLogger(CurrencyDelegate.class.getName());

	private IMapper<Currency> mapper;
	public CurrencyDelegate(String baseUrl) {
		super(baseUrl);
	}

	public List<Currency> getCurrencies() throws Exception{
		return getList(baseUrl + Config.REST_SERVICE_CURRENCIES_LIST);
	}

	@Override
	public IMapper<Currency> getMapper() {
		if (null == mapper){
			mapper = new CurrencyMapper();
		}
		return mapper;
	}
}
