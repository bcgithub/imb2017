package com.bergcomputers.bcibweb.managedbeans;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import com.bergcomputers.bcibweb.config.Config;
import com.bergcomputers.bcibweb.delegate.CurrencyDelegate;
import com.bergcomputers.domain.Currency;



@ManagedBean
@RequestScoped
public class CurrencyBean extends BaseBean{


	public CurrencyBean(){
		super();
	}

	public List<Currency> getCurrencies() throws Exception{
		return new CurrencyDelegate(Config.REST_SERVICE_BASE_URL).getCurrencies();
	}
}
