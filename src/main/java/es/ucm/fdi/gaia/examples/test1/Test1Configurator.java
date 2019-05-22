package es.ucm.fdi.gaia.examples.test1;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.inject.TypeLiteral;
import com.google.inject.name.Names;
import com.jiowa.codegen.JiowaCodeGeneratorEngine;
import com.jiowa.codegen.config.JiowaCodeGenConfig;

import es.ucm.fdi.gaia.jcolibri.cbrcore.Connector;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.NNretrieval.similarity.GlobalSimilarityFunction;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.NNretrieval.similarity.global.Average;
import es.ucm.fdi.gaia.recolibry.api.Query;
import es.ucm.fdi.gaia.recolibry.api.RecSysConfiguration;
import es.ucm.fdi.gaia.recolibry.api.RecommenderAlgorithm;
import es.ucm.fdi.gaia.recolibry.implementations.jcolibri.CSVConnector;
import es.ucm.fdi.gaia.recolibry.implementations.jcolibri.LocalSimilarityConfiguration;
import es.ucm.fdi.gaia.recolibry.implementations.jcolibri.QueryJColibri;
import es.ucm.fdi.gaia.recolibry.implementations.jcolibri.RecommenderJColibri;
import es.ucm.fdi.gaia.recolibry.utils.BeansFactory;
import es.ucm.fdi.gaia.recolibry.utils.ClassGenerator;

/**
 * Class to configure a content-based recommender system for movies.
 * The recommender system uses movie's genres and genres in user profile
 * to search movies to recommend.
 * 
 * @author Jose L. Jorro-Aragoneses
 * @version 1.0
 */
public class Test1Configurator extends RecSysConfiguration {
	
	private static final String PACKAGE_NAME = "es.ucm.fdi.gaia.recolibry.test";
	private static final String CLASS_NAME = "MovieDescription"; 

	@Override
	protected void generateClass() {
		
		 // Define single type attributes with their names and types.
		  String[] attr1 = new String[] {"id", "Integer"};
		  String[] attr2 = new String[] {"title", "String"};
		  
		  // Add single type attributes in a list
		  List<String[]> attributes = new ArrayList<>();
		  attributes.add(attr1);
		  attributes.add(attr2);
		  
		  // Define list type attributes with their names and types.
		  String[] attr3 = new String[] {"genres", "String"};
		  
		  // Add list type attributes in a different list.
		  List<String[]> attributesList = new ArrayList<>();
		  attributesList.add(attr3);
		  
		  // Load Jiowa configuration included in RecoLibry-Core.
		  JiowaCodeGenConfig config = new JiowaCodeGenConfig("codegen.properties");
		  
		  // Add all information to ClassGenerator object.
		  ClassGenerator classGenerator = new ClassGenerator(config);
		  classGenerator.setClassName(CLASS_NAME);
		  classGenerator.setPackageName(PACKAGE_NAME);
		  classGenerator.setAttributes(attributes);
		  classGenerator.setAttributesList(attributesList);
		  
		  // Build the new class with Jiowa engine and the class configuration.
		  JiowaCodeGeneratorEngine engine = new JiowaCodeGeneratorEngine(classGenerator);
		  engine.start();

		  // Save the java file in our project.
		  file = System.getProperty("user.dir").replace("\\","/") + "/src/main/java/" + PACKAGE_NAME.replace(".", "/") + "/" + CLASS_NAME + ".java";
		  file.replace("/", System.getProperty("path.separator"));
		  
	}

	@Override
	protected void configure() {
	 try {
		    
		 //Build MovieDescription class
		 generateClass();
	
		//Compile the class
		compile();
		
		//Save the class object in a variable
		Class<?> clazz = Class.forName(PACKAGE_NAME + "." + CLASS_NAME);
		
		//Configure BeansFactory to make MovieDescription objects
		BeansFactory factory = new BeansFactory(clazz);
		
		//Add BeanFactory to CSVConnector
		bind(BeansFactory.class)
		  .annotatedWith(Names.named("beansFactory"))
		  .toInstance(factory);
		
		//Define the file path
		String path = System.getProperty("user.dir") + "/data/movies.csv";
		
		bind(String.class)
		  .annotatedWith(Names.named("fileName"))
		  .toInstance(path);
		
		//Set that movies.csv contains title row
		 bind(Boolean.class)
		   .annotatedWith(Names.named("existTitleRow"))
		   .toInstance(true);
		
		//Link CSVConnector with Connector of RecommenderJColibri
		bind(Connector.class)
		  .to(CSVConnector.class);
		
		//Configure the list of local similarity functions
		List<LocalSimilarityConfiguration> configurations = new ArrayList<LocalSimilarityConfiguration>();
		LocalSimilarityConfiguration conf = new LocalSimilarityConfiguration("genres", clazz, new GenresSimilarity());
		configurations.add(conf);
		
		//Link local similarity functions
		bind(new TypeLiteral<List<LocalSimilarityConfiguration>>(){})
		  .toInstance(configurations);
		
		//Link global similarity function
		bind(GlobalSimilarityFunction.class)
		  .to(Average.class);
		
		//Link the number of results to recover
		bind(Integer.class)
		  .annotatedWith(Names.named("N-Results"))
		  .toInstance(10);
		
		//Link the algorithm to use in RecoLibry
		bind(RecommenderAlgorithm.class)
		  .to(RecommenderJColibri.class);
		
		//Link the name of Java Bean Class (MovieDescription)
		bind(String.class)
			.annotatedWith(Names.named("BeanClass"))
			.toInstance(PACKAGE_NAME + "." + CLASS_NAME);
		
		//Link the query class used in the recommender system
	  	bind(Query.class)
	    	.to(QueryJColibri.class);
	    
	  } catch (ClassNotFoundException e) {
	    e.printStackTrace();
	  } catch (IOException e) {
	    e.printStackTrace();
	  }

		
	}

}
