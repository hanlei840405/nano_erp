package com.nano.service.productmapping;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.nano.domain.productmapping.TaoBaoProduct;

/**
 * Created by Administrator on 2015/9/26.
 */
public interface TaoBaoProductService {
List<TaoBaoProduct> fetchData(Map<String,String> fetchParams);
	
	boolean onSale(List<TaoBaoProduct> taoBaoProducts,String user);

	boolean inStock(List<TaoBaoProduct> taoBaoProducts,String user);

	boolean syncStock(List<TaoBaoProduct> taoBaoProducts,String user);

	TaoBaoProduct get(String id);

	TaoBaoProduct get(TaoBaoProduct taoBaoProduct);

	List<TaoBaoProduct> find(TaoBaoProduct taoBaoProduct);

	PageInfo<TaoBaoProduct> find(TaoBaoProduct taoBaoProduct, int pageNo, int pageSize);

	TaoBaoProduct save(TaoBaoProduct taoBaoProduct);
	
	List<TaoBaoProduct> save(List<TaoBaoProduct> taoBaoProducts);

	TaoBaoProduct update(TaoBaoProduct taoBaoProduct);

	TaoBaoProduct delete(String id,String user);

	List<TaoBaoProduct> delete(List<String> ids,String user);
}
