package com.nano.service.productmapping;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.nano.domain.productmapping.DangDangProduct;

/**
 * Created by Administrator on 2015/9/26.
 */
public interface DangDangProductService {
	List<DangDangProduct> fetchData(Map<String,String> fetchParams);
	
	boolean onSale(List<DangDangProduct> dangDangProducts,String user);

	boolean inStock(List<DangDangProduct> dangDangProducts,String user);

	boolean syncStock(List<DangDangProduct> dangDangProducts,String user);

	DangDangProduct get(String id);

	DangDangProduct get(DangDangProduct dangDangProduct);

	List<DangDangProduct> find(DangDangProduct dangDangProduct);

	PageInfo<DangDangProduct> find(DangDangProduct dangDangProduct, int pageNo, int pageSize);

	DangDangProduct save(DangDangProduct dangDangProduct);
	
	List<DangDangProduct> save(List<DangDangProduct> dangDangProducts);

	DangDangProduct update(DangDangProduct dangDangProduct);

	DangDangProduct delete(String id,String user);

	List<DangDangProduct> delete(List<String> ids,String user);
}
