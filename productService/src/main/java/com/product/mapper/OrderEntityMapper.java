package com.product.mapper;

import com.product.entity.OrderEntryEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


@Mapper
public interface OrderEntityMapper
{
	@Insert("Insert into order_entries(id, product_id, order_id, quantity) values(#{id},#{productId},#{orderId},#{quantity}) ")
	void insert(OrderEntryEntity entryEntity);

	@Insert({"<script>",
		  "Insert into order_entries(entry_id, product_id, order_id, quantity) values",
		  "<foreach collection='entries' item='entry' index='index' separator=','>",
		  "(#{entry.id},#{entry.productId},#{entry.orderId},#{entry.quantity})",
		  "</foreach>",
		  "</script>"
	})
	void batchInsert(@Param(value = "entries") List<OrderEntryEntity> entries);
}
