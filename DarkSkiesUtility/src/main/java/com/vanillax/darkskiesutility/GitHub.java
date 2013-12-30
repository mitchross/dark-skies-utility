package com.vanillax.darkskiesutility;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by mitchross on 12/16/13.
 */
public interface GitHub {

	@GET("/repos/{owner}/{repo}/contributors")
		//List<Contributor> contributors(
	void contributors(
			@Path( "owner" )
			String owner,
			@Path( "repo" )
			String repo,
			Callback< List<Contributor> > callback
	);
}
