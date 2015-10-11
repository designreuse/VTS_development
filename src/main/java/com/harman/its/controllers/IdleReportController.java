package com.harman.its.controllers;



import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

import com.harman.its.dao.impl.IdleDaoImpl;
import com.harman.its.dao.impl.TripDaoImp;
import com.harman.its.entity.IdlePoints;
import com.harman.its.entity.TripsEntity;
import com.harman.its.utils.SessionUtils;



/**
 * Controller for Home page
 * View : "home"
 *  
 * @author VAmukapati
 *
 */
public class IdleReportController extends SimpleFormController {

	Logger logger = Logger.getLogger(IdleReportController.class);

	public ModelAndView handleRequestInternal(HttpServletRequest request ,HttpServletResponse response) throws ClassNotFoundException, SQLException{
		ModelAndView model = new ModelAndView("idleReport");
		String fromDate=request.getParameter("from");
		String toDate=request.getParameter("to");
		String fromHrs=request.getParameter("fhrs");
		String fromMin=request.getParameter("fmin");
		String fromSec=request.getParameter("fsec");
		String toHrs=request.getParameter("thrs");
		String toMin=request.getParameter("tmin");
		String toSec=request.getParameter("tsec");
		String startDateString = "2010-10-02 "+fromHrs+":"+fromMin+":"+fromSec;
		String endDateString = toDate+" "+toHrs+":"+toMin+":"+toSec;
		logger.debug("Start Date : "+startDateString+" , End Date : "+endDateString);

		String vehicleIdString =request.getParameter("vehicleId");
		logger.debug("Vehicle Id is "+vehicleIdString);
		try {
			if(vehicleIdString!=null){
				TripDaoImp tripDao = new TripDaoImp();
				List<TripsEntity> tripsList = tripDao.selectTripsByUserId(SessionUtils.getCurrentlyLoggedInUser().getId());
				IdleDaoImpl idleDaoImpl = new IdleDaoImpl();
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-dd hh:mm:ss");
				Date startDate = simpleDateFormat.parse(startDateString);

				Date endDate = simpleDateFormat.parse(endDateString);
				logger.debug("No.of trips"+tripsList.size()+" for Vehicle Id : "+vehicleIdString);
				List<IdlePoints> idlePointResultset = idleDaoImpl.selectAllIdlePointsBetweenDatesWithLimit(3, startDate,endDate);
				logger.debug("Finally idle points size is ::::::: "+idlePointResultset.size());
				model.addObject("idlPointsData", idlePointResultset);
				model.addObject("from", startDateString);
				model.addObject("to", endDateString);
			} 
		}catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return model;
	}
}