package com.nano.service.productmapping;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.nano.domain.productmapping.JingDongProduct;

/**
 * Created by Administrator on 2015/9/26.
 */
public interface JingDongProductService {
	List<JingDongProduct> fetchData(Map<String,String> fetchParams);
	
	boolean onSale(List<JingDongProduct> jingDongProducts,String user);

	boolean inStock(List<JingDongProduct> jingDongProducts,String user);

	boolean syncStock(List<JingDongProduct> jingDongProducts,String user);

	JingDongProduct get(String id);

	JingDongProduct get(JingDongProduct jingDongProduct);

	List<JingDongProduct> find(JingDongProduct jingDongProduct);

	PageInfo<JingDongProduct> find(JingDongProduct jingDongProduct, int pageNo, int pageSize);

	JingDongProduct save(JingDongProduct jingDongProduct);
	
	List<JingDongProduct> save(List<JingDongProduct> jingDongProducts);

	JingDongProduct update(JingDongProduct jingDongProduct);

	JingDongProduct delete(String id,String user);

	List<JingDongProduct> delete(List<String> ids,String user);
}
