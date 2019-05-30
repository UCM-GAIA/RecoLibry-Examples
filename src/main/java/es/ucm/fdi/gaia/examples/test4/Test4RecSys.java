package es.ucm.fdi.gaia.examples.test4;

import java.util.List;

import es.ucm.fdi.gaia.recolibry.api.Query;
import es.ucm.fdi.gaia.recolibry.api.RecommenderResult;
import es.ucm.fdi.gaia.recolibry.api.RecommenderSystem;
import es.ucm.fdi.gaia.recolibry.api.RecommenderSystemFactory;

public class Test4RecSys {

	public static RecommenderSystem getRecommenderSystem() {
		
		//Make new instance of recommender system
		RecommenderSystemFactory factory = new RecommenderSystemFactory();
		factory.makeRecommenderByJson(System.getProperty("user.dir") + "/configurations/test4.json");
        

        return factory.getRecommender();
	}
	
	public static void main(String[] args) {
		
		RecommenderSystem recSys = getRecommenderSystem();

        Query query = recSys.getQuery();
        long user = 414;
        query.setAttributeValue("userId", user);

        recSys.initRecommender();

        List<RecommenderResult> results = recSys.recommend(query);

        recSys.closeRecommender();

        for(RecommenderResult r : results)
            System.out.println(r);
		
	}
	
}
