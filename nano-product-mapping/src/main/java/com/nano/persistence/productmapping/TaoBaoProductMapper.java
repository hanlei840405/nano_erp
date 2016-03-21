package com.nano.persistence.productmapping;

import java.util.List;

import org.apache.ibatis.session.RowBounds;

import com.nano.domain.productmapping.TaoBaoProduct;

public interface TaoBaoProductMapper {
	TaoBaoProduct findOne(String id);
	
	List<TaoBaoProduct> findMany(TaoBaoProduct taoBaoProduct);
	
	List<TaoBaoProduct> findMany(TaoBaoProduct taoBaoProduct,RowBounds rowBounds);

	void saveOne(TaoBaoProduct taoBaoProduct);
	
	Long updateOne(TaoBaoProduct taoBaoProduct);
	
	Long deleteOne(TaoBaoProduct taoBaoProduct);
	
	List<TaoBaoProduct> deleteMany(List<TaoBaoProduct> taoBaoProducts);
}
