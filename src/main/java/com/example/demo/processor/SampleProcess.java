package com.example.demo.processor;

import org.springframework.batch.item.ItemProcessor;

import com.example.demo.Entiry.Person;

/**
 * Processor：要素を処理する
 * @author r-toba
 *
 */
public class SampleProcess implements ItemProcessor<Person,Person> {

	@Override
	public Person process(Person person) throws Exception {
	    final Integer id = person.getId();
	    final String name = person.getName().toUpperCase();
	    final String age = person.getAge();
	    final Person person1 = new Person(id,name, age);
		return person1;
	}
}
