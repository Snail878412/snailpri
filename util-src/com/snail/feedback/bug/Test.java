package com.snail.feedback.bug;

import java.util.ArrayList;
import java.util.List;

import com.snail.common.mail.MailEngine;
import com.snail.feedback.bug.bean.BugDesc;
import com.snail.feedback.bug.bean.BugFeedback;
import com.snail.feedback.bug.bean.Observer;
import com.snail.feedback.bug.bean.Presenter;
import com.snail.feedback.bug.bean.Solver;
import com.snail.feedback.bug.mail.BugFeedBackMailMessageBuilder;

public class Test {

	public static void main(String[] args) {

		BugFeedback bugFeedback = new BugFeedback();

		bugFeedback.setBugName("测试");
		Presenter presenter = new Presenter();
		presenter.setName("测试员1");
		presenter.setEmail("371755616@qq.com");
		bugFeedback.setPresenter(presenter);

		List<Observer> observers = new ArrayList<Observer>();
		Observer observer = new Observer();
		observer.setEmail("517141072@qq.com");
		observers.add(observer);
		bugFeedback.setObservers(observers);

		List<Solver> solvers = new ArrayList<Solver>();
		Solver solver = new Solver();
		solver.setEmail("shui878412@126.com");
		solvers.add(solver);
		bugFeedback.setSolvers(solvers);

		
		BugDesc bugDesc = new BugDesc();
		bugDesc.setPriority(BugDesc.BUG_PRIORITY_SUGGESTIVE);
		bugDesc.setTextDesc("这是一个测试邮件系统的bug这是一个测试邮件系统的bug这是一个测试邮件系统的bug这是一个测试邮件系统的bug这是一个测试邮件系统的bug这是一个测试邮件系统的bug这是一个测试邮件系统的bug这是一个测试邮件系统的bug这是一个测试邮件系统的bug这是一个测试邮件系统的bug这是一个测试邮件系统的bug这是一个测试邮件系统的bug这是一个测试邮件系统的bug这是一个测试邮件系统的bug这是一个测试邮件系统的bug这是一个测试邮件系统的bug这是一个测试邮件系统的bug这是一个测试邮件系统的bug这是一个测试邮件系统的bug这是一个测试邮件系统的bug这是一个测试邮件系统的bug这是一个测试邮件系统的bug这是一个测试邮件系统的bug这是一个测试邮件系统的bug这是一个测试邮件系统的bug这是一个测试邮件系统的bug这是一个测试邮件系统的bug这是一个测试邮件系统的bug这是一个测试邮件系统的bug这是一个测试邮件系统的bug这是一个测试邮件系统的bug这是一个测试邮件系统的bug这是一个测试邮件系统的bug这是一个测试邮件系统的bug这是一个测试邮件系统的bug这是一个测试邮件系统的bug这是一个测试邮件系统的bug这是一个测试邮件系统的bug");
		List<String> attachment = new ArrayList<String>();
		attachment.add("E:\\个人资料\\壁纸\\2-13030Q54Z8.jpg");
		attachment.add("E:\\个人资料\\壁纸\\图片中文名称测试.jpg");
		bugDesc.setAttachment(attachment);
		bugFeedback.setBugDesc(bugDesc );
		BugFeedBackMailMessageBuilder builder = new BugFeedBackMailMessageBuilder(
				bugFeedback);

		MailEngine.getInstance().sendMail(builder.build());
	}
}
