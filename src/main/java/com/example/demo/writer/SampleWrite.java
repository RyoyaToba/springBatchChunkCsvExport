package com.example.demo.writer;

import java.util.List;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 
 * writer（書き出す）の処理
 * @author r-toba
 *
 * @param <T>
 */
public class SampleWrite<T> implements ItemWriter<T> { 
	
    @Override
    public void write(List<? extends T> items) throws Exception { 
        items.forEach(System.out::println); 
    } 
}