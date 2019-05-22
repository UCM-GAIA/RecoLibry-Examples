package es.ucm.fdi.gaia.examples.test1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import es.ucm.fdi.gaia.recolibry.api.Query;
import es.ucm.fdi.gaia.recolibry.api.RecSysConfiguration;
import es.ucm.fdi.gaia.recolibry.api.RecommenderResult;
import es.ucm.fdi.gaia.recolibry.api.RecommenderSystem;
import es.ucm.fdi.gaia.recolibry.api.RecommenderSystemFactory;

public class Test1RecSys {
	
	public static RecommenderSystem getRecommenderSystem() {
	  //Object with recommender system configuration
	  RecSysConfiguration configuration = new Test1Configurator();
	  
	  //Make new instance of recommender system
	  RecommenderSystemFactory factory = new RecommenderSystemFactory();
		factory.makeRecommender(configuration);
	  
	  // Return recommender system
	  return factory.getRecommender();
	}
	
	public static void main(String[] args) {
		
		//Get recommender system
		RecommenderSystem recSys = getRecommenderSystem();
		
		//Get query object
		Query query = recSys.getQuery();
	  
		//Initialize query
		query.initialize();
  
		//Add genres in our query
		String[] genresArray = new String[]{"Adventure", "Children", "Fantasy"};
		List<String> genres = new ArrayList<>(Arrays.asList(genresArray));
		query.setAttributeValue("genres", genres);
    
		//Initialize recommender system
		recSys.initRecommender();
  
		//Get recommendations from query
		List<RecommenderResult> results = recSys.recommend(query);

		// Close recommender system
		recSys.closeRecommender();
  
		// Print the result of recommender system
		for(RecommenderResult r : results)
			System.out.println(r);
		
	}

}
