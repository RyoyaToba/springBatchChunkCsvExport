package com.example.demo;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import com.example.demo.Entiry.Person;
import com.example.demo.processor.SampleProcess;
import com.example.demo.repository.PersonRepository;
import com.example.demo.writer.SampleWrite;
import org.springframework.jdbc.core.*;

/**
 * DBの情報を取得して、CSVファイル出力するサンプル
 * 
 * @author r-toba
 *
 */
@Configuration
@EnableBatchProcessing
public class BatchConfig {
	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	@Autowired
	private DataSource dataSource;

	@Autowired
	private PersonRepository repository;

	@Autowired
	private EntityManagerFactory entityManagerFactory;

	// Jobの実装
	@Bean
	public Job testJob1() {
		return jobBuilderFactory.get("testJob").incrementer(new RunIdIncrementer()).start(testStep1()).build();
	}

	// Stepの実装
	// Step処理ではreader,processor,writerの順に動作を読み込む
	@Bean
	public Step testStep1() {
		return stepBuilderFactory.get("step1").<Person, Person>chunk(3) // chunk数の指定
				.reader(jdbcCursorItemReaderSample()) // reader指定
				.processor(sampleProcess()) // processorの指定
				.writer(sampleWrite()).build(); // writerの指定
	}

	// readerの部分 CSVファイルの読み込みを行っている
	@Bean
	public FlatFileItemReader<Person> fileReadSample() {
		return new FlatFileItemReaderBuilder<Person>().name("testCSV").resource(new ClassPathResource("person.csv"))
				.delimited().names(new String[] { "id", "name", "age" })
				.fieldSetMapper(new BeanWrapperFieldSetMapper<Person>() {
					{
						setTargetType(Person.class);
					}
				}).build();
	}

	
	// JdbcCursorItemReaderを利用してDB上の情報を読み込む
	@Bean
	public JdbcCursorItemReader<Person> jdbcCursorItemReaderSample() {
		
		String sql = "SELECT * FROM person";
	
		RowMapper<Person> rowMapper = new BeanPropertyRowMapper<>(Person.class);
		
		return new JdbcCursorItemReaderBuilder<Person>().dataSource(this.dataSource)
				.name("jdbcCursorItemReadersample")
				.sql(sql)
				.rowMapper(rowMapper)
				.build();
	}

	// processorのインスタンス化を行う
	@Bean
	public SampleProcess sampleProcess() {
		return new SampleProcess();
	}

	// writerのインスタンス化を行う
	// コンソールへの出力
	@Bean
	public SampleWrite<Person> sampleWrite() {
		return new SampleWrite<Person>();
	}
}