//package avengers.waffle.configuration.ssh;
//
//import com.jcraft.jsch.JSch;
//import com.jcraft.jsch.JSchException;
//import com.jcraft.jsch.Session;
//import lombok.RequiredArgsConstructor;
//import org.apache.ibatis.session.SqlSessionFactory;
//import org.mybatis.spring.SqlSessionFactoryBean;
//import org.mybatis.spring.annotation.MapperScan;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.io.Resource;
//import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
//import org.springframework.jdbc.datasource.DataSourceTransactionManager;
//import org.springframework.jdbc.datasource.DriverManagerDataSource;
//import org.springframework.transaction.PlatformTransactionManager;
//import org.springframework.transaction.annotation.EnableTransactionManagement;
//
//import javax.sql.DataSource;
//
//@Configuration
//@EnableTransactionManagement
//@RequiredArgsConstructor
//public class DataSourceConfig {
//    @Value("${ssh.host}")
//    private String sshHost;
//    @Value("${ssh.port}")
//    private int sshPort;
//    @Value("${ssh.username}")
//    private String sshUsername;
//    @Value("${ssh.password}")
//    private String sshPassword;
//    @Value("${ssh.private_key}")
//    private String sshPrivateKey;
//    @Value("${ssh.local_port}")
//    private int sshLocalPort;
//    @Value("${ssh.remote_host}")
//    private String sshRemoteHost;
//    @Value(("${ssh.remote_port}"))
//    private int sshRemotePort;
//    @Value(value = "${spring.datasource.driver-class-name}")
//    String driverClassName;
//
//    @Value(value = "${spring.datasource.url}")
//    String jdbcUrl;
//
//    @Value(value = "${spring.datasource.username}")
//    String dbUserName;
//
//    @Value(value = "${spring.datasource.password}")
//    String dbUserPw;
//
//    @Value(value = "${mybatis.mapper-locations}")
//    String mapperPath;
//
//    @Bean
//    public DataSource dataSource() throws JSchException {
//        JSch jsch = new JSch();
//        jsch.addIdentity(sshPrivateKey);
//
//        Session session = jsch.getSession(sshUsername, sshHost, sshPort);
//        session.setPassword(sshPassword);
//        session.setConfig("StrictHostKeyChecking", "no");
//        session.connect();
//
//        int localPort = session.setPortForwardingL(sshLocalPort, sshRemoteHost, sshRemotePort);
//
//        DriverManagerDataSource dataSource = new DriverManagerDataSource();
//        dataSource.setDriverClassName(driverClassName);
//        dataSource.setUrl(jdbcUrl);
//        dataSource.setUsername(dbUserName);
//        dataSource.setPassword(dbUserPw);
//
//        return dataSource;
//    }
//
//    @Bean
//    public SqlSessionFactory sqlSessionFactory() throws Exception {
//        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
//        sqlSessionFactoryBean.setDataSource(dataSource());
//
//        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
//        Resource[] mapperLocations = resolver.getResources(mapperPath);
//        sqlSessionFactoryBean.setMapperLocations(mapperLocations);
//
//        return sqlSessionFactoryBean.getObject();
//    }
//
//    @Bean
//    public PlatformTransactionManager transactionManager() throws Exception {
//        return new DataSourceTransactionManager(dataSource());
//    }
//
//}
