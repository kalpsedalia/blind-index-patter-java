package com.medium.blindindex;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;

@SpringBootApplication
public class BlindIndexPatternApplication {

	public static void main(String[] args) {
		SpringApplication.run(BlindIndexPatternApplication.class, args);
	}


	@Autowired
	private MongoDatabaseFactory mongoDatabaseFactory;

	@Bean
	public MongoTemplate mongoTemplate() {
		MappingMongoConverter converter = new MappingMongoConverter(mongoDatabaseFactory, new MongoMappingContext());
		converter.setTypeMapper(new DefaultMongoTypeMapper(null));
		return new MongoTemplate(mongoDatabaseFactory, converter);
	}
}
