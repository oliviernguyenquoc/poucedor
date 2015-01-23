package fr.poucedor.poucedor.provider;

import retrofit.RestAdapter;

/**
 * Created by loic on 09/01/15.
 */
public class RestFactory {


    protected PoucedorService pouceRest;

    public RestFactory() {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("http://www.poucedor.fr/") // The base API endpoint.
                .build();

        this.pouceRest = restAdapter.create(PoucedorService.class);
    }

    public PoucedorService getPouceRest() {
        return pouceRest;
    }
}
