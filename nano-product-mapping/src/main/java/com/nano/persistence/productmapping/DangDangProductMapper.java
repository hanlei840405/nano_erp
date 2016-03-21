package com.nano.persistence.productmapping;

import java.util.List;

import org.apache.ibatis.session.RowBounds;

import com.nano.domain.productmapping.DangDangProduct;

public interface DangDangProductMapper {
	DangDangProduct findOne(String id);
	
	List<DangDangProduct> findMany(DangDangProduct dangDangProduct);
	
	List<DangDangProduct> findMany(DangDangProduct dangDangProduct,RowBounds rowBounds);

	void saveOne(DangDangProduct dangDangProduct);
	
	Long updateOne(DangDangProduct dangDangProduct);
	
	Long deleteOne(DangDangProduct dangDangProduct);
	
	List<DangDangProduct> deleteMany(List<DangDangProduct> dangDangProducts);
}
