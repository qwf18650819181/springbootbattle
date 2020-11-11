package pactera.tf.chat.config.druid;

import javax.sql.DataSource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;

/**
 * druid连接池
 * 
 * @author pactera
 *
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
@Configuration
public class DruidConfig {
	
	@Bean
	@ConfigurationProperties(prefix = "spring.datasource")
	public DataSource druidDataSource() {
		return new DruidDataSource();
	}

	/**
	 * 实现web监控的配置处理
	 * 
	 * @return
	 */
	@Bean
	public ServletRegistrationBean druidServlet() {
		// 表示进行druid监控的配置处理操作
		ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(new StatViewServlet(),
				"/druid/*");
		servletRegistrationBean.addInitParameter("allow", "127.0.0.1");// 白名单
		servletRegistrationBean.addInitParameter("deny", "");// 黑名单
		servletRegistrationBean.addInitParameter("loginUsername", "admin");// 用户名
		servletRegistrationBean.addInitParameter("loginPassword", "123456");// 密码
		servletRegistrationBean.addInitParameter("resetEnable", "false");// 是否可以重置数据源
		return servletRegistrationBean;

	}

	/**
	 * 监控
	 * 
	 * @return
	 */
	@Bean
	public FilterRegistrationBean filterRegistrationBean() {
		FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
		filterRegistrationBean.setFilter(new WebStatFilter());
		filterRegistrationBean.addUrlPatterns("/*");// 所有请求进行监控处理
		filterRegistrationBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.css,/druid/*");// 排除
		return filterRegistrationBean;
	}


}
