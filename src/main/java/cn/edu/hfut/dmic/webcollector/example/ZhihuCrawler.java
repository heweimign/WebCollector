package cn.edu.hfut.dmic.webcollector.example;

import java.util.regex.Pattern;

import cn.edu.hfut.dmic.webcollector.model.CrawlDatums;
import cn.edu.hfut.dmic.webcollector.model.Page;
import cn.edu.hfut.dmic.webcollector.plugin.berkeley.BreadthCrawler;

public class ZhihuCrawler extends BreadthCrawler {

	public ZhihuCrawler(String crawlPath, boolean autoParse) {
		super(crawlPath, autoParse);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void visit(Page page, CrawlDatums next) {
		// TODO Auto-generated method stub
		String question_regex="^http://www.zhihu.com/question/[0-9]+";
	    if(Pattern.matches(question_regex, page.getUrl())){
	      System.out.println("正在抽取"+page.getUrl());
	      String title=page.getDoc().title();
	      System.out.println(title);
	      String question=page.getDoc().select("div[id=zh-question-detail]").text();
	      System.out.println(question);
	      
	    }
	}

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		ZhihuCrawler zhc = new ZhihuCrawler("zhc",true);
		zhc.addSeed("http://www.zhihu.com/question/21003086");
		zhc.start(5);
		

	}

}
