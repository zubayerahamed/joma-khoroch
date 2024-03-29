package com.coderslab.restcontroller;

import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.coderslab.model.MonthlyStatus;
import com.coderslab.model.MonthlyStatusList;
import com.coderslab.model.enums.JomaKhorochMonth;
import com.coderslab.model.enums.RecordStatus;
import com.coderslab.service.MonthlyStatusService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Zubayer Ahamed
 *
 */
@Slf4j
@RestController
@RequestMapping("/rest/monthlystatus")
@Api(tags = "Monthly status", description = "View all monthly satatus of current year")
public class MonthlyStatusRestController {

	@Autowired private MonthlyStatusService monthlyStatusService;

	@ApiOperation(value = "View a list of monthly status of current year", response = MonthlyStatusList.class)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "Successfully retrieved list"),
		@ApiResponse(code = 401, message = "You are not authorized to view the resource"),
		@ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
		@ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
	})
	@GetMapping(value = "/list", produces = "application/json")
	public MonthlyStatusList getListOfMonthlyStatus() {
		log.info("Getting Monthy status list ......");
		MonthlyStatusList list = new MonthlyStatusList();

		Calendar calDate = Calendar.getInstance();
		calDate.set(Calendar.DAY_OF_YEAR, 1);
		Date yearStartDate = calDate.getTime();
		calDate.set(Calendar.DAY_OF_YEAR, calDate.getActualMaximum(Calendar.DAY_OF_YEAR));
		Date yearEndDate = calDate.getTime();

		list.getMonthlyStatus().addAll(monthlyStatusService.getListOfMonthlyStatusofCurrentYear(new Long(5), yearStartDate, yearEndDate, RecordStatus.L));
		return list;
	}

	@GetMapping(value = "/{month}")
	public MonthlyStatus getMonthlyStatus(@PathVariable String month) {
		log.info("Getting monthly status with details......");

		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int mon = JomaKhorochMonth.valueOf(month.toUpperCase()).getCode();
		int day = 1;
		c.set(year, mon, day);
		int numOfDaysInMonth = c.getActualMaximum(Calendar.DAY_OF_MONTH);
		Date fromDate = c.getTime();
		c.add(Calendar.DAY_OF_MONTH, numOfDaysInMonth - 1);
		Date toDate = c.getTime();

		return monthlyStatusService.getMonthlyStatus(new Long(5), fromDate, toDate, RecordStatus.L);
	}
}
