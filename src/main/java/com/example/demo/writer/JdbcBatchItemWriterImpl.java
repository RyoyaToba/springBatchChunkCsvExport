package com.example.demo.writer;

import javax.sql.DataSource;

import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

/**
 * Writerの処理をクラスに分割したかったのですが、分割するとうまく動作しませんでした。
 * なのでConfigの方に直接記述したら動きましたので、それで対応しています。
 * testWrite1は切り分けて動いているのですが、なぜでしょうか
 * 
 * @author r-toba
 *
 * @param <T>
 */
public class JdbcBatchItemWriterImpl<T> {
	
	@Autowired
	private DataSource dataSource;
	
	@Bean
	public JdbcBatchItemWriter<T> write(){
		
		String sql = "INSERT INTO person (name, age) VALUES (:name, :age)";
		
		BeanPropertyItemSqlParameterSourceProvider<T> provider = new BeanPropertyItemSqlParameterSourceProvider<>();
		
		return new JdbcBatchItemWriterBuilder<T>().itemSqlParameterSourceProvider(provider)
												.sql(sql)
												.dataSource(dataSource)
												.build();
		
	}
	

}
