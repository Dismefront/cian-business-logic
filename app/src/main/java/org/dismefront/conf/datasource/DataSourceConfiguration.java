package org.dismefront.conf.datasource;

import com.atomikos.jdbc.AtomikosDataSourceBean;
import org.postgresql.xa.PGXADataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfiguration {

    @Bean(name="postgresXADataSource")
    @Primary
    public DataSource postgresDataSource() {
        PGXADataSource pgXa = new PGXADataSource();
        pgXa.setUrl("jdbc:postgresql://localhost:5433/mydb");
        pgXa.setUser("dismefront");
        pgXa.setPassword("dismefront");

        AtomikosDataSourceBean xaDataSource = new AtomikosDataSourceBean();
        xaDataSource.setXaDataSource(pgXa);
        xaDataSource.setUniqueResourceName("xa_postgres");
        xaDataSource.setPoolSize(5);

        return xaDataSource;
    }

}
