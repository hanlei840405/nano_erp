package com.nano.service.base.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nano.domain.base.ShopProperty;
import com.nano.exception.NanoDBConsistencyRuntimeException;
import com.nano.exception.index.ExceptionIndex;
import com.nano.persistence.base.ShopPropertyMapper;
import com.nano.service.base.ShopPropertyService;

@Service
public class ShopPropertyServiceImpl implements ShopPropertyService{

	@Autowired
	private ShopPropertyMapper shopPropertyMapper;
	
	public ShopProperty findOne(String id) {
		return shopPropertyMapper.findOne(id);
	}

	public List<ShopProperty> find(ShopProperty shopProperty) {
		return shopPropertyMapper.findMany(shopProperty);
	}

	@Transactional
	public ShopProperty save(ShopProperty shopProperty) {
		shopPropertyMapper.saveOne(shopProperty);
		return shopProperty;
	}

	@Transactional
	public List<ShopProperty> save(List<ShopProperty> shopPropertys) {
		shopPropertyMapper.saveMany(shopPropertys);
		return shopPropertys;
	}

	@Transactional
	public void update(List<ShopProperty> inserts,List<ShopProperty> updates,List<String> deletes) {
		for(ShopProperty shopProperty : inserts){
			save(shopProperty);
		}
		for(ShopProperty shopProperty : updates){
			shopPropertyMapper.updateOne(shopProperty);
		}
		for(String id : deletes){
			delete(id);
		}
	}

	@Transactional
	public void delete(String id) {
		ShopProperty shopProperty = findOne(id);
		long resultCnt = shopPropertyMapper.deleteOne(shopProperty);
		if(resultCnt == 0){
			throw new NanoDBConsistencyRuntimeException(String.format(
					ExceptionIndex.DB_TRANSACTION_CONSISTENCY_EXCEPTION,
					shopProperty.getId() + " : " + shopProperty.getCode()));
		}
	}

	@Transactional
	public void deleteByParent(String parentId) {
		shopPropertyMapper.deleteByParent(parentId);
	}

}
