package es.ucm.fdi.gaia.examples.test3;

import java.io.File;

import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

import com.google.inject.name.Names;

import es.ucm.fdi.gaia.recolibry.api.Query;
import es.ucm.fdi.gaia.recolibry.api.RecSysConfiguration;
import es.ucm.fdi.gaia.recolibry.api.RecommenderAlgorithm;
import es.ucm.fdi.gaia.recolibry.implementations.mahout.cfUser.MahoutCFUserQuery;
import es.ucm.fdi.gaia.recolibry.implementations.mahout.cfUser.MahoutUserNeighborhood;
import es.ucm.fdi.gaia.recolibry.implementations.mahout.cfUser.MahoutUserSimilarity;
import es.ucm.fdi.gaia.recolibry.implementations.mahout.cfUser.RecommenderMahoutCFUser;
import es.ucm.fdi.gaia.recolibry.implementations.mahout.models.MahoutFileDataModel;

public class Test3Configuration extends RecSysConfiguration{

	@Override
    protected void generateClass() {}

    @Override
    protected void configure() {

        File file = new File(System.getProperty("user.dir") + "/data/ratings.csv");

        bind(File.class)
                .annotatedWith(Names.named("source"))
                .toInstance(file);

        bind(String.class)
                .annotatedWith(Names.named("delimiter"))
                .toInstance(",");

        bind(String.class)
                .annotatedWith(Names.named("TypeUserSimilarity"))
                .toInstance("Euclidean");

        bind(DataModel.class)
                .to(MahoutFileDataModel.class);

        bind(String.class)
                .annotatedWith(Names.named("TypeNeighborhood"))
                .toInstance("NearestN");

        bind(Integer.class)
                .annotatedWith(Names.named("N-Users"))
                .toInstance(5);

        bind(Double.class)
                .annotatedWith(Names.named("Threshold"))
                .toInstance(0.0);

        bind(UserNeighborhood.class)
                .to(MahoutUserNeighborhood.class);

        bind(UserSimilarity.class)
                .to(MahoutUserSimilarity.class);

        bind(Integer.class)
                .annotatedWith(Names.named("numResults"))
                .toInstance(15);

        bind(RecommenderAlgorithm.class)
                .to(RecommenderMahoutCFUser.class);

        bind(Query.class)
                .to(MahoutCFUserQuery.class);

    }

}
