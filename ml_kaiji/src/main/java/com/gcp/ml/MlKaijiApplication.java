package com.gcp.ml;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.tensorflow.Graph;
import org.tensorflow.Session;
import org.tensorflow.Tensor;
import org.tensorflow.TensorFlow;

@SpringBootApplication
public class MlKaijiApplication {

	public static void main(String[] args) throws Exception{
		SpringApplication.run(MlKaijiApplication.class, args);
		
		
	}  
}
