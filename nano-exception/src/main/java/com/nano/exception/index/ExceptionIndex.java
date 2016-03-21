package com.nano.exception.index;

public class ExceptionIndex {

	public final static String DB_SELECT_EXCEPTION = "[%s]数据查询失败";

	public final static String DB_INSERT_EXCEPTION = "[%s]数据插入失败";

	public final static String DB_UPDATE_EXCEPTION = "[%s]数据更新失败";

	public final static String DB_DELETE_EXCEPTION = "[%s]数据删除失败";

	public final static String DB_DELETE_HAVE_CHILDREN_EXCEPTION = "[%s]存在级联数据，请先删除级联数据";

	public final static String DB_TRANSACTION_CONSISTENCY_EXCEPTION = "[%s]事务提交前数据被其他进程更新";

	public final static String EXCEL_READ_NO_SHEET_EXCEPTION = "[%s]excel文件没有sheet页";
}
