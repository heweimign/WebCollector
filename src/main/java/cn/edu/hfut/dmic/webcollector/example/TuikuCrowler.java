package cn.edu.hfut.dmic.webcollector.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.springframework.jdbc.core.JdbcTemplate;

import cn.edu.hfut.dmic.webcollector.jdbc.JDBCHelper;
import cn.edu.hfut.dmic.webcollector.model.CrawlDatums;
import cn.edu.hfut.dmic.webcollector.model.Page;
import cn.edu.hfut.dmic.webcollector.plugin.berkeley.BreadthCrawler;

public class TuikuCrowler extends BreadthCrawler {
	
	private JdbcTemplate jdbcTemplate = null;
	
	private Connection conn = null;

	public TuikuCrowler(String crawlPath, boolean autoParse) {
		super(crawlPath, autoParse);
		/*种子页面*/
//		this.addSeed("http://www.tuicool.com/articles/73A7Fv6");
		//正则规则设置
//		this.addRegex("http://www.tuicool.com/articles/*");
		/*不要爬取 jpg|png|gif*/
       // this.addRegex("-.*\\.(jpg|png|gif).*");
        /*不要爬取包含 # 的URL*/
       // this.addRegex("-.*#.*");
	}

	@Override
	public void visit(Page page, CrawlDatums next) {
		String url = page.getUrl();
		//判断是否为文章页
		if(page.matchUrl("^http://www.tuicool.com/*/*")){
			String title = page.getDoc().select("div[class=span8 contant article_detail_bg]>h1").text();
			String content = page.getDoc().select("div#nei").text();
			String sql = "insert into tb_content(title,url,content) value(?,?,?)";
			if(jdbcTemplate!=null){
				try {
					conn = jdbcTemplate.getDataSource().getConnection();
					PreparedStatement pstmt = conn.prepareStatement(sql);
					pstmt.setString(1, title);
					pstmt.setString(2, url);
					pstmt.setString(3, content);
					pstmt.executeUpdate();
					pstmt.close();
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				
			}else{
				JDBCHelper.createMysqlTemplate("mysql1", "jdbc:mysql://localhost:3306/testdb?useUnicode=true&characterEncoding=utf8",
						"root", "root", 5, 30);
			}
		}

	}

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		TuikuCrowler tc = new TuikuCrowler("crowler",true);
		tc.addSeed("http://www.tuicool.com/articles/73A7Fv6");
//		tc.addRegex("http://www.tuicool.com/articles/*");
//		tc.setThreads(5);
//		tc.setTopN(50);
		/*开始深度为4的爬取，这里深度和网站的拓扑结构没有任何关系
        	可以将深度理解为迭代次数，往往迭代次数越多，爬取的数据越多*/
		tc.start(2);
	}

}
