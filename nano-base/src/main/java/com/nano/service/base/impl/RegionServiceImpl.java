package com.nano.service.base.impl;

import com.github.pagehelper.PageInfo;
import com.nano.domain.base.Region;
import com.nano.domain.base.RegionExpress;
import com.nano.exception.NanoDBConsistencyRuntimeException;
import com.nano.exception.NanoExcelReadRuntimeException;
import com.nano.exception.index.ExceptionIndex;
import com.nano.persistence.base.RegionMapper;
import com.nano.service.base.RegionExpressService;
import com.nano.service.base.RegionService;
import org.apache.ibatis.session.RowBounds;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

@Service
public class RegionServiceImpl implements RegionService {
	private static final Logger logger = LoggerFactory
			.getLogger(RegionServiceImpl.class);
	@Autowired
	private RegionMapper regionMapper;
	@Autowired
	private RegionExpressService regionExpressService;

	@Transactional
	public Region save(Region region) {
		regionMapper.saveOne(region);
		return region;
	}

	@Transactional
	public void delete(String id) {
		Region region = findOne(id);
		Region queryParam = new Region();
		queryParam.setParentId(id);
		List<Region> regions = regionMapper.findMany(queryParam);
		if (!regions.isEmpty()) {
			throw new NanoDBConsistencyRuntimeException(String.format(
					ExceptionIndex.DB_DELETE_HAVE_CHILDREN_EXCEPTION,
					region.getId()));
		}
		long resultCnt = regionMapper.deleteOne(region);
		if (resultCnt == 0) {
			throw new NanoDBConsistencyRuntimeException(String.format(
					ExceptionIndex.DB_TRANSACTION_CONSISTENCY_EXCEPTION,
					region.getId()));
		}
		RegionExpress regionExpress = new RegionExpress();
		regionExpress.setRegionId(id);
		regionExpressService.delete(regionExpress);
	}

	@Transactional
	public void delete(List<String> ids) {
		for (String id : ids) {
			delete(id);
		}
	}

	public Region findOne(String id) {
		return regionMapper.findOne(id);
	}

	@Transactional
	public void update(Region region) {
		long resultCnt = regionMapper.updateOne(region);
		if (resultCnt == 0) {
			throw new NanoDBConsistencyRuntimeException(String.format(
					ExceptionIndex.DB_TRANSACTION_CONSISTENCY_EXCEPTION,
					region.getId()));
		}
	}

	@Transactional
	public void update(List<Region> regions) {
		for (Region region : regions) {
			update(region);
		}

	}

	public PageInfo<Region> find(Region region, int pageNo, int pageSize) {
		RowBounds rowBounds = new RowBounds(pageNo, pageSize);
		List<Region> regions = regionMapper.findMany(region, rowBounds);
		PageInfo<Region> pageInfo = new PageInfo<Region>(regions);
		return pageInfo;
	}

	public List<Region> find(Region region) {
		return regionMapper.findMany(region);
	}

	public List<Region> findRoots() {
		return regionMapper.findRoot();
	}

	@Transactional
	public void save(byte[] in) {
		HSSFWorkbook excel = null;
		try {
			ByteArrayInputStream bis = new ByteArrayInputStream(in);
			excel = new HSSFWorkbook(bis);
		} catch (IOException e) {
			logger.error("读取文件失败，文件损坏");
		}
		final HSSFSheet hssfSheet;
		try {
			hssfSheet = excel.getSheetAt(0);
		} catch (RuntimeException e) {
			logger.error(String.format(
					ExceptionIndex.EXCEL_READ_NO_SHEET_EXCEPTION, "行政区划编码"));
			throw new NanoExcelReadRuntimeException(String.format(
					ExceptionIndex.EXCEL_READ_NO_SHEET_EXCEPTION, "行政区划编码"));
		}

		Runnable saveThread = new Runnable() {
			@Override
			public void run() {
				Region queryParam = new Region();
				RowBounds rowBounds = new RowBounds(0, 1);
				for (int rowNum = 1; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
					Region region = new Region();
					HSSFRow hssfRow = hssfSheet.getRow(rowNum);
					String code = String.valueOf((int) hssfRow.getCell(0)
							.getNumericCellValue());
					String name = hssfRow.getCell(1).getStringCellValue();
					String parent = String.valueOf((int) hssfRow.getCell(2)
							.getNumericCellValue());
					region.setCode(code);
					region.setName(name);
					queryParam.setCode(parent);
					List<Region> parents = regionMapper.findMany(queryParam,
							rowBounds);
					if (!parents.isEmpty()) {
						region.setParentId(parents.get(0).getId());
					}
					save(region);
				}
			}
		};
		Thread start = new Thread(saveThread);
		start.start();
	}
}
