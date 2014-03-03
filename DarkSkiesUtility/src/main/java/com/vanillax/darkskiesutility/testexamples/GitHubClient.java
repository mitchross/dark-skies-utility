/*
 * Copyright (C) 2012 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.vanillax.darkskiesutility.testexamples;

import android.util.Log;

import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class GitHubClient {
  public static final String API_URL = "https://api.github.com";

  public static void connectToAPI() {
    // Create a very simple REST adapter which points the GitHub API endpoint.
    RestAdapter restAdapter = new RestAdapter.Builder()
        .setServer(API_URL)
        .build();

    // Create an instance of our GitHub API interface.
    GitHub github = restAdapter.create(GitHub.class);



	 Callback callback = new  Callback<List <Contributor> >()
	 {
		 @Override
		 public void success( List<Contributor> contributors, Response response )
		 {
			 for (Contributor contributor : contributors) {
				 Log.d("mytag", contributor.myLogin + " (" + contributor.contributions + ")" );



			 }
		 }

		 @Override
		 public void failure( RetrofitError retrofitError )
		 {

		 }
	 };

	  // Fetch and print a list of the contributors to this library.
	  github.contributors("square", "retrofit" , callback);


    }
  }

