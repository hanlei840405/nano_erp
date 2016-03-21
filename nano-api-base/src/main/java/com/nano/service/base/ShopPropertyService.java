package com.nano.service.base;

import java.util.List;

import com.nano.domain.base.ShopProperty;

public interface ShopPropertyService {

	ShopProperty findOne(String id);

	List<ShopProperty> find(ShopProperty shopProperty);

	ShopProperty save(ShopProperty shopProperty);
	
	List<ShopProperty> save(List<ShopProperty> shopPropertys);

	void update(List<ShopProperty> inserts,List<ShopProperty> updates,List<String> deletes);
	
	void delete(String id);
	
	void deleteByParent(String parentId);
}
