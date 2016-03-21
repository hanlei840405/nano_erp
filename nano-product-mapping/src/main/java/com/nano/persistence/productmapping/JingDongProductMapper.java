package com.nano.persistence.productmapping;

import java.util.List;

import org.apache.ibatis.session.RowBounds;

import com.nano.domain.productmapping.JingDongProduct;

public interface JingDongProductMapper {
	JingDongProduct findOne(String id);
	
	List<JingDongProduct> findMany(JingDongProduct jingDongProduct);
	
	List<JingDongProduct> findMany(JingDongProduct jingDongProduct,RowBounds rowBounds);

	void saveOne(JingDongProduct jingDongProduct);
	
	Long updateOne(JingDongProduct jingDongProduct);
	
	Long deleteOne(JingDongProduct jingDongProduct);
	
	List<JingDongProduct> deleteMany(List<JingDongProduct> jingDongProducts);
}
