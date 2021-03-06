package org.soft.recruitment.controller;

import java.util.List;

import org.soft.recruitment.model.Company;
import org.soft.recruitment.model.Job;
import org.soft.recruitment.model.Message;
import org.soft.recruitment.service.ICompanyService;
import org.soft.recruitment.service.IJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@Scope("prototype")
@RequestMapping("/job")
public class JobController {
	@Autowired
	private IJobService jobService;

	@Autowired
	private ICompanyService companyService;

	/**
	 * 查询出所有职位信息
	 * jobInfoExt 职位详细信息对象
	 * @param jobName
	 * @param jobAddress
	 * @param companyName
	 * @param model
	 * @return
	 */
	@RequestMapping("findAllJob")
	public String findAllJob(String jobName,String jobAddress,String companyName,Model model){
		List<Job> jobList = jobService.findAllJob(jobName,jobAddress,companyName);
		model.addAttribute("jobList", jobList);
		return "/job/allJob";
	}

	/**
	 * 浏览单个工作的的详细信息
	 * @param model
	 * @param jobId
	 * @return
	 */
	@RequestMapping("showAJob")
	public String showAJob(Model model, Integer jobId, Integer companyId) {
		Job job = jobService.findJobByJobId(jobId);
		if (job != null) {
			model.addAttribute("job", job);
		}

		Company company = companyService.findCompanyByCompanyId(companyId);
		if (company != null) {
			model.addAttribute("company", company);
		}

		return "/job/showAJob";
	}

	/**
	 * 添加职位
	 * @param request
	 * @param jobName
	 * @param jobAddress
	 * @param jobSalary
	 * @param jobEr
	 * @param jobEducation
	 * @param jobType
	 * @param jobReleaseTime
	 * @param jobNumber
	 * @param jobWelfare
	 * @param jobContent
	 * @param jobRequirements
	 * @param companyId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("toAddPost")
	public Message toAddPost(HttpServletRequest request, String jobName, String jobAddress, String jobSalary, String jobEr,
						   String jobEducation, String jobType, String jobReleaseTime, String jobNumber, String jobWelfare,
						   String jobContent, String jobRequirements, Integer companyId) {
		Message msg = new Message();
		Company company = (Company) request.getSession().getAttribute("currCompany");
		if (company != null) {
			Job job = jobService.findJobNameByCompanyId(companyId, jobName);
			if (job == null) {
				//将数据封装到新的job对象，插入数据库
				Job job_ = new Job();
				job_.setJobName(jobName);
				job_.setJobSalary(jobSalary);
				job_.setJobAddress(jobAddress);
				job_.setJobEr(jobEr);
				job_.setJobEducation(jobEducation);
				job_.setJobType(jobType);
				job_.setJobReleaseTime(jobReleaseTime);
				job_.setJobNumber(jobNumber);
				job_.setJobWelfare(jobWelfare);
				job_.setJobContent(jobContent);
				job_.setJobRequirements(jobRequirements);
				job_.setCompanyId(String.valueOf(companyId));

				int result = jobService.insertJob(job_);
				if (result == 1) {
					msg.setStr("Yes");
					return msg;
				} else {
					msg.setStr("发布失败");
					return msg;
				}
			} else {
				msg.setStr("发布的职位名称已存在");
				return msg;
			}
		} else {
			msg.setStr("No");
			return msg;
		}
	}

	/**
	 * 跳转到添加职位页面
	 * @param model
	 * @return
	 */
	@RequestMapping("addPost")
	public String addPost(Model model) {
		return "/job/addPost";
	}
}
